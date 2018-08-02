/**
 *
 */
package com.ack.util;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.ack.common.ServiceException;
import com.ack.config.RedisPool;
import com.ack.enums.RespCode;


/**
 * RSA 非对称加密
 * @author chen.zhao @DATE: Jul 15, 2018
 */
public class RSAUtil {

	static String PRIVATEKEY = "cloudPwdPrivateKey";

	static String PUBLICKEY = "cloudPwdPublicKey";

	private static final Logger log = LogManager.getLogger(RSAUtil.class);

	/**
	 * 初始化生成私钥 公钥
	 */
	public static void init() {
		log.info("#开始生成公钥私钥对");
		KeyPairGenerator keyPairGenerator;
		try {
			keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			SecureRandom secureRandom = new SecureRandom(new Date().toString().getBytes());
			keyPairGenerator.initialize(1024, secureRandom);
			KeyPair keyPair = keyPairGenerator.genKeyPair();
			byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
			RedisPool.del(PRIVATEKEY);
			RedisPool.del(PUBLICKEY);
			RedisPool.setByte(PUBLICKEY.getBytes(), publicKeyBytes);
			byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
			RedisPool.setByte(PRIVATEKEY.getBytes(), privateKeyBytes);
		} catch (Exception e) {
			throw new ServiceException("#生成公钥私钥对异常");
		}
	}

	/**
	 * 获取公钥
	 * 
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	public static PublicKey getPublicKey() throws Exception {
		if (!RedisPool.exists(PUBLICKEY)) {
			init();
		}
		byte[] keyBytes = RedisPool.getByte(PUBLICKEY.getBytes());
		X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return kf.generatePublic(spec);
	}

	/**
	 * 获取私钥
	 * 
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKey() throws Exception {
		byte[] keyBytes = RedisPool.getByte(PRIVATEKEY.getBytes());
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return kf.generatePrivate(spec);
	}
	
	public static String decrypt(String pwd) {
		Cipher cipher;
		byte[] plainText;
		try {
			RSAPrivateKey privKey = (RSAPrivateKey) RSAUtil.getPrivateKey();
			cipher = Cipher.getInstance("RSA");

			cipher.init(Cipher.DECRYPT_MODE, privKey);
			plainText = cipher.doFinal(Base64.decodeBase64(pwd.getBytes()));
			log.info("#密码明文 : " + new String(plainText));
		} catch (Exception e) {
			log.error(e);
			throw new ServiceException(RespCode.FAILURE,e);
		}
		return new String(plainText);
	}
	
	public static String encrypt(String pwd) {
		RSAPublicKey pubKey;
		byte[] cipherText;
		Cipher cipher;
		byte[] plainText;
		try {
			RSAPrivateKey privKey = (RSAPrivateKey) RSAUtil.getPrivateKey();
			pubKey = (RSAPublicKey) RSAUtil.getPublicKey();
			cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, pubKey);
			cipherText = cipher.doFinal(pwd.getBytes());
			cipher.init(Cipher.DECRYPT_MODE, privKey);
			plainText = cipher.doFinal(cipherText);
			log.info("#密码明文 : " + new String(plainText));
		} catch (Exception e) {
			log.error(e);
			throw new ServiceException(RespCode.FAILURE,e);
		}
		return  Base64.encodeBase64String(cipherText);
	}

}