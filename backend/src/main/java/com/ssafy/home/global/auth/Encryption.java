package com.ssafy.home.global.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class Encryption {

    private static final int SALT_SIZE = 16;


    @Value("{jwt.secretKey:test}")
    private String secretKey;

    public String Hashing(byte[] password, String Salt) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        for (int i = 0 ; i < 7120; i++){
            String temp = Byte_to_String(password) + Salt;
            md.update(temp.getBytes());
            password = md.digest();
        }

        return Byte_to_String(password);
    }

    public String getSalt(){
        SecureRandom rnd = new SecureRandom();
        byte[] temp = new byte[SALT_SIZE];
        rnd.nextBytes(temp);

        return Byte_to_String(temp);
    }

    private String Byte_to_String(byte[] temp){
        StringBuilder sb = new StringBuilder();
        for(byte a : temp){
            sb.append(String.format("%02x",a));
        }
        return sb.toString();
    }

    public String encrypt(String value, String salt) {
        try {
            salt = salt.substring(0,16);
            IvParameterSpec iv = new IvParameterSpec(salt.getBytes(StandardCharsets.UTF_8));

            SecretKeySpec secretKeySpec = new SecretKeySpec(this.secretKey.getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String decrypt(String encrypted, String salt) {
        try {
            salt = salt.substring(0,16);
            IvParameterSpec iv = new IvParameterSpec(salt.getBytes(StandardCharsets.UTF_8));

            SecretKeySpec secretKeySpec = new SecretKeySpec(this.secretKey.getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, iv);

            byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));

            return new String(original);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}