/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers.UF2NF2.Ex2;

/**
 *
 * @author gmartinez
 */
public class Bank implements Runnable {
    private Compte compte;
    int movimentDinners;
    static long filOperacio1ID;
    
    
    public Bank(Compte compteTmp, int movimentDinners) {
        compte = compteTmp;
        this.movimentDinners = movimentDinners;
    }

    
    public void run(){
        System.out.println("    " + Thread.currentThread().getName() + ".INICI");
        System.out.println("    " + Thread.currentThread().getName() + ".movimentDinners = "  + movimentDinners);
        System.out.println("    " + Thread.currentThread().getName() + ".compte.comprovarSaldo() = "  + compte.comprovarSaldo());
        System.out.println("    " + Thread.currentThread().getName() + ".REALITZO L'OPERACIÓ");
        
        if (movimentDinners > 0) {
            compte.ingressarDinners(movimentDinners);
        } else {
            compte.treureDinners(movimentDinners);
        }
        
        System.out.println("    " + Thread.currentThread().getName() + ".compte.comprovarSaldo() = "  + compte.comprovarSaldo());
        System.out.println("    " + Thread.currentThread().getName() + ".FI");
    }


    public static void main(String[] args) throws InterruptedException {
        //Compte operacio1 = new Compte();
        Compte compteTmp = new Compte();

        Bank objOperacio1 = new Bank(compteTmp, 100);
        Bank objOperacio2 = new Bank(compteTmp, 200);
        Bank objOperacio3 = new Bank(compteTmp, -30);
        Bank objOperacio4 = new Bank(compteTmp, 1000);
        Bank objOperacio5 = new Bank(compteTmp, -50);
        
        Thread filOperacio1 = new Thread(objOperacio1);
        filOperacio1.setName("operació 1");
        filOperacio1ID = filOperacio1.getId();
        Thread filOperacio2 = new Thread(objOperacio2);
        filOperacio2.setName("operació 2");
        Thread filOperacio3 = new Thread(objOperacio3);
        filOperacio3.setName("operació 3");
        Thread filOperacio4 = new Thread(objOperacio4);
        filOperacio4.setName("operació 4");
        Thread filOperacio5 = new Thread(objOperacio5);
        filOperacio5.setName("operació 5");
        
        filOperacio1.start();
        filOperacio2.start();
        filOperacio3.start();
        filOperacio4.start();
        filOperacio5.start();

        // El fil principal espera a que acabin els fills abans de continuar.
        filOperacio1.join(); 
        filOperacio2.join(); 
        filOperacio3.join(); 
        filOperacio4.join(); 
        filOperacio5.join(); 

        System.out.println("Saldo total (desde el main) = " + compteTmp.comprovarSaldo());
        System.out.println("Final Fil Principal");
    }  
    
}
