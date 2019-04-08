/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Examen;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Predicate;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author gmartinez
 */
public class Email {
    static void insertarNouEmail(Email_Dades nouEmail){
        EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
        config.common().objectClass(Calendar.class).callConstructor(true);
        ObjectContainer db = Db4oEmbedded.openFile(config, "baseDeDades/email.db4o");  
        //S'ha de fer una configuració especial perquè db4o pugui treballar amb Calendar.
        
        try {
            System.out.println(LocalTime.now() + ": Email.insertarNouEmail().inici"); 
            db.store(nouEmail);
            System.out.println(LocalTime.now() + ": Email.insertarNouEmail().fi"); 
        } finally {
            db.close();     //Fem el bloc try-finally per asegurar-nos el tancament de la DB.
        }
    }
    

    static String consultarEmails(String nomDestinatari) throws IOException {
        EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
        config.common().objectClass(Calendar.class).callConstructor(true);
        ObjectContainer db = Db4oEmbedded.openFile(config, "baseDeDades/email.db4o"); 
        
        Calendar dataLectura = Calendar.getInstance();
        HashMap mapaEmails = new HashMap();
        String nomFitxer;
        
        
        System.out.println(LocalTime.now() + ": Email.consultarEmails().inici"); 
        try { 
            Predicate p = new Predicate<Email_Dades>() { 
                @Override 
                public boolean match(Email_Dades c) { 
                    return c.getDesti().equalsIgnoreCase(nomDestinatari); 
                } 
            }; 
            
            List<Email_Dades> result = db.query(p); 
            
            if (!result.isEmpty()) {
                int i = 1;
                //Hem trobat en la BD emails per a aquest destinatari.
                for (Email_Dades dadaTmp : result) {
                    System.out.println(i + ": " + dadaTmp);
                    
                    dadaTmp.setDataLectura(dataLectura);
                    db.store(dadaTmp);
                    
                    mapaEmails.put(i, dadaTmp.getNomFitxer());
                    
                    i++;
                }
            } 
        } finally { 
            db.close(); 
        }   
        
        nomFitxer = "-1";
        do {
            Scanner sc = new Scanner(System.in);
            System.out.print("De quin email vols veure el fitxer adjunt? (0 = cap fitxer): ");
            int numEmail = Integer.parseInt(sc.nextLine());

            if (mapaEmails.containsKey(numEmail)){
                nomFitxer = mapaEmails.get(numEmail).toString() ;   
            } else {
                if (numEmail == 0) {
                    nomFitxer = "";
                } else {
                    System.out.print("No has sel·leccionat cap email amb fitxer adjunt.");
                }
            }
        } while (nomFitxer.equalsIgnoreCase("-1"));
        
        return nomFitxer;
    }
    
    
}
