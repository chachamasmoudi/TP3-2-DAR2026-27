import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

//Client de la calculatrice (communication par objets).

public class Client {

    public static void main(String[] args) {
        String host = (args.length > 0) ? args[0] : "localhost";
        int port = (args.length > 1) ? Integer.parseInt(args[1]) : 1234;

        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(InetAddress.getByName(host), port), 5000);

            // Ordre important : ObjectOutputStream d'abord (+ flush), puis ObjectInputStream
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            Scanner clavier = new Scanner(System.in);

            System.out.println((String) in.readObject()); // message de bienvenue

            while (true) {
                System.out.print("Opération (ex: 12 + 5) ou 'bye' > ");
                if (!clavier.hasNextLine()) break;
                String ligne = clavier.nextLine().trim();

                if (ligne.equalsIgnoreCase("bye") || ligne.equalsIgnoreCase("exit")) {
                    out.writeObject("bye");
                    out.flush();
                    System.out.println((String) in.readObject());
                    break;
                }

                String[] p = ligne.split("\\s+");
                if (p.length != 3 || p[1].length() != 1) {
                    System.out.println("Format invalide. Exemple : 12 + 5");
                    continue;
                }
                try {
                    Operation op = new Operation(Double.parseDouble(p[0]), p[1].charAt(0), Double.parseDouble(p[2]));
                    out.reset();
                    out.writeObject(op);
                    out.flush();

                    Operation reponse = (Operation) in.readObject();
                    System.out.println("Réponse du serveur : " + reponse);
                } catch (NumberFormatException e) {
                    System.out.println("Les opérandes doivent être des nombres.");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Communication impossible avec le serveur : " + e.getMessage());
        }
    }
}