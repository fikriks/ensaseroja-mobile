package com.algoritma.melani_AE6.helper;

import android.content.Context;
import android.util.Base64;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
public class Algo_AES {
    Context ctx;
    private SecretKey key;
    private final int KEY_SIZE = 128;
    private final int DATA_LENGTH = 128;
    private Cipher encryptCipher;

    public Algo_AES(Context ctx) {
        this.ctx = ctx;
    }

    public void init() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(KEY_SIZE, new SecureRandom());
        key = keyGenerator.generateKey();

        encryptCipher = Cipher.getInstance("AES/GCM/NoPadding");
        byte[] iv = new byte[12]; // GCM IV biasanya 12 byte
        new SecureRandom().nextBytes(iv);
        GCMParameterSpec spec = new GCMParameterSpec(DATA_LENGTH, iv);
        encryptCipher.init(Cipher.ENCRYPT_MODE, key, spec);
    }

    public String decrypt(String encrypted) throws Exception {
        byte[] dataInByte = decode(encrypted);
        Cipher decryptCipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(DATA_LENGTH, encryptCipher.getIV());
        decryptCipher.init(Cipher.DECRYPT_MODE, key, spec);
        byte[] decryptBytes = decryptCipher.doFinal(dataInByte);
        return new String(decryptBytes);
    }

    private byte[] decode(String data) {
        return Base64.decode(data, Base64.DEFAULT); // Menggunakan android.util.Base64
    }
}
