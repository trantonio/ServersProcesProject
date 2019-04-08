package Examen;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author gmartinez
 */
public class SPU_9_server {
    static final int PORT = 9090;
    static final String separador = "$##$";
    private static boolean fiComunicacio = false;
    static final String FS = File.separator;
    static final String PATH = "DATA"+FS+"in"+FS+"fitxersClients";
    static final String PATH_out = "DATA"+FS+"out";

    static String emails;

    static BufferedReader in = null;
    static PrintStream outToClient = null;
    
    private static String[] procesarMissatgeDelClient(String missatgeDelClient, BufferedReader entradaPelClientSocket, Socket clientSocket, PrintWriter sortidaCapAlClientSocket) throws IOException {
        String tipusMissatge = "";
        StringTokenizer st;

		// SI REBEM "tipusMissatge = EMAIL", HEM DE CREAR UN OBJECTE DE TIPUS "Email_Dades" I FER UN "Email.insertarNouEmail(nouEmail)" 
		// PER ENMAGATZEMAR L'EMAIL EN LA BASE DE DADES.
		// SI L'EMAIL TÉ ADJUNTAT UN FITXER, HEM DE REBRE LES LÍNIES DEL FITXER I GUARDAR-LES EN UN FITXER AMB EL MATEIX NOM EN LA 
		// CARPETA "fitxersServer".
		

		// SI REBEM "tipusMissatge = CLIENT_DEMANA_FITXER_EMAIL", HEM DE BUSCAR EN LA CARPETA "fitxersServer" EL FITXER QUE ENS DEMANEN
		// I ENVIAR-LI AL CLIENT FENT SERVIR LA FUNCIÓ "enviarFitxerAlClient()".


		// EN CAP MOMENT L'USUARI INTERACTUA AMB EL SERVER. EL SERVER NO TÉ CAP MENÚ, ÉS TOT AUTOMÀTIC.


        
        System.out.println(LocalTime.now() + ": SERVER.procesarMissatgeDelClient(): rebut del client el missatge = '" + missatgeDelClient + "'.");
        st = new StringTokenizer(missatgeDelClient, separador);
        tipusMissatge = st.nextToken();			//CADA VEGADA QUE FEM "st.nextToken()" AGAFEM EL SEGÜENT ELEMENT SEPARAT PER "separador".
        System.out.println(LocalTime.now() + ": SERVER.procesarMissatgeDelClient(): tipusMissatge rebut = '" + tipusMissatge + "'."); 
        System.out.println();


        return new String[0];
    }
    private static void consultarEmail(String clientMessage) throws IOException {
        StringTokenizer st = new StringTokenizer(clientMessage, separador);
        st.nextToken();
        String usuario = st.nextToken();
        emails = Email.consultarEmails(usuario);

        outToClient.println("Email.consultarEmails().inici");
        outToClient.flush();

        outToClient.println("Email.consultarEmails().fi");
        outToClient.flush();

        outToClient.println("De quin email vols veure el fitxer?");
        outToClient.flush();

    }

    private static void recibiendoEmail(String clientMessage) {
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
    private static void enviarFitxerAlClient(String nomFitxerAEnviar, PrintWriter sortidaCapAlClientSocket) {
		//AQUÍ ESTARÀ TOT EL CODI QUE PERMET ENVIAR TOT EL CONTINGUT DEL FITXER, LÍNIA A LÍNIA.
		//S'HA D'ENVIAR EL "tipusMissatge = FITXEREMAILADJUNTATDEMANAT" ABANS D'ENVIAR LES LÍNIES DEL FITXER.
        StringTokenizer st = new StringTokenizer(nomFitxerAEnviar, separador);
        st.nextToken();
        int emailNum = Integer.parseInt(st.nextToken());
        String nomFitxer = emails;
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

    private static boolean isFarewellMessage(String clientMessage) {
        return clientMessage.contains("TANCARCONNEXIO");
    }

    private static void procesarComunicacionsAmbClient(Socket clientSocket) {
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
                    tancarClient(clientSocket);
                    break;
                }

                clientMessage = in.readLine();
                farewellMessage = isFarewellMessage(clientMessage);
            } while ((clientMessage) != null && !farewellMessage);

        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
    
    
    
    private static void tancarClient(Socket clientSocket) {
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

    private static String processData(String clientMessage) throws IOException {
        System.out.println("Cliente ha dicho: " + clientMessage);

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
            enviarFitxerAlClient(clientMessage,new PrintWriter(PATH_out));
            return "";
        }

        return "";
    }
    public static void escoltar(){
        Socket clientSocket = null;
        try (ServerSocket serverSocket = new ServerSocket(PORT);) {
            //Es crea un ServerSocket que atendrà el port nº PORT a l'espera de
            //clients que demanin comunicar-se.
            while (!fiComunicacio) {
                //El mètode accept resta a l'espera d'una petició i en el moment de
                //produir-se crea una instància específica de sòcol per suportar
                //la comunicació amb el client acceptat.
                clientSocket = serverSocket.accept();

                //Processem la petició del client.
                procesarComunicacionsAmbClient(clientSocket);

                //Tanquem el sòcol temporal per atendre el client.
                tancarClient(clientSocket);
            }
            //Tanquem el sòcol principal
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(SPU_9_server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

    /**
     * @param args the command line arguments
     */
    //06022010
    public static void main(String[] args) {
        // TODO code application logic here
        
        System.out.println(LocalTime.now() + ": SERVER.main(): INICI");
        escoltar();
        System.out.println(LocalTime.now() + ": SERVER.main(): FI");
    }
    
}
