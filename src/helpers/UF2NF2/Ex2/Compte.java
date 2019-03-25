/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers.UF2NF2.Ex2;


import java.util.logging.Level;
import java.util.logging.Logger;

import static helpers.UF2NF2.Ex2.Bank.filOperacio1ID;

/**
 *
 * @author gmartinez
 */
public class Compte {
    int saldo = 0;

    
    
    public Compte() {
    }
    
    
    public synchronized void ingressarDinners(int euros) {
        if (Thread.currentThread().getId() == filOperacio1ID) {
            try {
                System.out.println("    " + Thread.currentThread().getName() + ".SLEEP");
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Compte.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
        
        this.saldo = this.saldo + euros;
    }
    
    
    public synchronized void treureDinners(int euros) {
        this.saldo = this.saldo + euros;    //És una suma perquè el nº que entra per paràmetre ya és negatiu.
    }
    
    
    public synchronized int comprovarSaldo() {
        return this.saldo;
    }
}
