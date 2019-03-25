/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers.UF2NF2.Ex4;

/**
 *
 * @author gmartinez
 */

// Hi ha 20 consumidors però només 6 caixes per a pagar.
// Es llançan els 20 consumidors a l'hora però només 6 podran accedir al les caixes, la resta s'haurà d'esperar 
// a que els 6 primers consumidors vagin alliberant les caixes.

public class BotigaMain {
    public static void main(String[] args) {
        int tamany = 20; 
        Consumidor[] consumidors = new Consumidor[tamany];
        
        
        Caixes recursos = new Caixes(6);
        
        for (int i = 0; i < tamany; i++){
            consumidors[i] = new Consumidor(i, recursos);
            consumidors[i].setName("consumidor" + i);
            consumidors[i].start();
        }
    }
    
}
