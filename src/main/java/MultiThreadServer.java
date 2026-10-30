import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//Serveur multi-thread du service de calculatrice (communication par objets).
//Un thread ClientProcess est créé pour chaque client accepté.

public class MultiThreadServer {

    private static final int PORT = 1234;

    public static void main(String[] args) {
        int clientCount = 0;

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Serveur calculatrice démarré sur le port " + PORT + "...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                clientCount++;
                System.out.println("Client n°" + clientCount + " connecté : "
                        + clientSocket.getRemoteSocketAddress());

                new ClientProcess(clientSocket, clientCount).start();
            }
        } catch (IOException e) {
            System.err.println("Erreur serveur : " + e.getMessage());
        }
    }
}