//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.evan.libraries.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * AES加密工具
 * @author Evan.Shen
 * @since 1.0
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public class AESUtil {
    public static final String KEY_ALGORITHM = "AES";
    public static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
    private static final Logger LOGGER = LoggerFactory.getLogger(AESUtil.class);

    public AESUtil() {
    }

    private static Key toKey(byte[] key) {
        SecretKey secretKey = new SecretKeySpec(key, "AES");
        return secretKey;
    }

    public static SecretKey getKey(String strKey) throws AESException {
        try {
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(strKey.getBytes());
            generator.init(128, secureRandom);
            return generator.generateKey();
        } catch (Exception var3) {
            throw new AESException("初始化密钥出现异常," + var3.getMessage(), var3);
        }
    }

    public static String encrypt(String data, String password) throws AESException {
        byte[] key = initKey(password);
        Key k = toKey(key);
        Cipher cipher = null;

        try {
            cipher = Cipher.getInstance("AES");
        } catch (NoSuchPaddingException | NoSuchAlgorithmException var9) {
            throw new AESException(var9.getMessage(), var9);
        }

        try {
            cipher.init(1, k);
        } catch (InvalidKeyException var8) {
            throw new AESException(var8.getMessage(), var8);
        }

        Object var5 = null;

        byte[] bytes;
        try {
            bytes = cipher.doFinal(data.getBytes());
        } catch (BadPaddingException | IllegalBlockSizeException var7) {
            throw new AESException(var7.getMessage(), var7);
        }

        return parseByte2HexStr(bytes);
    }

    public static String decrypt(String data, String password) throws AESException {
        byte[] key = initKey(password);
        Key k = toKey(key);
        Cipher cipher = null;

        try {
            cipher = Cipher.getInstance("AES");
        } catch (NoSuchPaddingException | NoSuchAlgorithmException var9) {
            throw new AESException(var9.getMessage(), var9);
        }

        try {
            cipher.init(2, k);
        } catch (InvalidKeyException var8) {
            throw new AESException(var8.getMessage(), var8);
        }

        byte[] bytes;
        try {
            bytes = cipher.doFinal(parseHexStr2Byte(data));
        } catch (BadPaddingException | IllegalBlockSizeException var7) {
            throw new AESException(var7.getMessage(), var7);
        }

        return new String(bytes);
    }

    private static byte[] initKey(String password) throws AESException {
        KeyGenerator kg = null;

        try {
            kg = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException var5) {
            throw new AESException(var5.getMessage(), var5);
        }

        SecureRandom secureRandom = null;

        try {
            secureRandom = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException var4) {
            throw new AESException(var4.getMessage(), var4);
        }

        secureRandom.setSeed(password.getBytes());
        kg.init(128, secureRandom);
        SecretKey secretKey = kg.generateKey();
        return secretKey.getEncoded();
    }

    public static String parseByte2HexStr(byte[] buf) {
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

    public static byte[] parseHexStr2Byte(String hexStr) {
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
