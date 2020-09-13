package online.smyhw.localnet.lib.encryption;

import java.security.*;
import java.security.interfaces.*;
import java.security.spec.*;
import java.util.*;
import javax.crypto.*;
import javax.crypto.spec.*;

/**
 * RSA加解密处理</br>
 * @author smyhw
 *
 */
public class RSA 
{
	/**
	 * 创建一对RSA密钥对</br>
	 * 包含公钥和私钥</br>
	 * 参数keysize表示密钥长度
	 * @return 返回一个二维byte数组，其中,[0]表示公钥,[1]表示私钥
	 * @throws 任何异常都将会抛给上级
	 */
	public static byte[][] genKeyPair(int keysize) throws NoSuchAlgorithmException {  
		//实例化密钥对生成器
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");  
		// 初始化密钥对生成器
		keyPairGen.initialize(keysize,new SecureRandom());  
		// 生成密钥对
		KeyPair keyPair = keyPairGen.generateKeyPair();  
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥  
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // 得到公钥  
		byte[][] re = new byte[2][];
		re[0] = publicKey.getEncoded();
		re[1] = privateKey.getEncoded();
		return re;
	} 
	
	/**
	 * 同genKeyPair(int keysize)</br>
	 * 此重载方法将默认指定keysize参数为1024</br>
	 * @return 返回公私密钥对
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[][] genKeyPair() throws NoSuchAlgorithmException {   
		return genKeyPair(1024);
	}
	
	/**
	 * 根据给定的密钥加密明文
	 * @param input 需要加密的明文
	 * @param key 密钥
	 * @return 加密后的密文
	 * @throws Exception 任何异常都会抛给上一级
	 */
	public static byte[] encryption (byte[] input, byte[] key) throws Exception{
		Cipher cipher = Cipher.getInstance("RSA");
		RSAPublicKey rsa_key = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(key));
		cipher.init(Cipher.ENCRYPT_MODE, rsa_key);
		return cipher.doFinal(input);
	}
	
	/**
	 * 根据给定的密钥解密密文
	 * @param input 需要解密的密文
	 * @param key 密钥
	 * @return 解密后的明文
	 * @throws Exception 任何异常都会抛给上一级
	 */
	public static byte[] decryption(byte[] input, byte[] key,boolean mode) throws Exception{
		Cipher cipher = Cipher.getInstance("RSA");
		RSAPrivateKey rsa_key = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(key));  
		cipher.init(Cipher.DECRYPT_MODE, rsa_key);
		return cipher.doFinal(input);
	}

}
