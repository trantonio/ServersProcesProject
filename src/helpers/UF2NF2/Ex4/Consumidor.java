/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers.UF2NF2.Ex4;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gmartinez
 */
public class Consumidor extends Thread {
    int id;                 //Ara mateix no té cap utilitat aquest atribut.
    Caixes recursos;

    
    
    public Consumidor(int id, Caixes recursos) {
        this.id = id;
        this.recursos = recursos;
    }
    
    
    public void run(){
        int numCaixaAdquirida;
        String name = Thread.currentThread().getName().toUpperCase();
        
        
        System.out.println(name + ": inicialitzat.");
        
        numCaixaAdquirida = recursos.adquirirCaixa();

        System.out.println(name + ": adquirida la caixa " + numCaixaAdquirida + " + sleep(7000)");

        // Fem un sleep() perquè es vegi que la resta de consumidors no poden agafar cap caixa i es queden bloquejats.
        try {
            Thread.currentThread().sleep(7000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Consumidor.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Si camuflem la següent linia, el semàfor no canvia i el recurs no queda alliberat.
        recursos.alliberarCaixa(numCaixaAdquirida);

        System.out.println(name + ": alliberat la caixa " + numCaixaAdquirida);
        
    }
    
}
