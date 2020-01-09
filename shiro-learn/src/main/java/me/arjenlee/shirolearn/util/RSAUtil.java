package me.arjenlee.shirolearn.util;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class RSAUtil {
    public static final String KEY_ALGORITHM = "RSA";
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    public static byte[] decryptBASE64(String key) {
        return Base64.decode(key);
    }

    public static String encryptBASE64(byte[] bytes) {
        return Base64.encode(bytes);
    }

    /**
     * 用私钥对信息生成数字签名
     *
     * @param data       加密数据
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        // 解密由base64编码的私钥
        byte[] keyBytes = decryptBASE64(privateKey);
        // 构造PKCS8EncodedKeySpec对象
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        // 取私钥匙对象
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 用私钥对信息生成数字签名
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(priKey);
        signature.update(data);
        return encryptBASE64(signature.sign());
    }

    /**
     * 校验数字签名
     *
     * @param data      加密数据
     * @param publicKey 公钥
     * @param sign      数字签名
     * @return 校验成功返回true 失败返回false
     * @throws Exception
     */
    public static boolean verify(byte[] data, String publicKey, String sign)
            throws Exception {
        // 解密由base64编码的公钥
        byte[] keyBytes = decryptBASE64(publicKey);
        // 构造X509EncodedKeySpec对象
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        // 取公钥匙对象
        PublicKey pubKey = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(pubKey);
        signature.update(data);
        // 验证签名是否正常
        return signature.verify(decryptBASE64(sign));
    }

    public static byte[] decryptByPrivateKey(byte[] data, String key) throws Exception {
        // 对密钥解密
        byte[] keyBytes = decryptBASE64(key);
        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * 解密<br>
     * 用私钥解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(String data, String key)
            throws Exception {
        return decryptByPrivateKey(decryptBASE64(data), key);
    }

    /**
     * 解密<br>
     * 用公钥解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] data, String key)
            throws Exception {
        // 对密钥解密
        byte[] keyBytes = decryptBASE64(key);
        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);
        // 对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    /**
     * 加密<br>
     * 用公钥加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(String data, String key)
            throws Exception {
        // 对公钥解密
        byte[] keyBytes = decryptBASE64(key);
        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data.getBytes());
    }

    /**
     * 加密<br>
     * 用私钥加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String key)
            throws Exception {
        // 对密钥解密
        byte[] keyBytes = decryptBASE64(key);
        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * 取得私钥
     *
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Key> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return encryptBASE64(key.getEncoded());
    }

    /**
     * 取得公钥
     *
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Key> keyMap)
            throws Exception {
        Key key = keyMap.get(PUBLIC_KEY);
        return encryptBASE64(key.getEncoded());
    }

    /**
     * 初始化密钥
     *
     * @return
     * @throws Exception
     */
    public static Map<String, Key> initKey() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator
                .getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        Map<String, Key> keyMap = new HashMap(2);
        keyMap.put(PUBLIC_KEY, keyPair.getPublic());// 公钥
        keyMap.put(PRIVATE_KEY, keyPair.getPrivate());// 私钥
        return keyMap;
    }

    public static void main(String[] args) throws Exception {
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC4gGMpmZX4a2SCuk0Ezj6RiPOprP3dLTkEJEMbQdeUHYM1TSJus6ADCn8rQBPbpBhH6XZ6L/QS3GmTPtoVQ0NkhfKA1NAj9VHsBbKX107INlWAOWJVqKWtgQ3LszcC5VhLjUVh1OECNDUDzto7Qh0U3HS3wkgNmSFm4BZ4W+WczwIDAQAB";
        String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALiAYymZlfhrZIK6TQTOPpGI86ms/d0tOQQkQxtB15QdgzVNIm6zoAMKfytAE9ukGEfpdnov9BLcaZM+2hVDQ2SF8oDU0CP1UewFspfXTsg2VYA5YlWopa2BDcuzNwLlWEuNRWHU4QI0NQPO2jtCHRTcdLfCSA2ZIWbgFnhb5ZzPAgMBAAECgYEAkaJuI8Ei+wMyRGpwLxHpm5FB3LY7cSGoNte0zMgtNbISBrnLKxzIen4HoYkj46TksabcKVi+zUCEaYEcPdppT5CaMytVktmRagR2Dfkvy69Pls8RFLTvZBusbGhiGvMYYXWXFwFw+2rd7+7MkNX6zYJaUS57ZWxYetu7PgMiy3kCQQDryP9S0sF/QlnXVT47syAU3m1YD/5i9jQZ1+tJCvTVtJd2qLLrFsmj3iIWgCRgdbPaf0smzc36bC3mGXr0LVn1AkEAyFHRpiGZHl0wKIjlb7v1GNLJd97vFm9b5NW+Q86yTT5w3t9UPvOm57x4GhDmd0JR0H9dsCUyMgpWu1t3YNZNMwJBAIutvtOp1sc031QZQ4zKSK8UNWJ91w15HNRSfSgAPZp8KV46AgQSs9FHvpnlv6cFE07VKggK04MOowI1Bgmw9CECQBUlPeIxjb48zAabXMzMpHS4W3gB2m8d1i9zuJ/jw8KPos6D+JqkkavIo0ztF8bjA0vlBfjAFGpf2QPyBcn9FS0CQDY5V7zRTKVaZ9+IApItDGfQZ+qKDO6umL1sKh6NgxeiljDVtL6R5Vbd3M9MdZTSNo1m0XEdoO5Wy9W2Gd+6k6w=";

//        Map<String, Key> keyMap = initKey();
//        System.out.println("公钥：" + getPublicKey(keyMap));
//        System.out.println("私钥：" + getPrivateKey(keyMap));
        String pwd = "kjl中国78394379hello";
        System.out.println(pwd);
        System.out.println("加密后：");
//        byte[] encryptData = encryptByPublicKey(pwd, getPublicKey(keyMap));//公钥加密
        byte[] encryptData = encryptByPublicKey(pwd, publicKey);//公钥加密
        String encryptStr = encryptBASE64(encryptData);
        System.out.println(encryptStr);
        System.out.println("解密后：");
        //byte[] decryptData = decryptByPrivateKey(decryptBASE64(encryptStr), getPrivateKey(keyMap));//私钥解密
        //System.out.println(new String(decryptData));


        String miwen = "RAvme947sVfbkglnnJbTrSFHDfRb1BjjBqfd92crmQUxho36hmTBl6YeF+Odtp4fGgfJyDo19maKlkplsJenncYlihXWr6EEap/oWXnaBKIi5oRUjfKTnBeuuacDtjym7Rzh0JhGjcF5FXwLnn+y3wDaN5QoJe4eWwl3xLiLdDw=";
        byte[] decryptData = decryptByPrivateKey(decryptBASE64(miwen), privateKey);//私钥解密
        //decryptData = decryptByPrivateKey(decryptBASE64(encryptStr), privateKey);//私钥解密
        System.out.println(new String(decryptData));
    }
}
