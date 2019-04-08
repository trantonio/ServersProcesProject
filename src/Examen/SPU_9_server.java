package Examen;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.util.Calendar;
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
        
        
        
        
    }
    
    
    
    private static void enviarFitxerAlClient(String nomFitxerAEnviar, PrintWriter sortidaCapAlClientSocket) {
		//AQUÍ ESTARÀ TOT EL CODI QUE PERMET ENVIAR TOT EL CONTINGUT DEL FITXER, LÍNIA A LÍNIA.
		//S'HA D'ENVIAR EL "tipusMissatge = FITXEREMAILADJUNTATDEMANAT" ABANS D'ENVIAR LES LÍNIES DEL FITXER.

    }
    
    

    private static void procesarComunicacionsAmbClient(Socket clientSocket) {
        
    }
    
    
    
    private static void tancarClient(Socket clientSocket) {

    }    
    
    
    public static void escoltar(){
        
    }
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        System.out.println(LocalTime.now() + ": SERVER.main(): INICI");
        escoltar();
        System.out.println(LocalTime.now() + ": SERVER.main(): FI");
    }
    
}
