package online.smyhw.localnet.lib.encryption;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES算法加解密操作
 *
 * @author smyhw
 */
public class AES {

    /**
     * 根据给定的密钥加密明文
     *
     * @param input 需要加密的明文
     * @param key   密钥
     * @return 加密后的密文
     * @throws Exception 任何异常都会抛出给上一级
     */
    public static byte[] encryption(byte[] input, byte[] key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec("1234567887654321".getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), iv);
        byte[] bytes = cipher.doFinal(input);
        return bytes;
    }

    /**
     * 根据给定的密钥解密密文
     *
     * @param input 需要解密的密文
     * @param key   密钥
     * @return 解密后的明文
     * @throws Exception 任何异常都会抛出给上一级
     */
    public static byte[] decryption(byte[] input, byte[] key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec("1234567887654321".getBytes());
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), iv);
        byte[] bytes = cipher.doFinal(input);
        return bytes;
    }

    /**
     * 参考byte[] encryption(byte[] input, byte[] key)
     * 本重载接收String类型的密钥和明文,并返回String类型的密文
     */
    public static String encryption(String input, String key, boolean mode) throws Exception {
        return new String(encryption(input.getBytes("utf-8"), key.getBytes("utf-8")));
    }

    /**
     * 参考byte[] decryption(byte[] input, byte[] key)
     * 本重载接收String类型的密钥和密文,并返回String类型的明文
     */
    public static String decryption(String input, String key, boolean mode) throws Exception {
        return new String(decryption(input.getBytes("utf-8"), key.getBytes("utf-8")));
    }

}
