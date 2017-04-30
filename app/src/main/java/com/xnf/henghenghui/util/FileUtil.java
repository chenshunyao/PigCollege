package com.xnf.henghenghui.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.math.BigDecimal;


import com.xnf.henghenghui.HengHengHuiAppliation;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

public class FileUtil {

  /**
   * 对文件中的某一部分的数据进行加密
   * 
   * @param fName :要修改的文件名字
   * @param start :起始字节
   * @param len :要修改多少个字节
   * @return :是否修改成功
   * @throws Exception :文件读写中可能出的错
   */
  public static boolean encryptFile(String fName, int start, int len) throws Exception {
    java.io.RandomAccessFile raf = new java.io.RandomAccessFile(fName, "rw");
    long totalLen = raf.length();
    FileChannel channel = raf.getChannel();

    java.nio.MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_WRITE, start, len);

    byte[] original = new byte[len];
    for (int i = 0; i < len; i++) {
      original[i] = buffer.get(i);
    }

    byte[] encrypt = AESUtil.encrypt(original, "12345678");

    for (int i = 0; i < len; i++) {
      byte src = encrypt[i];
      buffer.put(i, src);
    }

    buffer.force();
    buffer.clear();
    channel.close();
    raf.close();
    return true;
  }

  /**
   * 对文件中的某一部分的数据进行解密
   * 
   * @param fName :要修改的文件名字
   * @param start :起始字节
   * @param len :要修改多少个字节
   * @return :是否修改成功
   * @throws Exception :文件读写中可能出的错
   */
  public static boolean decryptFile(String fName, int start, int len) throws Exception {
    java.io.RandomAccessFile raf = new java.io.RandomAccessFile(fName, "rw");
    long totalLen = raf.length();

    FileChannel channel = raf.getChannel();

    java.nio.MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_WRITE, start, len);

    byte[] encrypt = new byte[len];
    for (int i = 0; i < len; i++) {
      encrypt[i] = buffer.get(i);
    }

    Debug.print("encrypt2 string:" + Arrays.toString(encrypt));

    byte[] original = AESUtil.decrypt(encrypt, "12345678");

    Debug.print("original2 string:" + Arrays.toString(original));

    for (int i = 0; i < len; i++) {
      byte src = original[i];
      buffer.put(i, src);
    }

    buffer.force();
    buffer.clear();
    channel.close();
    raf.close();
    return true;
  }

  /**
   * 得到本地文件保存路径
   * 
   * @return
   */
  public static String getSaveDir(Context context) {

    String fileDir;

    if (Environment.getExternalStorageState().equals(
            Environment.MEDIA_MOUNTED)) { // 判断SD卡是否存在
      File f = context.getExternalCacheDir();
      if (null == f) {
        fileDir =
            Environment.getExternalStorageDirectory().getPath() + File.separator
                + context.getPackageName() + File.separator + "cache";
      } else {
        fileDir = f.getPath();
      }
    } else {
      File f = context.getCacheDir();
      fileDir = f.getPath();
    }
    return fileDir;
  }

  /**
   * 将服务器的返回值保存至文件中
   * 
   * @param fileSavePath
   * @param result
   */
  public static void saveFileForLocal(String fileSavePath, String result) {
    String path = fileSavePath.substring(0, fileSavePath.lastIndexOf("/"));
    String fileName = fileSavePath.substring(fileSavePath.lastIndexOf("/"), fileSavePath.length());
    File file = new File(path, fileName);

    if (file.exists()) {
      file.delete();
    }
    try {
      FileOutputStream fout = new FileOutputStream(file);
      byte[] buffer = result.getBytes();
      fout.write(buffer);
      fout.close();
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public static void deleteFileFromLocal(String fileSavePath) {
    File file = new File(fileSavePath);
    if (file.exists()) {
      file.delete();
    }
  }

  public static boolean isFolderExists(String strFolder)
  {
    File file = new File(strFolder);

    if (!file.exists())
    {
      if (file.mkdir())
      {
        return true;
      }
      else
        return false;
    }
    return true;
  }
//  /**
//   * 从文件中读取字符串
//   *
//   * @param fileSavePath
//   * @return
//   */
//  @SuppressWarnings("resource")
//  public static String getFileFromLocal(String fileSavePath) {
//    File file = new File(fileSavePath);
//    String result = "";
//    if (file.exists()) {
//      FileInputStream fileIn;
//      try {
//        fileIn = new FileInputStream(file);
//        int length = fileIn.available();
//        byte[] buffer = new byte[length];
//        fileIn.read(buffer);
//        result = EncodingUtils.getString(buffer, "UTF-8");
//      } catch (FileNotFoundException e) {
//        // TODO Auto-generated catch block
//        e.printStackTrace();
//      } catch (IOException e) {
//        // TODO Auto-generated catch block
//        e.printStackTrace();
//      }
//      return result;
//    }
//    return "";
//  }

  /**
   * 获取外部的sdcard目录地址 Environment.getExternalStorageDirectory() = /mnt/sdcard
   * 
   * @return
   */
  public static String getExternalStorageDirPath() {
    return Environment.getExternalStorageDirectory().getAbsolutePath();
  }

  /**
   * 获取程序外部的缓存目录地址 context.getExternalCacheDir() = /mnt/sdcard/Android/data/com.mt.mtpp/cache
   * 
   * @return
   */
  public static String getExternalCacheDirPath() {
    return HengHengHuiAppliation.getInstance().getApplicationContext().getExternalCacheDir()
        .getAbsolutePath();
  }

  /**
   * 获取程序内部缓存的目录地址 context.getCacheDir() = /data/data/com.mt.mtpp/cache
   * 
   * @return
   */
  public static String getCacheDirPath() {
    return HengHengHuiAppliation.getInstance().getApplicationContext().getCacheDir().getAbsolutePath();
  }

  /**
   * 获取程序内部files目录地址 context.getFilesDir() = /data/data/com.mt.mtpp/files
   * 
   * @return
   */
  public static String getFilesDirPath() {
    return HengHengHuiAppliation.getInstance().getApplicationContext().getFilesDir().getAbsolutePath();
  }


  @SuppressWarnings("unused")
  private static File getStorageFolder(Context ctxt, boolean preferCache) {
    File root = Environment.getExternalStorageDirectory();
    if (!root.canWrite() || preferCache) {
      root = ctxt.getCacheDir();
    }

    if (root.canWrite()) {
      // TODO 文件夹将该为定义好的值
      File dir = new File(root.getAbsolutePath() + File.separator + "AllKids");
      if (!dir.exists()) {
        dir.mkdirs();
        Debug.print(Debug.MODULE_OTHERS, FileUtil.class,
            "Create directory " + dir.getAbsolutePath());
      }
      return dir;
    }
    return null;
  }

  /**
   * Get the external app cache directory.
   * 
   * @param context The context to use
   * @return The external cache dir
   */
  public static File getExternalCacheDir(final Context context) {
    if (hasExternalCacheDir()) {
      return context.getExternalCacheDir();

    }

    // Before Froyo we need to construct the external cache dir ourselves
    final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
    return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
  }

  /**
   * Get the external app cache directory.
   * 
   * @param context The context to use
   * @return The external cache dir
   */
  public static File getSaveCacheDir(final Context context) {
    File f;
    try {
      if (Environment.getExternalStorageState().equals(
          Environment.MEDIA_MOUNTED)) { // 判断SD卡是否存在
        f = context.getExternalCacheDir();
        if (null == f) {
          return context.getCacheDir();
        } else {
          return f;
        }
      } else {
        return context.getCacheDir();

      }
    } catch (Exception e) {
      return context.getCacheDir();
    }
  }


  public static boolean hasExternalCacheDir() {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
  }

  /**
   * 删除文件夹
   * 
   * @param file
   * @return 返回是否成功
   */
  public static boolean deleteFolder(File file) {
    boolean isSuccess = true;
    if (file.exists()) { // 判断文件是否存在
      if (file.isFile()) { // 判断是否是文件
        isSuccess = file.delete();
      } else if (file.isDirectory()) { // 否则如果它是一个目录
        File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
        if (files != null) {
          for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
            if (!deleteFolder(files[i])) // 把每个文件 用这个方法进行迭代
              isSuccess = false;
          }
        }
        if (!file.delete())// 删除目录
          isSuccess = false;
      }
    }
    return isSuccess;
  }

  /**
   * 删除文件夹下的更新文件
   * 
   * @param file
   * @return 返回是否成功
   */
  public static boolean deleteUpdateFolder(File file) {
    boolean isSuccess = true;
    if (file.exists()) { // 判断文件是否存在
      if (file.isFile()) { // 判断是否是文件
        isSuccess = file.delete();
      } else if (file.isDirectory()) { // 否则如果它是一个目录
        File files[] = file.listFiles(new FilenameFilter() {
          @Override
          public boolean accept(File dir, String filename) {
            // TODO Auto-generated method stub
            return filename.toLowerCase().endsWith(".update");
          }
        }); // 声明目录下所有的文件 files[];
        if (files != null) {
          for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
            if (!deleteUpdateFolder(files[i])) // 把每个文件 用这个方法进行迭代
              isSuccess = false;
          }
        }
        if (!file.delete())// 删除目录
          isSuccess = false;
      }
    }
    return isSuccess;
  }

  /**
   * 创建固定大小文件
   * 
   * @param file
   * @return 返回是否成功
   */
  public static void createFile(File file, int size) {
    long start = System.currentTimeMillis();
    FileOutputStream fos = null;
    FileChannel output = null;
    try {
      fos = new FileOutputStream(file);
      output = fos.getChannel();
      output.write(ByteBuffer.allocate(1), size - 1);
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      try {
        if (output != null) {
          output.close();
        }
        if (fos != null) {
          fos.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    long end = System.currentTimeMillis();
    Debug.print("total times " + (end - start));
  }

  /**
   * 转换文件大小
   * 
   * @param fileS
   * @return
   */
  public static String FormatFileSize(long fileS) {
    DecimalFormat df = new DecimalFormat("#.00");
    String fileSizeString = "";
    if (fileS == 0) {
      fileSizeString = "0B";
    } else if (fileS < 1024) {
      fileSizeString = df.format((double) fileS) + "B";
    } else if (fileS < 1048576) {
      fileSizeString = df.format((double) fileS / 1024) + "K";
    } else if (fileS < 1073741824) {
      fileSizeString = df.format((double) fileS / 1048576) + "M";
    } else {
      fileSizeString = df.format((double) fileS / 1073741824) + "G";
    }
    return fileSizeString;
  }

  /**
   * 取得耽搁文件的大小
   * 
   * @param f
   * @return
   * @throws Exception
   */
  public static long getSingleFileSize(File f) throws Exception {
    long s = 0;
    if (f.exists()) {
      FileInputStream fis = null;
      fis = new FileInputStream(f);
      s = fis.available();
    }
    // 服务器对文件进行了转化，详情页返回的课件大小的值不准确
    // 首先对文件大小除以1024，取小数点后一位，不四舍五入。
    // 然后再将该值乘以1024，舍小数点后的书中，不四舍五入
    double a = (double) (s / 1024.0);
    BigDecimal bg = new BigDecimal(a);
    double f1 = bg.setScale(1, BigDecimal.ROUND_DOWN).doubleValue();
    s = (long) (f1 * 1024);
    return s;
  }

  /**
   * 取得文件夹中大小
   * 
   * @param f
   * @return
   * @throws Exception
   */
  public static long getFileSize(File f) throws Exception {
    long size = 0;
    File flist[] = f.listFiles();
    for (int i = 0; i < flist.length; i++) {
      if (flist[i].isDirectory()) {
        size = size + getFileSize(flist[i]);
      } else {
        size = size + flist[i].length();
      }
    }
    return size;
  }

  public static boolean copyFile(InputStream ins, String destFileFullPath) {
    FileOutputStream fos = null;
    try {
      File file = new File(destFileFullPath);
      fos = new FileOutputStream(file);
      byte[] buffer = new byte[8192];
      int count = 0;
      while ((count = ins.read(buffer)) > 0) {
        fos.write(buffer, 0, count);
      }
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    } finally {
      try {
        fos.close();
        ins.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

}
