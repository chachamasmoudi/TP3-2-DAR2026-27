import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/*** Chaque Thread est dédié à UN client : reçoit des objets Operation, calcule,
   * renvoie l'objet complété. Un thread = une communication indépendante*/

public class ClientProcess extends Thread {

    private final Socket socket;
    private final int clientNumber;

    public ClientProcess(Socket socket, int clientNumber) {
        this.socket = socket;
        this.clientNumber = clientNumber;
    }

    @Override
    public void run() {
        try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
            out.flush(); // envoie l'en-tête du flux pour ne pas bloquer le client
            try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

                // Envoi du numéro d'ordre du client
                out.writeObject("Bienvenue ! Vous êtes le client n°" + clientNumber);
                out.flush();

                while (true) {
                    Object recu = in.readObject();

                    // Un String "bye" signale la fin de la session
                    if (recu instanceof String) {
                        System.out.println("[Client n°" + clientNumber + "] " + recu);
                        out.writeObject("Au revoir client n°" + clientNumber);
                        out.flush();
                        break;
                    }

                    Operation op = (Operation) recu;
                    calculer(op);
                    System.out.println("[Client n°" + clientNumber + "] " + op);

                    // Chaque opération calculée : incrément + affichage du compteur global
                    CompteurGlobal.incrementer("client n°" + clientNumber);

                    out.reset(); // évite de renvoyer une version en cache de l'objet
                    out.writeObject(op);
                    out.flush();
                }
            }
        } catch (EOFException e) {
            System.out.println("Client n°" + clientNumber + " a fermé la connexion.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erreur avec le client n°" + clientNumber + " : " + e.getMessage());
        } finally {
            try { socket.close(); } catch (IOException ignored) { }
            System.out.println("Client n°" + clientNumber + " déconnecté.");
        }
    }

    /** Le service de calculatrice : complète l'objet Operation. */
    private void calculer(Operation op) {
        double a = op.getOperande1();
        double b = op.getOperande2();
        switch (op.getOperateur()) {
            case '+': op.setResultat(a + b); break;
            case '-': op.setResultat(a - b); break;
            case '*': op.setResultat(a * b); break;
            case '/':
                if (b == 0) op.setErreur("Division par zéro");
                else op.setResultat(a / b);
                break;
            default:
                op.setErreur("Opérateur inconnu : " + op.getOperateur());
        }
    }
}