package Aitor;

import helpers.Crypto;

import javax.crypto.Cipher;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("Duplicates")
public class SPU_9_server {
    private static final String CLOSE = "TANCARCONNEXIO";
    private static final String START = "INICIARCONNEXIO";
    private static final String CHAT = "CHAT";
    private static final String RCTRL = "RETORNCTRL";
    private static final String PKEY = "CLAUPUBLICA";
    private static final String MSG = "MISSATGEENCRIPTAT";
    private static final String MSGEND = "MISSATGEENCRIPTATFI";

    static final int PORT = 9090;
    static final String SEPARATOR = "$##$";
    private static boolean endCom = false;

    private static PublicKey clientPublicKey;

    private static void listen() {
        Socket clientSocket = null;

        //Es crea un ServerSocket que atendrà el port nº PORT a l'espera de clients que demanin comunicar-se.
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (!endCom) {
                clientSocket = serverSocket.accept();
                //Processem la petició del client.
                processClientCommunication(clientSocket);

                //Tanquem el sòcol temporal per atendre el client.
                closeClient(clientSocket);
                System.out.println("El client s'ha desconnectat, esperant a que es connecti un altre client...");
            }
            //Tanquem el sòcol principal
            if((serverSocket != null) && (!serverSocket.isClosed())){
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processClientCommunication(Socket clientSocket) {
        BufferedReader clientIN = null;
        PrintWriter clientOUT = null;
        String clientMessage = "";
        String messageToClient[] = {"", ""}; // [0]header, [1]body

        try {
            clientIN = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            clientOUT = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);

            // Processa la petició de connexió del client.
            do {
                clientMessage = clientIN.readLine();
                System.out.println("============= C: "+clientMessage);

                if (!clientMessage.equals("!")) {
                    messageToClient = processClientMessage(clientMessage, clientIN, clientSocket, clientOUT);

                    if (!messageToClient[0].equals(MSG)) {
                        clientOUT.println(messageToClient[1]);
                        clientOUT.flush();
                    } else {
                        sendCryptedMessage(messageToClient, clientOUT);
                    }
                }
            } while (!messageToClient[0].equals(CLOSE));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendCryptedMessage(String[] messageToClient, PrintWriter clientOUT) {
        // TODO
        //Enviem el codi MISSATGEENCRIPTAT.

        //Enviem el text encriptat. Fem 1 enviamente de cada linia que conté.

        //Enviem el codi CLAUENCRIPTADA.

        //Enviem la clau AES encriptada. Fem 1 enviamente de cada linia que conté.

        //Enviem el codi MISSATGEENCRIPTATFI.
        clientOUT.println(MSGEND + SEPARATOR);
        clientOUT.flush();
    }

    private static String[] processClientMessage(String clientMessage, BufferedReader clientIN, Socket clientSocket, PrintWriter clientOUT) {
        StringTokenizer st = new StringTokenizer(clientMessage, SEPARATOR);
        String messageType = st.nextToken();
        String messageBody = st.nextToken();
        String messageToClient[] = {"", ""};

        boolean opcioCorrecta;
        String opcio;

        System.out.printf("MiniLog. Missatge del client => H: %s B: %s\n\n", messageType, messageBody);

        switch (messageType) {
            case START: {
                System.out.println("=== INICI DE LA CONNEXIÓ AMB UN CLIENT ===");
                break;
            }
            case CLOSE: {
                System.out.println("=== FI DE LA CONNEXIÓ AMB EL CLIENT ===");
                System.out.println("El server tanca la comunicació perquè el client a enviat un tancament de comunicacions.");
                break;
            }
            case CHAT: {
                System.out.println("=== REBUT UN XAT ===");
                System.out.println("Client: " + messageBody);
                break;
            }
            case PKEY: {
                //LLegim la long. de la clau publica que ens envien.
                int length = Integer.parseInt(messageBody);
                byte[] publicKeyClientEnBytes = new byte[length];
                System.out.println("Tamany de la clau publica del server: " + length);

                //LLegim la clau publica que ens envien en byte (i no en String).
                try {
                    DataInputStream dIn = new DataInputStream(clientSocket.getInputStream());
                    dIn.readFully(publicKeyClientEnBytes, 0, length);
                    clientPublicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKeyClientEnBytes));
                } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
                    e.printStackTrace();
                }
                System.out.println("Clau rebuda.");
                break;
            }
        }

        if (!messageType.equals(CLOSE)) {
            Scanner sc = new Scanner(System.in);
            opcioCorrecta = false;
            do {
                System.out.println("---------------- SERVER ----------------");
                System.out.println("0. Desconnectar-se del CLIENT");
                System.out.println();
                System.out.println("   ENCRIPTACIÓ ASIMÈTRICA (RSA amb clau embolcallada)");
                System.out.println("1. Generar clau simètrica i público-privades");
                System.out.println("2. Enviar clau pública al CLIENT");
                System.out.println("3. Encriptar missatge amb RSA amb clau embolcallada");
                System.out.println("4. Enviar el missatge encriptat al CLIENT");
                System.out.println();
                System.out.println("    SENSE ENCRIPTACIÓ");
                System.out.println("11. Enviar un missatge al CLIENT (chat)");
                System.out.println("12. Retornar el control de les comunicacions al CLIENT");
                System.out.println("15. Enviar un missatge encriptat al CLIENT");
                System.out.println();
                System.out.println("21. Enviar un fitxer al CLIENT");
                System.out.println();
                System.out.println("50. Tancar el programa (equival al menú 0)");
                System.out.println();
                System.out.print("opció?: ");

                opcio = sc.next();

                switch (opcio) {
                    case "0": {
                        messageBody = CLOSE + SEPARATOR + "El server tanca comunicació.";
                        opcioCorrecta = true;
                        break;
                    }
                    case "1": {
                        Crypto.guardarSecretKey(Crypto.generadorDeClausSimetriques(128));
                        Crypto.guardarKeyPair(Crypto.generadorDeClausAsimetriques(512));
                        System.out.println("Claus generades.");
                        opcioCorrecta = false;
                        break;
                    }
                    case "2": {
                        // Enviem la long. de la clau publica.
                        String keyLong = Integer.toString(Crypto.keyPair.getPublic().getEncoded().length);
                        System.out.println("Tamany de la clau pública: " + keyLong);
                        messageBody = PKEY + SEPARATOR + keyLong;
                        clientOUT.println(messageBody);
                        clientOUT.flush();

                        // Enviem la clau publica en bytes (i no en String).
                        DataOutputStream dataOutputStream;
                        try {
                            dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
                            dataOutputStream.write(Crypto.keyPair.getPublic().getEncoded());
                            dataOutputStream.flush();
                            System.out.println("Enviada la clau publica al server.");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        messageBody = "!";
                        opcioCorrecta = true;
                        break;
                    }
                    case "11": {
                        Scanner sc2 = new Scanner(System.in);
                        System.out.print("SERVER.procesarMissatgeDelClient(): quin missatge vols enviar al server?: ");
                        String message = sc2.nextLine();
                        messageBody = CHAT + SEPARATOR + message;
                        opcioCorrecta = true;
                        break;
                    }
                    case "12": {
                        messageBody = RCTRL + SEPARATOR + "El server retorna el control de les comunicacions al client.";
                        opcioCorrecta = true;
                        break;
                    }
                    case "50": {
                        messageBody = CLOSE + SEPARATOR + "El server tanca la comunicació.";
                        opcioCorrecta = true;
                        break;
                    }
                    default:
                        System.out.println("COMANDA NO RECONEGUDA");
                }
            } while (opcioCorrecta != true);

            if (!messageBody.equals("!")) {
                st = new StringTokenizer(messageBody, SEPARATOR);
                messageType = st.nextToken();
            }

        } else {
            messageType = CLOSE;
            messageBody = CLOSE + SEPARATOR + "El server tanca la comunicació perquè el client a enviat un tancament de comunicacions.";
        }

        messageToClient[0] = messageType;
        messageToClient[1] = messageBody;
        System.out.println("============================="+messageBody);

        return messageToClient;
    }

    private static void closeClient(Socket clientSocket) {
        //Si falla el tancament no podem fer gaire cosa, només enregistrar el problema.

        //Tancament de tots els recursos.
        if((clientSocket != null) && (!clientSocket.isClosed())){
            try {
                if(!clientSocket.isInputShutdown()){
                    clientSocket.shutdownInput();
                }
                if(!clientSocket.isOutputShutdown()){
                    clientSocket.shutdownOutput();
                }
                clientSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(SPU_9_server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName() + " - INICI");
        listen();
        System.out.println(Thread.currentThread().getName() + " - FI");
    }
}