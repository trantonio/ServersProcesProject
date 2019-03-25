/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers.UF3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyPair;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKey;

/**
 *
 * @author gmartinez
 */
public class SPU_9_client {
    String clientNom ;
    
    static final String ADDRESS = "127.0.0.1";
    static final int PORT = 9090;
    static final String separador = "$##$";
    //private boolean fiComunicacio = false;
    
    static SecretKey clauSecretaSimetrica = null;
    static KeyPair clauPublicaIPrivada = null;
    
    

    public SPU_9_client(String clientNom) {
        this.clientNom = clientNom;
    }
    
    
    
    private static String[] procesarMissatgeDelServidor(String missatgeDelServer, BufferedReader entradaPelSocket) {
        StringTokenizer st;
        StringTokenizer st2;
        String nomClau;
        String clauAESXifradaEnString;
        String tipusMissatge = "";
        String resposta = "-1";
        String opcio;
        boolean opcioCorrecta;
        String missatge = "";
        String missatgeEncriptatRebut = "";
        String clauAESEncriptadaRebuda = "";
        String clauPublicaEnString;
        
        
        //Encriptacio objEncriptacio = new Encriptacio();

        String[] dadesAEnviarAlServer = {"", ""};        
        
        System.out.println("CLIENT.procesarMissatgeDelServidor(): rebut del server el missatge: '" + missatgeDelServer + "'.");
        st = new StringTokenizer(missatgeDelServer, separador);
        tipusMissatge = st.nextToken();
        System.out.println("CLIENT.procesarMissatgeDelServidor(): tipusMissatge rebut= '" + tipusMissatge + "'."); 
       
        
        if (tipusMissatge.equals("MISSATGEENCRIPTAT")){

        }
        
        
        
        if (!tipusMissatge.equals("TANCARCONNEXIO")){
            Scanner sc = new Scanner(System.in);
            opcioCorrecta = false;
            do {
                System.out.println("---------------- CLIENT ----------------");
                System.out.println("0. Desconnectar-se del SERVER");
                System.out.println();
                System.out.println("   ENCRIPTACIÓ ASIMÈTRICA (RSA amb clau embolcallada)");
                System.out.println("1. Generar clau simètrica i público-privades");
                System.out.println("2. Enviar clau pública al SERVER");
                System.out.println("3. Encriptar missatge amb RSA amb clau embolcallada");
                System.out.println("4. Enviar el missatge encriptat al SERVER");
                System.out.println();
                System.out.println("11. Enviar un missatge al SERVER (chat)");
                System.out.println("12. Retornar el control de les comunicacions al SERVER");
                System.out.println("15. Enviar un missatge encriptat al SERVER");
                System.out.println();
                System.out.println("21. Enviar un fitxer al SERVER");
                System.out.println();
                System.out.println("50. Tancar el programa (equival al menú 0)");
                System.out.println();
                System.out.print("opció?: ");


                opcio = sc.next();

                switch (opcio) {
                    case "0":
                        resposta = "TANCARCONNEXIO" + separador + "El client tanca la comunicació.";
                        opcioCorrecta = true;
                    case "1":

                        //opcioCorrecta = true;
                        break;
                    case "2":

                        //opcioCorrecta = true;
                        break;    
                    case "11":
                        Scanner sc2 = new Scanner(System.in);
                        System.out.print("CLIENT.procesarMissatgeDelServidor(): quin missatge vols enviar al server?: ");
                        missatge = sc2.nextLine();
                        resposta = "CHAT" + separador + missatge;
                        opcioCorrecta = true;
                        break;
                    case "12":
                        resposta = "RETORNCTRL" + separador + "El client retorna el control de les comunicacions al server.";
                        opcioCorrecta = true;
                        break;
                    case "15":
                        
                        opcioCorrecta = true;
                        break;                    
                    case "21":
                        
                        //opcioCorrecta = true;
                        break; 
                    case "50":
                        resposta = "TANCARCONNEXIO" + separador + "El client tanca la comunicació.";
                        opcioCorrecta = true;
                        break; 
                    default:
                        System.out.println("COMANDA NO RECONEGUDA");
                }   
            } while (opcioCorrecta != true);

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
                Logger.getLogger(SPU_9_client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }  
    
    
    private static void enviarClauPublica(String[] dadesAEnviar, PrintWriter sortidaCapAlSocket, Socket socket) {
        //Enviem el codi CLAUPUBLICA.
        
        //Enviem la clau publica. Fem 1 enviamente de cada linia que conté.
        
        //Enviem el codi MISSATGEENCRIPTATFI.
        sortidaCapAlSocket.println("CLAUPUBLICAFI" + separador);
        sortidaCapAlSocket.flush();
    }
    
    
    public static void connectar(String address, int port) {
        String missatgeDelServer = "";
        String dadesAEnviar[] = {"PETICIOCONNEXIODENEGADA", ""};
        Socket socket;
        BufferedReader entradaPelSocket;
        PrintWriter sortidaCapAlSocket = null;
        SimpleDateFormat formatData = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dataResposta = "";
        String tipusMissatge;
            
            
        try {   
            socket = new Socket(InetAddress.getByName(address), port);
            entradaPelSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            sortidaCapAlSocket = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            
            //Enviament de la petició. Inicialitzant la comunicació amb el server.
            sortidaCapAlSocket.println("patata");    //Assegurem que acaba amb un final de línia.
            sortidaCapAlSocket.flush(); 
            
            //El client atén el port fins que decideix finalitzar.
            do {
                missatgeDelServer = entradaPelSocket.readLine();
                
                //Processament de les dades rebudes i obtenció d’una nova petició.
                dadesAEnviar = procesarMissatgeDelServidor(missatgeDelServer, entradaPelSocket);
                //System.out.println("    " + Thread.currentThread().getName() + ": enviant les dades: " + dadesAEnviar[1]);
                
                if (!dadesAEnviar[0].equals("CLAUPUBLICA")){
                    sortidaCapAlSocket.println(dadesAEnviar[1]);    //Assegurem que acaba amb un final de línia.
                    sortidaCapAlSocket.flush();                     //Assegurem que s’envia.
                } else {
                    enviarClauPublica(dadesAEnviar, sortidaCapAlSocket, socket);
                }
                
            } while (!dadesAEnviar[0].equals("TANCARCONNEXIO"));

            tancarSocket(socket);
        } catch (UnknownHostException ex) {
            Logger.getLogger(SPU_9_client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SPU_9_client.class.getName()).log(Level.SEVERE, null, ex);
        }
        
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
