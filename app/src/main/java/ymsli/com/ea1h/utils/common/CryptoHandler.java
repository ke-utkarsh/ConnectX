package ymsli.com.ea1h.utils.common;

/*
 * Project Name : EA1H
 * @company YMSLI
 * @author  (VE00YM129)
 * @date    18/07/2020 11:45 PM
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * CryptoHandler : This handles encryption or decryption of data coming from
 * API or going to API. This prevent network interception data misuse.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import android.util.Base64;

import static ymsli.com.ea1h.utils.common.Constants.AES;
import static ymsli.com.ea1h.utils.common.Constants.NA;
import static ymsli.com.ea1h.utils.common.Constants.UTF8;

public class CryptoHandler {
    private static String SecretKey = "ccC2H19lDDbQDfakxcrtNMQdd0FloLGG";
    private static String IV = "ggGGHUiDD0Qjhuvv";
    private static String TRANSFORMATION = "AES/CBC/PKCS5PADDING";
    private static CryptoHandler instance = null;
    public static CryptoHandler getInstance() {
        if (instance == null) {
            instance = new CryptoHandler();
        }
        return instance;
    }

    /**
     * it encrypts encryption of data sent in paramters
     * @param message data to be encrpted
     * @return encrypted string
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws InvalidKeyException
     * @throws UnsupportedEncodingException
     * @throws InvalidAlgorithmParameterException
     */
    public static String encrypt(String message) throws NoSuchAlgorithmException,
            NoSuchPaddingException, IllegalBlockSizeException,
            BadPaddingException, InvalidKeyException,
            UnsupportedEncodingException, InvalidAlgorithmParameterException {
        byte[] srcBuff = message.getBytes(UTF8);
        //here using substring because AES takes only 16 or 24 or 32 byte of key
        SecretKeySpec skeySpec = new
                SecretKeySpec(SecretKey.substring(0,32).getBytes(), AES);
        IvParameterSpec ivSpec = new
                IvParameterSpec(IV.substring(0,16).getBytes());
        Cipher ecipher = Cipher.getInstance(TRANSFORMATION);
        ecipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivSpec);
        byte[] dstBuff = ecipher.doFinal(srcBuff);
        String base64 = Base64.encodeToString(dstBuff, Base64.DEFAULT);
        return base64.trim();
    }

    /**
     * this decrypts input data
     * @param encrypted encrypted data
     * @return plain text data
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws InvalidAlgorithmParameterException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws UnsupportedEncodingException
     */
    public static String decrypt(String encrypted) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException,
            BadPaddingException, UnsupportedEncodingException {
        try {
            SecretKeySpec skeySpec = new
                    SecretKeySpec(SecretKey.substring(0, 32).getBytes(), AES);
            IvParameterSpec ivSpec = new
                    IvParameterSpec(IV.substring(0, 16).getBytes());
            Cipher ecipher = Cipher.getInstance(TRANSFORMATION);
            ecipher.init(Cipher.DECRYPT_MODE, skeySpec, ivSpec);
            byte[] raw = Base64.decode(encrypted, Base64.DEFAULT);
            byte[] originalBytes = ecipher.doFinal(raw);
            String original = new String(originalBytes, UTF8);
            return original.trim();
        }
        catch (Exception ex){
            return NA;
        }
    }
}
