package me.arjenlee.shirolearn.util;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class RSA {
    public static final String CHARSET = "utf-8";
    public static final String KEY_ALGORITHM = "RSA";
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";
    private static final String PUBLIC_KEY = "LocatorPublicKey";
    private static final String PRIVATE_KEY = "LocatorPrivateKey";
    private static final int MAX_ENCRYPT_BLOCK = 117;
    private static final int MAX_DECRYPT_BLOCK = 128;

    public RSA() {
    }

    public static Map<String, Object> genKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        HashMap keyMap = new HashMap(2);
        keyMap.put("LocatorPublicKey", publicKey);
        keyMap.put("LocatorPrivateKey", privateKey);
        return keyMap;
    }

    public static String sign(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = Base64.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initSign(privateK);
        signature.update(data);
        return Base64.encode(signature.sign());
    }

    public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {
        byte[] keyBytes = Base64.decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initVerify(publicK);
        signature.update(data);
        return signature.verify(Base64.decode(sign));
    }

    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey) throws Exception {
        byte[] keyBytes = Base64.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(2, privateK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;

        for (int i = 0; inputLen - offSet > 0; offSet = i * 128) {
            byte[] cache;
            if (inputLen - offSet > 128) {
                cache = cipher.doFinal(encryptedData, offSet, 128);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }

            out.write(cache, 0, cache.length);
            ++i;
        }

        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    public static String decryptByPrivateKey(String encryptedStr, String privateKey) throws Exception {
        byte[] encryptedData = Base64.decode(encryptedStr);
        byte[] keyBytes = Base64.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(2, privateK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;

        for (int i = 0; inputLen - offSet > 0; offSet = i * 128) {
            byte[] cache;
            if (inputLen - offSet > 128) {
                cache = cipher.doFinal(encryptedData, offSet, 128);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }

            out.write(cache, 0, cache.length);
            ++i;
        }

        byte[] decryptedData = out.toByteArray();
        out.close();
        return new String(decryptedData, "utf-8");
    }

    public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey) throws Exception {
        byte[] keyBytes = Base64.decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(2, publicK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;

        for (int i = 0; inputLen - offSet > 0; offSet = i * 128) {
            byte[] cache;
            if (inputLen - offSet > 128) {
                cache = cipher.doFinal(encryptedData, offSet, 128);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }

            out.write(cache, 0, cache.length);
            ++i;
        }

        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    public static byte[] encryptByPublicKey(byte[] data, String publicKey) throws Exception {
        byte[] keyBytes = Base64.decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(1, publicK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;

        for (int i = 0; inputLen - offSet > 0; offSet = i * 117) {
            byte[] cache;
            if (inputLen - offSet > 117) {
                cache = cipher.doFinal(data, offSet, 117);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }

            out.write(cache, 0, cache.length);
            ++i;
        }

        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    public static String encryptByPublicKey(String source, String publicKey) throws Exception {
        byte[] data = source.getBytes("utf-8");
        byte[] keyBytes = Base64.decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(1, publicK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;

        for (int i = 0; inputLen - offSet > 0; offSet = i * 117) {
            byte[] cache;
            if (inputLen - offSet > 117) {
                cache = cipher.doFinal(data, offSet, 117);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }

            out.write(cache, 0, cache.length);
            ++i;
        }

        byte[] encryptedData = out.toByteArray();
        out.close();
        return Base64.encode(encryptedData);
    }

    public static byte[] encryptByPrivateKey(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = Base64.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(1, privateK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;

        for (int i = 0; inputLen - offSet > 0; offSet = i * 117) {
            byte[] cache;
            if (inputLen - offSet > 117) {
                cache = cipher.doFinal(data, offSet, 117);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }

            out.write(cache, 0, cache.length);
            ++i;
        }

        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    public static String getPrivateKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get("LocatorPrivateKey");
        String encode = Base64.encode(key.getEncoded());
        return encode;
    }

    public static String getPublicKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get("LocatorPublicKey");
        return Base64.encode(key.getEncoded());
    }

    public static void main(String[] args) {
        try {
            Map<String, Object> keyPair = RSA.genKeyPair();
            System.out.println("公钥:[" + getPublicKey(keyPair) + "]");
            System.out.println("私钥:[" + getPrivateKey(keyPair) + "]");


            String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC7Axl+Wo4OiVFJU/r4oH7MffO0PDUlGKUvg3d325Kbnj4SHmkKLehwUhkIB2jU0/OmfR9OoYfZ6+sHK86C0LdEbfepVf661TgQPYTHSn8b/9HRjNfCC7mVNfQ2M2COTNHKp/JYgfbdpiSG8eslkG0Nt9jctosmdA8Nwzgan5Pl9wIDAQAB";
            String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALsDGX5ajg6JUUlT+vigfsx987Q8NSUYpS+Dd3fbkpuePhIeaQot6HBSGQgHaNTT86Z9H06hh9nr6wcrzoLQt0Rt96lV/rrVOBA9hMdKfxv/0dGM18ILuZU19DYzYI5M0cqn8liB9t2mJIbx6yWQbQ232Ny2iyZ0Dw3DOBqfk+X3AgMBAAECgYBsBrlWPLbsfLPg2MsTjZpRQ7xf85t7Z0YcMSP0r3h7vif6zWg9whEPuMzOz4Dl191Z4dMQ18wivN2R0eNDu47w0l8zFIX628FMQGWJIHyQO8rcGVO23SuShHlEbXN9gQrIVhkMzbUPjyzniose+vuAXUV2HAJNTmZDr0clkbm6sQJBAON6TpQwRHr3L7uSDW62LApwWVXfcN7fYX9SQYe2pnwwpjCGxakHWTto5khn5/HtGHgQ3IO6880+dDvkGjzSBikCQQDSdef7iuJZHcYd0Eg5kQIiFepOi2p3ifS+HrUGmoBXcManb8YPU1xUFvKM6s7yYf3wsoxYAMofRj/dH1g3Gs8fAkBVD1T2wJ26xVeJ8fEn5uLM/HnLTIPuMuVIML/kOX5DjgV2u86Jh//r4SvLA1+N7TRrIRL0ByJHKwVXp9HP0FERAkAFdzZtG/BA7DsG7y/Q/ukVRYhuPcSoEhcfEOEWqNCIdM9T7d0w7LNLI8Vsz3RMaysTIy/t4SdepI5oCbZayAF1AkEAhQniONJDqTLofmPv//QWDEEEsVnPB6gZmQUlpCowf5YO4c5/0YWsgh5GHmbprA7uyOYfExan5+9nYeaIuOi+pA==";
            String dataStr = "kjl中国78394379hello";

            byte[] bytes = RSA.encryptByPrivateKey(dataStr.getBytes("utf-8"), privateKey);
            System.out.println("加密后的数据：" + Base64.encode(bytes));

            byte[] bytes1 = RSA.decryptByPublicKey(bytes, publicKey);
            String aaa = new String(bytes1);
            System.out.println("解密后的数据：" + aaa);

            // 公钥加密的数据：
            String miwen = "DE2mFoAVn8+Kg103F7uvrAOXX3LyUHr/FLiv0LqCiqMye3ppdnfmzfWAuUpqHLpK2z72YSdxMyXNef0DyfGobHjUT5hRsFbVQq3oX+/31Sb+LtXRDFnu6r7CcL5KcMLlEUYYZUq7+oLtJSy90Vrm2qtaDb2FsiU25e4YJmn0fnU=";
            byte[] decode = Base64.decode(miwen);
            // 私钥解密：
            byte[] bytes2 = RSA.decryptByPrivateKey(decode, privateKey);
            aaa = new String(bytes2);
            System.out.println("解密后的数据：" + aaa);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
