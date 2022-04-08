//package com.liansheng.carworld.utils;
//
//
//import android.util.Base64;
//
//import java.security.Key;
//
//import javax.crypto.Cipher;
//import javax.crypto.spec.SecretKeySpec;
//
///**
// * 与php互通的AES加密类
// * @author hzy19
// *
// */
//public class CryptAES {
//
//	private static final String AESTYPE ="AES/ECB/PKCS5Padding";
//
//	private static Key generateKey(String key){
//		try{
//			SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
//			return keySpec;
//		}catch(Exception e){
//			e.printStackTrace();
//			throw e;
//		}
//	}
//
//	/**
//	 * AES加密
//	 * @param keyStr
//	 * @param plainText
//	 * @return
//	 */
//	public static String AESEncrypt(String keyStr, String plainText) {
//		byte[] encrypt = null;
//		try{
//			Key key = generateKey(keyStr);
//			Cipher cipher = Cipher.getInstance(AESTYPE);
//			cipher.init(Cipher.ENCRYPT_MODE, key);
//			encrypt = cipher.doFinal(plainText.getBytes());
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		return new String(android.util.Base64.encodeBase64(encrypt));
//	}
//
//	/**
//	 * AES解密
//	 * @param keyStr
//	 * @param encryptData
//	 * @return
//	 */
//	public static String AESDecrypt(String keyStr, String encryptData) {
//		byte[] decrypt = null;
//		try{
//			Key key = generateKey(keyStr);
//			Cipher cipher = Cipher.getInstance(AESTYPE);
//			cipher.init(Cipher.DECRYPT_MODE, key);
//			decrypt = cipher.doFinal(Base64.decodeBase64(encryptData));
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		return new String(decrypt).trim();
//	}
//
//}
