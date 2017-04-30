package com.xnf.henghenghui.util;

import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES 算法 对称加密，密码学中的高级加密标准 2005年成为有效标准 
 * @author stone
 * @date 2014-03-10 06:49:19
 */
public class AESUtil {
    static Cipher cipher;
    static final String KEY_ALGORITHM = "AES";
    static final String CIPHER_ALGORITHM_ECB = "AES/ECB/PKCS5Padding";
    /*
     * 
     */
    static final String CIPHER_ALGORITHM_CBC = "AES/CBC/PKCS5Padding";
    /* 
     * AES/CBC/NoPadding 要求
     * 密钥必须是16位的；Initialization vector (IV) 必须是16位
     * 待加密内容的长度必须是16的倍数，如果不是16的倍数，就会出如下异常：
     * javax.crypto.IllegalBlockSizeException: Input length not multiple of 16 bytes
     * 
     *  由于固定了位数，所以对于被加密数据有中文的, 加、解密不完整
     *  
     *  可 以看到，在原始数据长度为16的整数n倍时，假如原始数据长度等于16*n，则使用NoPadding时加密后数据长度等于16*n，
     *  其它情况下加密数据长 度等于16*(n+1)。在不足16的整数倍的情况下，假如原始数据长度等于16*n+m[其中m小于16]，
     *  除了NoPadding填充之外的任何方 式，加密数据长度都等于16*(n+1).
     */
    static final String CIPHER_ALGORITHM_CBC_NoPadding = "AES/CBC/NoPadding"; 
    
    static SecretKey secretKey;
       
    static byte[] getIV() {
        String iv = "1234567812345678"; //IV length: must be 16 bytes long
        return iv.getBytes();
    }
    
    /**
     * 使用AES 算法 加密，默认模式 AES/CBC/NoPadding  参见上面对于这种mode的数据限制
     */
    public static byte[] encrypt(byte[] clear,String seed) throws Exception {
        cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC_NoPadding);
        //KeyGenerator 生成aes算法密钥
        System.out.println("加密前：" + Arrays.toString(clear));
        
        //byte[] rawKey = getRawKey(seed.getBytes());
        //TODO 使用默认的密钥
        byte[] rawKey =new byte[]{48, -66, 101, 43, -63, -116, -113, -42, 92, 44, 53, -93, -83, -21, -100, -18};
        
        secretKey = new SecretKeySpec(rawKey, "AES");

        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(getIV()));//使用加密模式初始化 密钥
        byte[] encrypt = cipher.doFinal(clear, 0, clear.length); //按单部分操作加密或解密数据，或者结束一个多部分操作。
        Debug.print("加密后：" + Arrays.toString(encrypt));
        
        return encrypt;
        
    }
    
    /**
     * 使用AES 算法 加密，默认模式 AES/CBC/NoPadding  参见上面对于这种mode的数据限制
     */
    public static byte[] decrypt(byte[] encrypt,String seed) throws Exception {
        cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC_NoPadding);
        //byte[] rawKey1 = getRawKey(seed.getBytes());
        byte[] rawKey1 =new byte[]{48, -66, 101, 43, -63, -116, -113, -42, 92, 44, 53, -93, -83, -21, -100, -18};
        //byte[] rawKey1 =new byte[]{-124, -86, -63, 47, 84, -85, 102, 110, -49, -62, -88, 60, 103, 105, 8, -56};
        secretKey = new SecretKeySpec(rawKey1, "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(getIV()));//使用解密模式初始化 密钥
        byte[] decrypt = cipher.doFinal(encrypt);
        return decrypt;
    }
    
    private static byte[] getRawKey(byte[] seed) throws Exception {
      KeyGenerator kgen = KeyGenerator.getInstance("AES");
      SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
      sr.setSeed(seed);
      kgen.init(128, sr);
      SecretKey sKey = kgen.generateKey();
      byte[] raw = sKey.getEncoded();

      return raw;
  }
    
}
