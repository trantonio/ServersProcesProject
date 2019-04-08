/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Aitor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author gmartinez
 */
public class Email_Dades {
    String origen;                  //És el nom de l'usuari que envia l'email .
    String desti;                   //És el nom de l'usuari que rebrà l'email
    Calendar dataEnviament;         //Quan es va generar i enviar el missatge.
    Calendar dataLectura;           //Quan es va baixar el missatge (llegir) pel destinatari.
    String dada;                    //El contingut de l'email.
    String nomFitxer;               //Nom del fitxer que es pot adjuntar a l'email.

    
    public Email_Dades(String origen, String desti, Calendar dataEnviament, Calendar dataLectura, String dada, String nomFitxer) {
        this.origen = origen;
        this.desti = desti;
        this.dataEnviament = dataEnviament;
        this.dataLectura = dataLectura;
        this.dada = dada;
        this.nomFitxer = nomFitxer;
    }

    
    public String getDesti() {
        return desti;
    }

    
    public String getNomFitxer() {
        return nomFitxer;
    }
    

    public void setDataLectura(Calendar dataLectura) {
        this.dataLectura = dataLectura;
    }

    
    @Override
    public String toString() {
        DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        
        return "Email_Dades {"  + "\n" + 
                "    Origen: " + origen + "\n" + 
                "    Desti: " + desti +  "\n" + 
                "    DataEnviament: " + sdf.format(dataEnviament.getTime())  +  "\n" + 
                "    DataLectura: " + sdf.format(dataLectura.getTime())  +  "\n" + 
                "    Cos del missatge: " + dada +  "\n" + 
                "    Nom del fitxer adjunt: " + nomFitxer +  "\n" + 
                "}" + "\n";
    }
    
    
    
}
