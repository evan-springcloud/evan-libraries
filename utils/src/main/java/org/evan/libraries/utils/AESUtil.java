//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.evan.libraries.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * AES加密工具
 *
 * @author Evan.Shen
 * @since 1.0
 */
public class AESUtil {
    private static final String AES = "AES";
    private static final Logger LOGGER = LoggerFactory.getLogger(AESUtil.class);
    private static final int BIT_LENGTH = 128;

    /**
     * 加密
     *
     * @param data
     * @param secret
     * @return
     * @throws AESException
     */
    public static String encrypt(String data, String secret) throws AESException {
        Cipher cipher = getCipher(secret, 1);
        byte[] bytes;
        try {
            bytes = cipher.doFinal(data.getBytes());
        } catch (BadPaddingException | IllegalBlockSizeException ex) {
            throw new AESException(ex.getMessage(), ex);
        }

        return parseByte2HexStr(bytes);
    }

    /**
     * 解密
     *
     * @param data
     * @param secret
     * @return
     * @throws AESException
     */
    public static String decrypt(String data, String secret) throws AESException {
        Cipher cipher = getCipher(secret, 2);

        byte[] bytes;
        try {
            bytes = cipher.doFinal(parseHexStr2Byte(data));
        } catch (BadPaddingException | IllegalBlockSizeException | NumberFormatException ex) {
            throw new AESException(ex.getMessage(), ex);
        }

        return new String(bytes);
    }

    private static Cipher getCipher(String secret, int i) throws AESException {
        try {
            SecretKey secretKey = getSecretKey(secret);
            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(i, secretKey);
            return cipher;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException ex) {
            throw new AESException(ex.getMessage(), ex);
        }
    }

    private static SecretKey getSecretKey(String secret) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(secret.getBytes());
        keyGenerator.init(BIT_LENGTH, secureRandom);
        SecretKey secretKey = keyGenerator.generateKey();
        //SecretKey secretKey2 = new SecretKeySpec(secretKey.getEncoded(), AES);
        return secretKey;
    }

    private static String parseByte2HexStr(byte[] buf) {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < buf.length; ++i) {
            String hex = Integer.toHexString(buf[i] & 255);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }

            sb.append(hex.toUpperCase());
        }

        return sb.toString();
    }

    private static byte[] parseHexStr2Byte(String hexStr) throws NumberFormatException {
        if (hexStr.length() < 1) {
            return null;
        } else {
            byte[] result = new byte[hexStr.length() / 2];

            for (int i = 0; i < hexStr.length() / 2; ++i) {
                int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
                int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
                result[i] = (byte) (high * 16 + low);
            }

            return result;
        }
    }
}
