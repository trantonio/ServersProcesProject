/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers.UF2NF2.Ex4;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gines
 */

// Si hi ha 5 recursos i els volem fer servir tots, a llavors el semàfor és de tamany 5 ([0-4]). 
// Segons anem agafant recursos fem semafor.acquire() lo qual incrementarà el semàfor (+ 1) fins que 
// arribi a 4, en aquest cas el següent semafor.acquire() bloqueijarà l'execució fins que algú fassi
// un semafor.release() i baixi el nº de semafor en 1.
// El semàfor es un contador i mentre no arribem al seu tope, tot anirà OK. Quan arribem al seu 
// tope, l'execució del programa es bloqueija en aquell punt (on fem el semafor.acquire()).

public class Caixes {

    private int quantitatDeCaixes;
    private Semaphore semafor;
    private boolean[] caixesUtilitzades;   //Indica si cadascun dels recursos s'està fent servir o no.

    

    public Caixes(int tamany) {
        quantitatDeCaixes = tamany;
        semafor = new Semaphore(tamany);            //Creem un semàfor per controlar l'accés als recursos.
        caixesUtilitzades = new boolean[tamany];    //Creem l'array indicador de l'ús dels recursos.
    }

    
    // Busquem una caixa lliure.
    // Si el mètode no fós sincronitzat, diversos consumidors (fins a 6) podrien arribar a aquest punt
    // a la vegada i sel's assignaria la mateixa caixa a totes ells.
    public synchronized int asignarCaixa() {
        int i = 0;
        while (i < quantitatDeCaixes) {
            if (caixesUtilitzades[i] == false) {
                caixesUtilitzades[i] = true;
                break;
            }
            i++;
        }

        return (i);
    }

    
    //Mètode per quan algú vol un recurs.
    //Si tots els recursos estan caixesUtilitzades, es bloqueijarà fins que algun d'ells sigui alliberat.
    public Integer adquirirCaixa() {
        String name = Thread.currentThread().getName().toUpperCase();
        
        try {
            System.out.println("Caixes.adquirirCaixa(): " + name + " abans del semafor.");
            
            semafor.acquire();      //Sol·licitem el semàfor. Podrem comprobar que només poden entrar fins a 6 consumidors.
            
            System.out.println("Caixes.adquirirCaixa(): " + name + " després del semafor.");
            
        } catch (InterruptedException ex) {
            Logger.getLogger(Caixes.class.getName()).log(Level.SEVERE, null, ex);
        }

        return (asignarCaixa());
    }

    
    //Mètode per quan algú acaba de fer servir un recurs i l'allibera.
    public synchronized void alliberarCaixa(Integer numCaixa) {
        caixesUtilitzades[numCaixa] = false;
        
        semafor.release();
    }

}
