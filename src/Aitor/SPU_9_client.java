/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Aitor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKey;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 *
 * @author iam8799345
 */
public class SPU_9_client {

    public static KeyPair keyPair;
    public static SecretKey keySimetric;
    PrintStream out;
    BufferedReader in;
    Socket socket;
    PublicKey clauPublica;

    public SPU_9_client() {
    }

    public void connect(String address, int port) {
        String serverData;
        String request;
        boolean continueConnected = true;

        try {
            socket = new Socket(InetAddress.getByName(address), port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintStream(socket.getOutputStream());

            //El client atén el port fins que decideix finalitzar.
            while (continueConnected) {
                serverData = in.readLine();
                continueConnected = mustFinish(serverData);
                if (!continueConnected) {
                    String tancar = tipus[0] + separador + "El server tanca la comunicació.";
                    System.out.println(tancar);
                    break;
                }

                //Processament de les dades rebudes i obtenció d’una nova petició.
                request = getRequest(serverData);

                //Enviament de la petició.
                if (!request.equalsIgnoreCase("")) {
                    out.println(request);
                    out.flush();
                }

                if (mustFinishByClient(request)) {
                    break;
                }
            }
            close(socket);
        } catch (UnknownHostException ex) {
            reportError("Error de connexió. No existeix el host", ex);
        } catch (IOException ex) {
            Logger.getLogger(SPU_9_client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void close(Socket socket) {
        //Si falla el tancament no podem fer gaire cosa, només enregistrar el problema.
        try {
            //Tancament de tots els recursos.
            if (socket != null && !socket.isClosed()) {
                if (!socket.isInputShutdown()) {
                    socket.shutdownInput();
                }
                if (!socket.isOutputShutdown()) {
                    socket.shutdownOutput();
                }
                socket.close();
            }
        } catch (IOException ex) {
            //Enregistrem l’error amb un objecte Logger.
            System.err.println("Error " + ex);
        }
    }

    private void reportError(String error_de_connexió_No_existeix_el_host, UnknownHostException ex) {
        System.err.println("Error no identificado");
    }

    private String getRequest(String serverData) {
        System.out.println("Servidor ha dicho: " + serverData);

        if (serverData.contains("CLAUPUBLICA")) {
            recibirPublicKey(serverData);
        }

        if (serverData.contains(tipus[4])) {
            System.out.println("Descifrando mensaje...");
            desEncrypt();
        }

        if (serverData.contains("Email.consultarEmails().inici")) {
            System.out.println("Leyendo emails...");
            recibiendoEmails();
        }

        return menu();
    }

    private boolean mustFinish(String request) {
        return !request.contains(tipus[0]);
    }

    private boolean mustFinishByClient(String request) {
        return request.contains(tipus[0]);
    }

    public static void main(String[] args) {

        SPU_9_client c = new SPU_9_client();
        c.connect("127.0.0.1", 9090); //YOU DIED
    }

    static String[] tipus = {"TANCARCONNEXIO", "CHAT", "RETORNCTRL", "CLAUPUBLICA", "MISSATGEENCRIPTAT", "MISSATGEENCRIPTATFI"};
    static String separador = "$##$";
    static Scanner sc = new Scanner(System.in);

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
                    + "14. Enviar el missatge encriptat al CLIENT / SERVER\n"
                    + "21. Enviar un email a un client (es pot adjuntar un fitxer)\n"
                    + "22. Veure els emails rebuts\n");
            switch (scanner.nextInt()) {
                case 0:
                    String tancar = tipus[0] + separador + "El server tanca la comunicació.";
                    System.out.println(tancar);
                    return tancar;
                case 1:
                    String missatge = tipus[1] + separador + sc.nextLine();
                    System.out.println(missatge);
                    return missatge;
                case 2:
                    String control = tipus[2] + separador + "El Client retorna el control de les comunicacions al Servidor.";
                    System.out.println(control);
                    return control;
                case 11:

                    break;
                case 12:
                    sendThePublicKey();
                    return "";
                case 13:

                    break;
                case 14:

                    return "";
                case 21:
                    enviarEmail();
                    break;
                case 22:
                    consultarEmails();
                    return "";
                default:

                    break;
            }

        } while (!exit);
        return "";
    }

    final String PATH = "fitxersClients";

    private void recibiendoEmails() {
        StringBuilder sb = new StringBuilder();
        try {
            String trosMissatgeTmp = "";
            while (((trosMissatgeTmp = in.readLine()) != null) && (!trosMissatgeTmp.contains("Email.consultarEmails().fi"))) {
                sb.append(trosMissatgeTmp + "\n");
            }
            sb.deleteCharAt(sb.length() - 1);
            System.out.println(sb.toString());
            sb.setLength(0);
            System.out.println(in.readLine());
            Scanner s = new Scanner(System.in);
            out.println("NUMEROEMAIL" + separador + s.nextInt());

            while (((trosMissatgeTmp = in.readLine()) != null) && (!trosMissatgeTmp.contains("FINARCHIVO"))) {
                if (trosMissatgeTmp.equalsIgnoreCase("No hay archivo adjunto")) {
                    System.out.println(trosMissatgeTmp);
                    break;
                }
                sb.append(trosMissatgeTmp + "\n");
            }
            System.out.println(sb.toString());

        } catch (IOException ex) {
            System.err.println(ex);
        }

    }

    private void consultarEmails() {
        Scanner s = new Scanner(System.in);
        System.out.print("Usuario: ");
        String nombre = s.nextLine();
        out.println("CONSULTAREMAILINICIO" + separador + nombre);
        out.flush();
    }

    private void enviarEmail() {
        Scanner s = new Scanner(System.in);
        System.out.print("Origen: ");
        String origen = s.nextLine();

        System.out.print("Destinatario: ");
        String destino = s.nextLine();

        System.out.print("Missatge a enviar: ");
        String mensaje = s.nextLine();

        System.out.print("Vols adjuntar un fitxer? (S/N): ");
        String adjuntar = s.nextLine();
        if (adjuntar.equalsIgnoreCase("s")) {
            String nomFitxer = s.nextLine();
            out.println("EMAIL" + separador + origen + separador + destino + separador + mensaje + separador + nomFitxer);
            out.flush();
            String cadena = "";
            try (BufferedReader br = new BufferedReader(new FileReader(new File(PATH + File.separator + "client.txt")));) {
                while ((cadena = br.readLine()) != null) {
                    out.println(cadena);
                    out.flush();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            out.println("FINEMAIL");
            out.flush();
        } else {
            out.println("EMAIL" + separador + origen + separador + destino + separador + mensaje + separador);
            out.flush();
        }

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
        out.println("CLAUPUBLICA" + separador);
        out.flush();
        String trosMissatgeTmp;
        StringTokenizer tokenizer = new StringTokenizer(publicKeyString(), "\n");
        while (tokenizer.hasMoreElements()) {
            trosMissatgeTmp = tokenizer.nextToken("\n");
            out.println(trosMissatgeTmp);
            out.flush();
        }
        out.println("CLAUPUBLICAFI" + separador);
        out.flush();
    }

    private String publicKeyString() {
        String publicKey = new BASE64Encoder().encode(keyPair.getPublic().getEncoded());
        return publicKey;
    }
    byte[][] textAndKey = new byte[2][];

    private void sendMessage(String mensajeAndKey) {
        out.println(tipus[4] + separador);
        out.flush();
        String trosMissatgeTmp;
        StringTokenizer tokenizer = new StringTokenizer(mensajeAndKey, "\n");
        while (tokenizer.hasMoreElements()) {
            trosMissatgeTmp = tokenizer.nextToken("\n");
            out.println(trosMissatgeTmp);
            out.flush();
        }
        out.println(tipus[5] + separador);
        out.flush();
    }

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
        } catch (IOException ex) {
            System.err.println(ex);
        }
        System.out.println(mensaje);
        return mensaje;
    }

}
