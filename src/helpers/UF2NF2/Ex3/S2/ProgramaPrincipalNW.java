/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers.UF2NF2.Ex3.S2;

/**
 *
 * @author gines
 */
public class ProgramaPrincipalNW {
    public static void main(String args[]) {
        BorsaDeLLetresNW borsaDeLLetres = new BorsaDeLLetresNW();
        ProductorDeLLetresNW productorDeLLetres = new ProductorDeLLetresNW(borsaDeLLetres);
        ConsumidorDeLLetresNW consumidorDeLLetres = new ConsumidorDeLLetresNW(borsaDeLLetres);

        
        System.out.println("0000 - ProgramaPrincipalNW.INICI");
        productorDeLLetres.start();
        consumidorDeLLetres.start();
        
        try {
            //El main() continuarà la seva execució 5 segons després d'inicialitzar productorDeLLetres i 
            //consumidorDeLLetres independentment que aquest hagin acabat la seva execució.
            //El productorDeLLetres es quedarà pillat en el WAIT() al fer afegir() perque intenta ficar 20
            //lletres quan només hi ha espai per a 15 (10 inicialment i les 5 que treu consumidorDeLLetres).
            
            productorDeLLetres.join(5000);      
            consumidorDeLLetres.join(5000);     
        } catch (InterruptedException e) {      
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("0000 - ProgramaPrincipalNW.FI");
        
    }
    
    
}
