package com.lydia.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Arrays;

/**
 * AES��ȫ�������.
 */
public abstract class AESCoder extends Coder {
  /**
   * ALGORITHM �㷨 <br>
   * ���滻Ϊ��������һ���㷨��ͬʱkeyֵ��size��Ӧ�ı䡣
   *
   * <pre>
   * DES          		key size must be equal to 56
   * DESede(TripleDES) 	key size must be equal to 112 or 168
   * AES          		key size must be equal to 128, 192 or 256, but 192 and 256 bits may not be available
   * Blowfish     		key size must be multiple of 8, and can only range from 32 to 448 (inclusive)
   * RC2          		key size must be between 40 and 1024 bits
   * RC4(ARCFOUR) 		key size must be between 40 and 1024 bits
   * </pre>
   *
   * ��Key toKey(byte[] key)������ʹ����������
   * <code>SecretKey secretKey = new SecretKeySpec(key, ALGORITHM);</code> �滻
   * <code>
   * DESKeySpec dks = new DESKeySpec(key);
   * SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
   * SecretKey secretKey = keyFactory.generateSecret(dks);S
   * </code>
   */
  public static final String KEY_ALGORITHM = "AES";
  public static final String ALGORITHM_AES = "AES";
  public static final String ALGORITHM_ACP7P = "AES/CBC/PKCS7Padding";
  public static final String PROVIDER_BC = "BC";

  static {
  //  Security.addProvider(new BouncyCastleProvider());
  }
  /**
   * ת����Կ<br>
   *
   * @param key
   * @return
   * @throws Exception
   */
  private static Key toKey(byte[] key) throws Exception {
//    DESKeySpec dks = new DESKeySpec(key);
//    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
//    SecretKey secretKey = keyFactory.generateSecret(dks);

    // ��ʹ�������ԳƼ����㷨ʱ����AES��Blowfish���㷨ʱ�������������滻�������д���
    SecretKey secretKey = new SecretKeySpec(key, KEY_ALGORITHM);
    return secretKey;
  }

  private static IvParameterSpec toIv(byte[] ivKey) {
    return new IvParameterSpec(ivKey);
  }

  /**
   * ����
   *
   * @param data
   * @param key
   * @return
   * @throws Exception
   */
  public static byte[] decrypt(byte[] data, String key, String ivKey) throws Exception {
    Key k = toKey(decryptBASE64(key));
    IvParameterSpec iv = null;
    if (!StringUtils.isBlank(ivKey)) {
      iv = toIv(decryptBASE64(ivKey));
    }

    Cipher cipher = null;
    if (iv != null) {
      cipher = Cipher.getInstance(ALGORITHM_ACP7P, PROVIDER_BC);
      cipher.init(Cipher.DECRYPT_MODE, k, iv);
    } else {
      cipher = Cipher.getInstance(ALGORITHM_AES);
      cipher.init(Cipher.DECRYPT_MODE, k);
    }

    return cipher.doFinal(data);
  }

  /**
   * ����
   *
   * @param data
   * @param key
   * @return
   * @throws Exception
   */
  public static String decrypt(String data, String key, String ivKey) throws Exception {
    byte[] outputData = decryptBASE64(data);
    outputData = decrypt(outputData, key, ivKey);
    return new String(outputData, "UTF-8");
  }

  /**
   * ����
   *
   * @param data
   * @param key
   * @return
   * @throws Exception
   */
  public static byte[] encrypt(byte[] data, String key, String ivKey) throws Exception {
    Key k = toKey(decryptBASE64(key));
    IvParameterSpec iv = null;
    if (!StringUtils.isBlank(ivKey)) {
      iv = toIv(decryptBASE64(ivKey));
    }

    Cipher cipher = null;
    if (iv != null) {
      cipher = Cipher.getInstance(ALGORITHM_ACP7P, PROVIDER_BC);
      cipher.init(Cipher.ENCRYPT_MODE, k, iv);
    } else {
      cipher = Cipher.getInstance(ALGORITHM_AES);
      cipher.init(Cipher.ENCRYPT_MODE, k);
    }

    return cipher.doFinal(data);
  }

  /**
   * ����
   *
   * @param data
   * @param key
   * @return
   * @throws Exception
   */
  public static String encrypt(String data, String key, String ivKey) throws Exception {
    byte[] inputData = data.getBytes("UTF-8");
    inputData = encrypt(inputData, key, ivKey);
    return encryptBASE64(inputData);
  }

  /**
   * ������Կ
   *
   * @return
   * @throws Exception
   */
  public static String initKey() throws Exception {
    return initRandomKey(null);
  }

  /**
   * ������Կ
   *
   * @param seed
   * @return
   * @throws Exception
   */
  public static String initRandomKey(String seed) throws Exception {
    SecureRandom secureRandom = null;

    if (seed != null) {
      secureRandom = new SecureRandom(decryptBASE64(seed));
    } else {
      secureRandom = new SecureRandom();
    }

    KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
    kg.init(secureRandom);
    SecretKey secretKey = kg.generateKey();
    return encryptBASE64(secretKey.getEncoded());
  }

  public static String initFixKey(String seed) throws Exception {
    byte[] data = Arrays.copyOf(seed.getBytes("UTF-8"), 16);
    return encryptBASE64(data);
  }
}
