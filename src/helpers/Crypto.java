package helpers;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class Crypto {
    private static final byte[] IV_PARAM = {0x00, 0x01, 0x02, 0x03,
            0x04, 0x05, 0x06, 0x07,
            0x08, 0x09, 0x0A, 0x0B,
            0x0C, 0x0D, 0x0E, 0x0F};

    public static SecretKey sKey;
    public static KeyPair keyPair;

    public static SecretKey generadorDeClausSimetriques(int clauTamany) {
        SecretKey sKey = null;
        if ((clauTamany == Info.SIZE_128) || (clauTamany == Info.SIZE_192) || (clauTamany == Info.SIZE_256)) {
            try {
                KeyGenerator kgen = KeyGenerator.getInstance("AES");
                kgen.init(clauTamany);
                sKey = kgen.generateKey();
            } catch (NoSuchAlgorithmException ex) {
                System.err.println("Generador de claus simetriques no disponible.");
                ex.printStackTrace();
            }
        }
        return sKey;
    }

    public static void guardarSecretKey(SecretKey x) {
        sKey = x;
    }

    public static KeyPair generadorDeClausAsimetriques(int clauTamany) {
        KeyPair keys = null;

        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(clauTamany);
            keys = keyGen.genKeyPair();
        } catch (Exception ex) {
            System.err.println("Generador de claus asimètriques no disponible.");
            ex.printStackTrace();
        }
        return keys;
    }

    public static void guardarKeyPair(KeyPair x) {
        keyPair = x;
    }
}

class Info {
    public static final int SIZE_128 = 128;
    public static final int SIZE_192 = 192;
    public static final int SIZE_256 = 256;
    public static final int SIZE_512 = 512;
    public static final int SIZE_1024 = 1024;
    public static final int SIZE_2048 = 2048;
    public static final byte[] IV_PARAM = {
            0x00, 0x04, 0x08, 0x0C,
            0x01, 0x05, 0x09, 0x0D,
            0x02, 0x06, 0x0A, 0x0E,
            0x03, 0x07, 0x0B, 0x0F};
}

class Utils {
    public static String bytesToString(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static byte[] stringToBytes(String string) {
        return string.getBytes();
    }

    public static void checkPath(File path) {
        if (!path.exists()) {
            if (path.mkdirs()) {
                System.out.println("PATH CREAT!");
            }
        }
    }
}