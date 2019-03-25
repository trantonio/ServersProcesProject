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
public class ConsumidorDeLLetres extends Thread {
    private BorsaDeLLetres borsaDeLLetres;
    
    
    public ConsumidorDeLLetres(BorsaDeLLetres borsaDeLLetres) {
        this.borsaDeLLetres = borsaDeLLetres;
    }
    
    
    @Override
    public void run() {
    	System.out.println("2222 - ConsumidorDeLLetres.INICI");
    	
        for (int i = 0; i < 5; i++) {
            System.out.println("   2222.1111[" + i + "] - ConsumidorDeLLetres: hiHaCaracters() = " + borsaDeLLetres.hiHaCaracters());
            
            if (borsaDeLLetres.hiHaCaracters() == true) {
                System.out.println("   2222.2222[" + i + "] - ConsumidorDeLLetres: treure() = " + borsaDeLLetres.treure() + ", quantEspaiHiHa() = " + borsaDeLLetres.quantEspaiHiHa());
            }
            
        }
    }
    
}
