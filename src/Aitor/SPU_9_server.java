/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Aitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Predicate;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 *
 * @author iam8799345
 */
public class SPU_9_server {

    static final int PORT = 9090;
    private static boolean end = false;
    static int keyPairLength = 512;
    static int secretKeyLength = 128;
    public static KeyPair keyPair;
    public static SecretKey keySimetric;
    static String[] tipus = {"TANCARCONNEXIO", "CHAT", "RETORNCTRL", "CLAUPUBLICA", "MISSATGEENCRIPTAT", "MISSATGEENCRIPTATFI"};

    HashMap<Integer, Email_Dades> emails = new HashMap<>();

    public void listen() throws NoSuchAlgorithmException, InvalidKeySpecException {
        Socket clientSocket = null;
        try (ServerSocket serverSocket = new ServerSocket(PORT);) {
            //Es crea un ServerSocket que atendrà el port nº PORT a l'espera de
            //clients que demanin comunicar-se.
            while (!end) {
                //El mètode accept resta a l'espera d'una petició i en el moment de
                //produir-se crea una instància específica de sòcol per suportar
                //la comunicació amb el client acceptat.
                clientSocket = serverSocket.accept();

                //Processem la petició del client.
                proccesClientRequest(clientSocket);

                //Tanquem el sòcol temporal per atendre el client.
                closeClient(clientSocket);
            }
            //Tanquem el sòcol principal
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(SPU_9_server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    static BufferedReader in = null;
    static PrintStream outToClient = null;


    public void proccesClientRequest(Socket clientSocket) throws NoSuchAlgorithmException, InvalidKeySpecException {
        boolean farewellMessage = false;
        String clientMessage = "";

        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outToClient = new PrintStream(clientSocket.getOutputStream());

            do {
                //Processem el missatge del client i generem la resposta. Si
                //clientMessage és buida generarem el missatge de benvinguda.
                String dataToSend = processData(clientMessage);

                if (!dataToSend.equalsIgnoreCase("")) {
                    outToClient.println(dataToSend);
                    outToClient.flush();
                }

                if (isFarewellMessage(dataToSend)) {
                    closeClient(clientSocket);
                    break;
                }

                clientMessage = in.readLine();
                farewellMessage = isFarewellMessage(clientMessage);
            } while ((clientMessage) != null && !farewellMessage);

        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    private void closeClient(Socket clientSocket) {
        //Si falla el tancament no podem fer gaire cosa, només enregistrar el problema.
        try {
            //Tancament de tots els recursos.
            if (clientSocket != null && !clientSocket.isClosed()) {
                if (!clientSocket.isInputShutdown()) {
                    clientSocket.shutdownInput();
                }
                if (!clientSocket.isOutputShutdown()) {
                    clientSocket.shutdownOutput();
                }
                clientSocket.close();
            }
        } catch (IOException ex) {
            //Enregistrem l’error amb un objecte Logger.
            Logger.getLogger(SPU_9_server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean isFarewellMessage(String clientMessage) {
        return clientMessage.contains(tipus[0]);
    }
    int contador = 0;


    private String processData(String clientMessage) {
        System.out.println("Cliente ha dicho: " + clientMessage);
        if (contador == 0) {
            contador++;
            return "Bienvenido Cliente";
        }
        if (clientMessage.contains("CLAUPUBLICA")) {
            recibirPublicKey(clientMessage);
        }

        if (clientMessage.contains(tipus[4])) {
            System.out.println("Descifrando mensaje...");
            desEncrypt();
        }

        if (clientMessage.startsWith("EMAIL$##$")) {
            System.out.println("Recibiendo email...");
            System.out.println(clientMessage);
            recibiendoEmail(clientMessage);
            return "";
        }

        if (clientMessage.startsWith("CONSULTAREMAILINICIO")) {
            System.out.println("Consultando emails...");
            consultarEmail(clientMessage);
            return "";
        }

        if (clientMessage.startsWith("NUMEROEMAIL")) {
            System.out.println("Enviando fichero...");
            enviandoFichero(clientMessage);
            return "";
        }

        return "";
    }
    PublicKey clauPublica;
    static String separador = "$##$";
    Scanner sc = new Scanner(System.in);

    public String menu() {

        boolean exit = false;
        Scanner scanner = new Scanner(System.in);

        do {
            System.out.println("0. Desconnectar-se del CLIENT / SERVER\n"
                    + "1. Enviar un missatge al CLIENT / SERVER\n"
                    + "2. Retornar el control de les comunicacions al CLIENT / SERVER\n"
                    + "11. Generar clau simètrica i público-privades\n"
                    + "12. Enviar clau pública al CLIENT / SERVER\n"
                    + "13. Encriptar missatge amb RSA amb clau embolcallada\n"
                    + "14. Enviar el missatge encriptat al CLIENT / SERVER");
            switch (scanner.nextInt()) {
                case 0:
                    String tancar = tipus[0] + separador + "El server tanca la comunicació.";
                    return tancar;
                case 1:
                    String missatge = tipus[1] + separador + sc.nextLine();
                    System.out.println(missatge);
                    return missatge;
                case 2:
                    String control = tipus[2] + separador + "El Servidor retorna el control de les comunicacions al Client.";
                    System.out.println(control);
                    return control;
                case 11:
                    keyPair = Encrypt.generadorDeClausAsimetriques();
                    keySimetric = Encrypt.generadorDeClausSimetriques();
                    break;
                case 12:
                    sendThePublicKey();
                    return "";
                case 13:
                    textAndKey = Encrypt.EncryptAsimetric(sc.nextLine(), keySimetric, clauPublica);
                    break;
                case 14:
                    String mensajeAndKey = Encrypt.enviarMensajeYKey(textAndKey);
                    sendMessage(mensajeAndKey);
                    return "";
                default:
                    break;
            }

        } while (!exit);
        return "";
    }

    private void enviandoFichero(String clientMessage) {
        StringTokenizer st = new StringTokenizer(clientMessage, separador);
        st.nextToken();
        int emailNum = Integer.parseInt(st.nextToken());
        String nomFitxer = emails.get(emailNum).getNomFitxer();
        if (nomFitxer.equalsIgnoreCase("")) {
            outToClient.println("No hay archivo adjunto");
            outToClient.flush();
        } else {

            File tmp = new File(PATH + File.separator + nomFitxer);

            outToClient.println("Contenido del fitxer adjunt: " + nomFitxer);
            outToClient.flush();
            String cadena = "";
            try (BufferedReader br = new BufferedReader(new FileReader(tmp));) {
                while ((cadena = br.readLine()) != null) {
                    outToClient.println(cadena);
                    outToClient.flush();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            outToClient.println("FINARCHIVO");
            outToClient.flush();
        }

    }

    private void consultarEmail(String clientMessage) {
        StringTokenizer st = new StringTokenizer(clientMessage, separador);
        st.nextToken();
        String usuario = st.nextToken();
        emails = Email.consultarEmails(usuario);
        System.out.println(emails.size() + "*********************************");

        outToClient.println("Email.consultarEmails().inici");
        outToClient.flush();

        for (Map.Entry<Integer, Email_Dades> entry : emails.entrySet()) {
            outToClient.println(entry.getKey() + "\t" + entry.getValue());
            outToClient.flush();
        }
        outToClient.println("Email.consultarEmails().fi");
        outToClient.flush();

        outToClient.println("De quin email vols veure el fitxer?");
        outToClient.flush();

    }

    private void recibiendoEmail(String clientMessage) {
        StringBuilder sb = new StringBuilder();
        StringTokenizer st = new StringTokenizer(clientMessage, separador);
        st.nextToken();
        String origen = st.nextToken();
        String destino = st.nextToken();
        Calendar dataEnviament = Calendar.getInstance();
        String dada = st.nextToken();
        if (!st.hasMoreTokens()) {
            Email.insertarNouEmail(new Email_Dades(origen, destino, dataEnviament, dataEnviament, dada, ""));
        } else {
            String nomFitxer = st.nextToken();
            Email.insertarNouEmail(new Email_Dades(origen, destino, dataEnviament, dataEnviament, dada, nomFitxer));
            try {
                String trosMissatgeTmp = "";
                while (((trosMissatgeTmp = in.readLine()) != null) && (!trosMissatgeTmp.contains("FINEMAIL"))) {

                    sb.append(trosMissatgeTmp + "\n");
                }
                sb.deleteCharAt(sb.length() - 1);
            } catch (IOException ex) {
                Logger.getLogger(SPU_9_server.class.getName()).log(Level.SEVERE, null, ex);
            }
            try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(PATH + File.separator + "client.txt")));) {
                bw.write(sb.toString());
            } catch (IOException ex) {
                ex.printStackTrace();
                System.err.println(ex);
            }
            System.out.println(sb.toString());
        }
    }

    final String PATH = "fitxersServer";

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SPU_9_server s = new SPU_9_server();
        s.listen();
    }

    private void recibirPublicKey(String clientMessage) {
        StringTokenizer st = new StringTokenizer(clientMessage, separador);
        String tipusMissatge = st.nextToken();
        String fin = "";
        if (tipusMissatge.equals(tipus[3])) {
            try {
                StringBuilder sb = new StringBuilder();
                String trosMissatgeTmp = "";
                while (((trosMissatgeTmp = in.readLine()) != null) && (!trosMissatgeTmp.contains("CLAUPUBLICAFI"))) {
                    sb.append(trosMissatgeTmp + "\n");
                }
                //ARA HI HA UN SALT DE LINIA AL FINAL DE MÉS QUE HEM D'ELIMINAR PERQUE LA CLAU    // NO ACABA AMB SALT DE LINIA.
                fin = trosMissatgeTmp;
                sb.deleteCharAt(sb.length() - 1);    //Convertim la clau publica rebuda en tipus String a tipus PublicKey.
                String clauPublicaDelClientEnString = sb.toString();
                BASE64Decoder decoder = new BASE64Decoder();
                byte[] clauPublicaDelClientEnByte = decoder.decodeBuffer(clauPublicaDelClientEnString);
                clauPublica = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(clauPublicaDelClientEnByte));
            } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException ex) {
                Logger.getLogger(SPU_9_server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println(new BASE64Encoder().encode(clauPublica.getEncoded()));
        System.out.println(fin);
    }

    public void sendThePublicKey() {
        outToClient.println("CLAUPUBLICA" + separador);
        outToClient.flush();
        String trosMissatgeTmp;
        StringTokenizer tokenizer = new StringTokenizer(publicKeyString(), "\n");
        while (tokenizer.hasMoreElements()) {
            trosMissatgeTmp = tokenizer.nextToken("\n");
            outToClient.println(trosMissatgeTmp);
            outToClient.flush();
        }
        outToClient.println("CLAUPUBLICAFI" + separador);
        outToClient.flush();
    }

    private String publicKeyString() {
        String publicKey = new BASE64Encoder().encode(keyPair.getPublic().getEncoded());
        return publicKey;
    }

    byte[][] textAndKey = new byte[2][];

    private String desEncrypt() {
        StringBuilder sb = new StringBuilder();
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            String trosMissatgeTmp = "";
            while (((trosMissatgeTmp = in.readLine()) != null && (!trosMissatgeTmp.contains(tipus[5])))) {
                System.out.println(trosMissatgeTmp);
                sb.append(trosMissatgeTmp + "\n");
            }
            sb.deleteCharAt(sb.length() - 1);
        } catch (IOException ex) {
            Logger.getLogger(SPU_9_server.class.getName()).log(Level.SEVERE, null, ex);
        }

        String mensaje = "";
        String claveSimetrica = "";
        StringTokenizer tokenizer = new StringTokenizer(sb.toString(), separador);
        String tipusMissatge = tokenizer.nextToken();
        if (tipusMissatge.equals(tipus[4])) {

            mensaje = tokenizer.nextToken();
            System.out.println(mensaje);
            if (tokenizer.nextToken().equals("CLAUENCRIPTADA")) {
                claveSimetrica = tokenizer.nextToken();
                System.out.println(claveSimetrica);
            }
        }

        try {
            byte[] secretKey = decoder.decodeBuffer(claveSimetrica);
            byte[] mensajeEncrypted = decoder.decodeBuffer(mensaje);
            mensaje = Encrypt.desencriptarRSA(mensajeEncrypted, secretKey, keyPair);
        } catch (IOException ex) {
            System.err.println(ex);
        }
        System.out.println(mensaje);
        return mensaje;
    }

    private void sendMessage(String mensajeAndKey) {
        outToClient.println(tipus[4] + separador);
        outToClient.flush();
        String trosMissatgeTmp;
        StringTokenizer tokenizer = new StringTokenizer(mensajeAndKey, "\n");
        while (tokenizer.hasMoreElements()) {
            trosMissatgeTmp = tokenizer.nextToken("\n");
            outToClient.println(trosMissatgeTmp);
            outToClient.flush();
        }
        outToClient.println(tipus[5] + separador);
        outToClient.flush();
    }

}
