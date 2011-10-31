package com.heroku.util;

import org.apache.commons.codec.binary.Base64;
import sun.security.rsa.RSAKeyPairGenerator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPublicKey;

/**
 * TODO: Javadoc
 *
 * @author James Ward
 */
public class OpenSSHKeyUtil {

    public static RSAPublicKey generateRSAPublicKey() {
        RSAKeyPairGenerator keyGen = new RSAKeyPairGenerator();
        keyGen.initialize(1024, new SecureRandom());
        KeyPair keyPair = keyGen.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        RSAPublicKey publicKey = (RSAPublicKey)keyPair.getPublic();
        return publicKey;
    }

    public static String encodeOpenSSHPublicKeyString(RSAPublicKey publicKey) throws IOException {

        byte[] openSSHPublicKeyBytes = encodeOpenSSHPublicKeyBytes(publicKey);

        String openSSHPublicKeyString = "ssh-rsa " + new String(Base64.encodeBase64(openSSHPublicKeyBytes));

        return openSSHPublicKeyString;
    }

    // below code from: http://stackoverflow.com/questions/3588120/given-a-java-ssh-rsa-publickey-how-can-i-build-an-ssh2-public-key

    public static byte[] encodeOpenSSHPublicKeyBytes(RSAPublicKey key) throws IOException {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        byte[] name = "ssh-rsa".getBytes("US-ASCII");
        write(name, buf);
        write(key.getPublicExponent().toByteArray(), buf);
        write(key.getModulus().toByteArray(), buf);
        return buf.toByteArray();
    }

    private static void write(byte[] str, OutputStream os) throws IOException {
        for (int shift = 24; shift >= 0; shift -= 8)
            os.write((str.length >>> shift) & 0xFF);
        os.write(str);
    }

}
