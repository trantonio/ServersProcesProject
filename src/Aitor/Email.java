/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Aitor;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Predicate;
import java.util.Calendar;
import java.util.HashMap;

/**
 *
 * @author iam8799345
 */
public class Email {

    public static void insertarNouEmail(Email_Dades email) {
        EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
        ObjectContainer db = Db4oEmbedded.openFile(config, "Email.db4o");

        db.store(email);
        db.close();
    }
    
     public static HashMap<Integer, Email_Dades> consultarEmails(String user) {
        EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
        ObjectContainer db = Db4oEmbedded.openFile(config, "Email.db4o");
        Predicate p = new Predicate<Email_Dades>() {
            @Override
            public boolean match(Email_Dades email) {
                //condition
                System.out.println(email.getDesti().equalsIgnoreCase(user));
                return email.getDesti().equalsIgnoreCase(user);
            }
        };

        ObjectSet<Email_Dades> result = db.query(p);
        HashMap<Integer, Email_Dades> emails = new HashMap<>();
        int contador = 1;
        for (Email_Dades selectEmail : result) {
            selectEmail.setDataLectura(Calendar.getInstance());
            db.store(selectEmail);
            emails.put(contador++, selectEmail);
        }

        db.close();
        return emails;
    }

}
