/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Aitor;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import sun.misc.BASE64Encoder;

/**
 *
 * @author ronny
 */
public class Encrypt {

    static int keyPairLength = 512;
    static int secretKeyLength = 128;

    static final String AES = "AES/CBC/PKCS5Padding";
    static final String AESECB = "AES/ECB/PKCS5Padding";
    static final String RSA = "RSA/ECB/PKCS1Padding";
    static final String separador = "$##$";

    static String[] tipus = {"TANCARCONNEXIO", "CHAT", "RETORNCTRL", "CLAUPUBLICA", "MISSATGEENCRIPTAT", "MISSATGEENCRIPTATFI"};
    public static final byte[] IV_PARAM = {0x00, 0x01, 0x02, 0x03,
        0x04, 0x05, 0x06, 0x07,
        0x08, 0x09, 0x0A, 0x0B,
        0x0C, 0x0D, 0x0E, 0x0F};

    public static SecretKey generadorDeClausSimetriques() {
        SecretKey k = null;
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128);
            k = kgen.generateKey();
        } catch (NoSuchAlgorithmException ex) {
            System.err.println("Generador no disponible.");
        }
        return k;
    }

    public static KeyPair generadorDeClausAsimetriques() {
        KeyPair key = null;
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(512);
            key = keyGen.genKeyPair();
        } catch (Exception ex) {
            System.err.println("Generador no disponible.");
            System.err.println(ex);
        }
        return key;
    }

    /**
     * encriptar String y clave simetrica
     *
     * @param data
     * @param k
     * @param publicKey
     * @return
     */
    public static byte[][] EncryptAsimetric(String data, SecretKey k, PublicKey publicKey) {
        System.out.println("Texto a Encryptar");
        System.out.println(data);
        byte[][] textAndKey = new byte[2][];
        try {
            Cipher cipher = Cipher.getInstance(AES);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(IV_PARAM);
            cipher.init(Cipher.ENCRYPT_MODE, k, ivParameterSpec);
            byte[] encMsg = cipher.doFinal(data.getBytes());

            cipher = Cipher.getInstance(RSA);

            cipher.init(Cipher.WRAP_MODE, publicKey);
            byte[] encKey = cipher.wrap(k);

            textAndKey[0] = encKey;
            textAndKey[1] = encMsg;
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println(ex);
        }
        System.out.println("\nTexto Encriptado\n");
        System.out.println(new BASE64Encoder().encode(textAndKey[1]));
        return textAndKey;
    }

    public static String enviarMensajeYKey(byte[][] textAndKey) {

        String textEncriptatEnString = new String(new BASE64Encoder().encode(textAndKey[1]));
        String clauAESEncriptadaEnString = new String(new BASE64Encoder().encode(textAndKey[0]));

        return tipus[4] + separador
                + textEncriptatEnString + separador
                + "CLAUENCRIPTADA" + separador + clauAESEncriptadaEnString;
    }

    public static String desencriptarRSA(byte[] missatgeEncriptatByte, byte[] simetricKeyEncriptadaByte, KeyPair keyPair) {
        String dadesDesencriptadesEnString = "";
        try {
            //Desencriptem la clau d'encriptació que s'ha fet servir amb RSA + la clau privada.
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.UNWRAP_MODE, keyPair.getPrivate());
            SecretKey clauAESDesencriptada = (SecretKey) cipher.unwrap(simetricKeyEncriptadaByte, "AES", Cipher.SECRET_KEY);
            //Una clau simètrica és una "SECRET_KEY".

            //Desencriptem les dades amb AES en mode CBC.
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(IV_PARAM);
            cipher.init(Cipher.DECRYPT_MODE, clauAESDesencriptada, iv);
            byte[] dadesDesencriptadesEnByte = cipher.doFinal(missatgeEncriptatByte);

            dadesDesencriptadesEnString = new String(dadesDesencriptadesEnByte);

        } catch (Exception ex) {
            System.err.println("ERROR: No es pot desencriptar " + ex);
        }
        return dadesDesencriptadesEnString;
    }
}
