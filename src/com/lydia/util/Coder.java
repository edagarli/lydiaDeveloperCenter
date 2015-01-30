package com.lydia.util;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;

/**
 * 基础加密组件
 */
public abstract class Coder {
  public static final String KEY_SHA1 = "SHA1";
  public static final String KEY_MD5 = "MD5";

  /**
   * MAC算法可选以下多种算法
   *
   * <pre>
   * HmacMD5
   * HmacSHA1
   * HmacSHA256
   * HmacSHA384
   * HmacSHA512
   * </pre>
   */
  public static final String KEY_MAC = "HmacMD5";

  /**
   * BASE64解密
   *
   * @param key
   * @return
   * @throws Exception
   */
  public static byte[] decryptBASE64(String key) throws Exception {
    return (new BASE64Decoder()).decodeBuffer(key);
  }

  /**
   * BASE64加密
   *
   * @param key
   * @return
   * @throws Exception
   */
  public static String encryptBASE64(byte[] key) throws Exception {
    return (new BASE64Encoder()).encode(key);
  }

  /**
   * BASE64加密
   *
   * @param key
   * @return
   * @throws Exception
   */
  public static String encryptBASE64(String key) throws Exception {
    byte[] inputData = key.getBytes(Constant.UTF8);
    return encryptBASE64(inputData);
  }

  /**
   * MD5加密
   *
   * @param data
   * @return
   * @throws Exception
   */
  public static byte[] encryptMD5(byte[] data) throws Exception {
    MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
    md5.update(data);
    return md5.digest();
  }

  /**
   * MD5加密
   *
   * @param data
   * @return
   * @throws Exception
   */
  public static String encryptMD5(String data) throws Exception {
    byte[] inputData = data.getBytes("UTF-8");
    inputData = encryptMD5(inputData);
    return new String(inputData, "UTF-8");
  }

  /**
   * SHA1加密
   *
   * @param data
   * @return
   * @throws Exception
   */
  public static byte[] encryptSHA1(byte[] data) throws Exception {
    MessageDigest sha = MessageDigest.getInstance(KEY_SHA1);
    sha.update(data);
    return sha.digest();
  }

  public static String encryptSHA1(String data) throws Exception {
    byte[] inputData = data.getBytes("UTF-8");
    inputData = encryptSHA1(inputData);
    return new String(inputData, "UTF-8");
  }

  /**
   * 初始化HMAC密钥
   *
   * @return
   * @throws Exception
   */
  public static String initMacKey() throws Exception {
    KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_MAC);
    SecretKey secretKey = keyGenerator.generateKey();
    return encryptBASE64(secretKey.getEncoded());
  }

  /**
   * HMAC加密
   *
   * @param data
   * @param key
   * @return
   * @throws Exception
   */
  public static byte[] encryptHMAC(byte[] data, String key) throws Exception {
    SecretKey secretKey = new SecretKeySpec(decryptBASE64(key), KEY_MAC);
    Mac mac = Mac.getInstance(secretKey.getAlgorithm());
    mac.init(secretKey);

    return mac.doFinal(data);
  }
}

