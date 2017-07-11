package com.tasfe.framework.config.service;

import java.io.UnsupportedEncodingException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EncryptUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(EncryptUtils.class);
    private static final String Algorithm = "DESede";

    public EncryptUtils() {
    }

    private static byte[] encryptMode(byte[] src, String key) {
        try {
            SecretKey deskey = new SecretKeySpec(build3DesKey(key), "DESede");
            Cipher c1 = Cipher.getInstance("DESede");
            c1.init(1, deskey);
            return c1.doFinal(src);
        } catch (Exception var4) {
            return null;
        }
    }

    private static byte[] decryptMode(byte[] src, String cryptKey) {
        try {
            SecretKey deskey = new SecretKeySpec(build3DesKey(cryptKey), "DESede");
            Cipher c1 = Cipher.getInstance("DESede");
            c1.init(2, deskey);
            return c1.doFinal(src);
        } catch (Exception var4) {
            LOGGER.warn("解密失败!", var4);
            return null;
        }
    }

    private static byte[] build3DesKey(String keyStr) throws UnsupportedEncodingException {
        byte[] key = new byte[24];
        byte[] temp = keyStr.getBytes("UTF-8");
        if (key.length > temp.length) {
            System.arraycopy(temp, 0, key, 0, temp.length);
        } else {
            System.arraycopy(temp, 0, key, 0, key.length);
        }

        return key;
    }

    public static String encryptMode(String value, String encryptKey) {
        String secretValue = null;

        try {
            secretValue = bytesToHexString(encryptMode(value.getBytes("UTF-8"), encryptKey));
        } catch (UnsupportedEncodingException var4) {
            LOGGER.warn("找不到编码表!", var4);
        }

        return secretValue;
    }

    public static String decryptMode(String secretValue, String encryptKey) {
        String value = null;

        try {
            value = new String(decryptMode(hexStringToByte(secretValue), encryptKey), "UTF-8");
        } catch (UnsupportedEncodingException var4) {
            LOGGER.warn("找不到编码表!", var4);
        }

        return value;
    }

    private static byte[] hexStringToByte(String hex) {
        int len = hex.length() / 2;
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();

        for (int i = 0; i < len; ++i) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }

        return result;
    }

    private static byte toByte(char c) {
        byte b = (byte) "0123456789abcdef".indexOf(c);
        return b;
    }

    private static String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);

        for (int i = 0; i < bArray.length; ++i) {
            String sTemp = Integer.toHexString(255 & bArray[i]);
            if (sTemp.length() < 2) {
                sb.append(0);
            }

            sb.append(sTemp);
        }

        return sb.toString();
    }
}
