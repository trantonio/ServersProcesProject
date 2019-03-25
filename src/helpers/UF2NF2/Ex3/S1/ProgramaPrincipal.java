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
public class ProgramaPrincipal {
    public static void main(String args[]) {
        BorsaDeLLetres borsaDeLLetres = new BorsaDeLLetres();
        ProductorDeLLetres productorDeLLetres = new ProductorDeLLetres(borsaDeLLetres);
        ConsumidorDeLLetres consumidorDeLLetres = new ConsumidorDeLLetres(borsaDeLLetres);

        
        System.out.println("0000 - ProgramaPrincipal.INICI");
        productorDeLLetres.start();
        consumidorDeLLetres.start();
        
        try {
            productorDeLLetres.join();
            consumidorDeLLetres.join();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("0000 - ProgramaPrincipal.FI");
        
    }
    
    
}
