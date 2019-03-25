/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers.UF2NF2.Ex3.S1;

/**
 *
 * @author gines
 */
public class BorsaDeLLetres {
    private char[] llistaDeLLetres = {'0','0','0','0','0','0','0','0','0','0'};
    int posicio = -1;		//En posicio tenim el índex on hi ha l'últim element ficat.

    
    
    public BorsaDeLLetres() {
    }
    
    
    public synchronized int quantEspaiHiHa() {
        int posTmp;
        
       	posTmp = 9 - posicio;		//Posicions van de [0,9].
        
        return (posTmp);
    }
    
    
    public synchronized boolean hiHaCaracters() {
        if ((0 <= posicio) && (posicio <= 9)){
        	return true;
        } else {
        	return false;
        }
    }
    
    
    public synchronized void afegir(char caracter) {
        if ((-1 <= posicio) && (posicio <= 8)){		//De  [-1,8] --> [0,9].
            posicio++;
        	
            //System.out.println("BorsaDeLLetres.afegir(): NOTIFY(), posicio = " + posicio + ", caracter = " + caracter); 
            llistaDeLLetres[posicio] = caracter;
            //notify();
        }/* 
        else {
            try {
                System.out.println("BorsaDeLLetres.afegir(): WAIT(),  posicio = " + posicio);
                wait();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }*/
    }
    
    
    public synchronized char treure() {
        char caracter = '0';
        
        if ((0 <= posicio) && (posicio <= 9)){		//Entre [0,9] hi ha dades.
            caracter = llistaDeLLetres[posicio];
            posicio--;
            //System.out.println("BorsaDeLLetres.treure(): NOTIFY(), caracter = " + caracter);
            //notify();
        } /*else {
            try {
                System.out.println("BorsaDeLLetres.treure(): WAIT(),  posicio = " + posicio);
                wait();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }*/
        
        return caracter;
    }
    
}
