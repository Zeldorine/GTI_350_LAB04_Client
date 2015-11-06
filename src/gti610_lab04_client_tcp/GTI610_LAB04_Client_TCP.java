package gti610_lab04_client_tcp;

import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;
import network.CommunicationSocket;

/**
 *
 * @author Tony CAZORLA
 */
public class GTI610_LAB04_Client_TCP extends Thread implements Observer {

    private static CommunicationSocket communicationSocket;

    private boolean waitMessage = false;

    public static final String ERROR_CODE_SERVER = "1";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        GTI610_LAB04_Client_TCP client = new GTI610_LAB04_Client_TCP();
        client.start();
    }

    public GTI610_LAB04_Client_TCP() {
        System.out.println("\t Lancement du client");
        System.out.print("* Creation et demarrage du client TCP/IP ");
        System.out.println("(V 1.0.0)\n");

        communicationSocket = new CommunicationSocket("127.0.0.1", 9980, "Socket de communication avec le serveur pour GTI610", this);

        System.out.println("Quel message voulez-vous envoyer ?\n");
        while (communicationSocket.estConnecte()) {
            if (!waitMessage) {
                Scanner sc = new Scanner(System.in);
                String message = sc.nextLine();
                envoyerMessage(message);
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof String) {
            recevoirMessage((String) arg);
        }
    }

    private void recevoirMessage(String message) {
        if (message.equalsIgnoreCase(ERROR_CODE_SERVER)) {
            finCommunication();
        } else {
            System.out.println(message + "\n");
            waitMessage = false;
        }
    }

    private void envoyerMessage(String message) {
        communicationSocket.envoyerMessage(message);
        
        if (message.equalsIgnoreCase("exit")) {
            System.out.println("Alpha a bravo - Fin de la transmission\n");
            finCommunication();
        } else {
            waitMessage = true;
        }
    }

    private void finCommunication() {
        communicationSocket.stopperEmission();
        communicationSocket.stopperReception();
        communicationSocket.fermerForce();

        System.exit(0);
    }
}
