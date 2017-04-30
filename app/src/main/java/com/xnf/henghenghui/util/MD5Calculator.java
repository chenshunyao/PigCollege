package com.xnf.henghenghui.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


/**
 * Description: MD5加解密工具类
 */
public class MD5Calculator {
  private static final int BUFFER_SIZE = 4096;

  public static String bufferToHex(byte[] array, int offset, int length) {
    final char[] HEX_CHAR =
        {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    StringBuilder result = new StringBuilder();

    for (int i = offset; i < offset + length; i++) {
      byte b = array[i];
      result.append(HEX_CHAR[(b >>> 4) & 0x0F]);
      result.append(HEX_CHAR[b & 0x0F]);
    }
    return result.toString();
  }

  public static String calculateMD5(String str) {
    String md5Hex = null;
    MD5Calculator md5Calculator = getMD5Calculator();

    if (str == null) {
      return null;
    }

    md5Calculator.DigestMD5(str);

    byte[] md5Byte = md5Calculator.messageDigest.digest();
    md5Calculator.messageDigest.reset();


    md5Hex = bufferToHex(md5Byte, 0, md5Byte.length);

    return md5Hex;
  }

  private MessageDigest messageDigest = null;

  private static MD5Calculator mMD5Calculator = null;

  public static MD5Calculator getMD5Calculator() {
    if (mMD5Calculator == null) {
      mMD5Calculator = new MD5Calculator();
    }
    return mMD5Calculator;
  }

  public MD5Calculator() {
    try {
      messageDigest = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      Debug.print(Debug.MODULE_OTHERS, MD5Calculator.class, e.toString());
    }
  }

  private void DigestMD5(String str) {
//    FileInputStream fis = null;
//    try {
//      fis = new FileInputStream(filePath);
//      byte[] bytes = new byte[BUFFER_SIZE];
//      int byteCount;
//      while ((byteCount = fis.read(bytes)) > 0) {
//        messageDigest.update(bytes, 0, byteCount);
//      }

      byte[] bytes = str.getBytes();
      messageDigest.update(bytes, 0, bytes.length);

//    } catch (FileNotFoundException e) {
//      Debug.print(Debug.MODULE_OTHERS, MD5Calculator.class, e.toString());
//    } catch (IOException e) {
//      Debug.print(Debug.MODULE_OTHERS, MD5Calculator.class, e.toString());
//    } finally {
//      try {
//        if (fis != null) {
//          fis.close();
//        }
//      } catch (IOException e) {
//        Debug.print(Debug.MODULE_OTHERS, MD5Calculator.class, e.toString());
//      }
//    }
  }
}
