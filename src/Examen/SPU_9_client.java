/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Examen;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gmartinez
 */
public class SPU_9_client {
    String clientNom ;
    
    static final String ADDRESS = "127.0.0.1";
    static final int PORT = 9090;
    static final String separador = "$##$";
    
    

    
    
    private static String[] procesarMissatgeDelServidor(String missatgeDelServer, BufferedReader entradaPelSocket) throws IOException {
        StringTokenizer st;
        String tipusMissatge = "";

        
		// SI REBEM "tipusMissatge = FITXEREMAILADJUNTATDEMANAT", HEM DE REBRE LES LÍNIES DEL FITXER I TREURE-LES PER PANTALLA.


		// MENÚ 22:
		// SI VOLEM ENVIAR UN EMAIL HEM D'ENVIAR "tipusMissatge = EMAIL". EL FORMAT SERÀ: 
		//			"EMAIL" + separador + origen + separador + destinatari + separador + missatge + separador + nom del fitxer adjuntat
		// PER TANT HEM DE DEMANAR EL NOM DEL ORIGEN, EL DEL DESTINATARI, EL TEXT DEL MISSATGE I EL NOM DEL FITXER QUE VOLEM ADJUNTAR (EN
		// EL CAS QUE VULGUEM ADJUNTAR UN FITXER).
		// SI NO VOLEM ADJUNTAR UN FITXER A L'EMAIL, EL FORMAT SERÀ:
		//			"EMAIL" + separador + origen + separador + destinatari + separador + missatge + separador
		// SE SUPOSA QUE ELS FITXERS QUE S'ADJUNTEN ESTARAN EN LA CARPETA fitxersClients.
		// QUAN TINGUEM LES DADES, CRIDAREM A LA FUNCIÓ enviarEmail() PER A ENVIAR L'EMAIL I EL FITXER ADJUNTAT SI N'HI HAGUÉS CAP.


		// MENÚ 23:
		// DEMANARÀ EL NOM DE L'USUARI DEL QUAL VOLEM VEURE ELS EMAILS QUE HA REBUT (PER TANT SERÀ EL "destinatari" EN ELS EMAILS) I
		// ELS VISUALITZARÀ. PER A FER AIXÒ CRIDAREM A LA FUNCIÓ "Email.consultarEmails()".
		// DESPRÉS TINDRÀ L'OPCIÓ DE VEURE EL CONTINGUT DEL FITXER ADJUNTAT A UN DELS EMAILS. "Email.consultarEmails()" RETORNARÀ EL
		// NOM DEL FITXER ADJUNTAT A L'EMAIL QUE S'HAGI ESCOLLIT (SI L'USUARI VOL).
		// SI ES VOLGUÉS VEURE EL CONTINGUT D'UN FITXER ADJUNTAT, A LLAVORS S'HAURÀ D'ENVIAR UN MISSATGE AL SERVER DEMANANT EL FITXER
		// AMB "tipusMissatge = CLIENT_DEMANA_FITXER_EMAIL". EL SERVER RESPONDRÀ AMB "tipusMissatge = FITXEREMAILADJUNTATDEMANAT" I 
		// A LLAVORS REBREM LES LÍNIES DEL FITXER I LES TREUREM PER PANTALLA.

        
        System.out.println("CLIENT.procesarMissatgeDelServidor(): rebut del server el missatge = '" + missatgeDelServer + "'.");
        st = new StringTokenizer(missatgeDelServer, separador);
        tipusMissatge = st.nextToken();			//CADA VEGADA QUE FEM "st.nextToken()" AGAFEM EL SEGÜENT ELEMENT SEPARAT PER "separador".
        System.out.println("CLIENT.procesarMissatgeDelServidor(): tipusMissatge rebut = '" + tipusMissatge + "'."); 
       
        
        
        if (!tipusMissatge.equals("TANCARCONNEXIO")){
            Scanner sc = new Scanner(System.in);
            sortirDelMenu = false;
            
            do {
                System.out.println("---------------- CLIENT ----------------");
                System.out.println("0. Desconnectar-se del SERVER");
                System.out.println();
                System.out.println("    EMAIL");
                System.out.println("22. Enviar un email a un client (es pot adjuntar un fitxer)");
                System.out.println("23. Veure els emails rebuts");
                System.out.println();
                System.out.println("50. Tancar el programa (equival al menú 0)");
                System.out.println();
                System.out.print("opció?: ");

                opcio = sc.next();

                switch (opcio) {
                    case "0":
                        resposta = "TANCARCONNEXIO" + separador + "El client tanca la comunicació.";
                        sortirDelMenu = true;
                    case "22":

                        break; 
                    case "23":

                        break;                         
                    case "50":
                        resposta = "TANCARCONNEXIO" + separador + "El client tanca la comunicació.";
                        sortirDelMenu = true;
                        break; 
                    default:
                        System.out.println("COMANDA NO RECONEGUDA");
                }   
            } while (sortirDelMenu != true);

            st = new StringTokenizer(resposta, separador);
            tipusMissatge = st.nextToken();
            
        } else {
            resposta = "TANCARCONNEXIO" + separador + "El client tanca la comunicació perquè el server a enviat un tancament de comunicacions.";
            tipusMissatge = "TANCARCONNEXIO";
        }
        
        
        dadesAEnviarAlServer[0] = tipusMissatge;
        dadesAEnviarAlServer[1] = resposta;
        
        return(dadesAEnviarAlServer);
    }
    
    
    
    private static void tancarSocket(Socket clientSocket) {

    }  
    
    
    
    private static void enviarEmail(String[] dadesAEnviar, PrintWriter sortidaCapAlSocket, Socket socket) throws IOException {

    }
    

    
    public static void connectar(String address, int port) {
       
    }    



    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println("CLIENT - INICI");
        connectar(ADDRESS, PORT);
        System.out.println("CLIENT - FI");
        
    }
    
}
