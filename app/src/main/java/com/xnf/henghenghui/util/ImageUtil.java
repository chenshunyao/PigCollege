package com.xnf.henghenghui.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.graphics.Shader.TileMode;
import android.os.Build;
import android.os.Build.VERSION;

public class ImageUtil {
  public interface ImageCallback {
    public void loadImage(Bitmap bitmap, String imagePath);
  }


  // ///////////////////////////////////////////////////////////////////////
  // 公共方法

  /**
   * 保存图片到缓存
   * 
   * @param imagePath
   * @param bm
   */
  public static void saveImage(String imagePath, Bitmap bm, boolean force) {

    if (bm == null || imagePath == null || "".equals(imagePath)) {
      return;
    }

    File f = new File(imagePath);
    if (f.exists() && !force) {
      return;
    } else {
      try {
        File parentFile = f.getParentFile();
        if (!parentFile.exists()) {
          parentFile.mkdirs();
        }
        f.createNewFile();
        FileOutputStream fos;
        fos = new FileOutputStream(f);
        bm.compress(Bitmap.CompressFormat.PNG, 100, fos);
        fos.close();
      } catch (FileNotFoundException e) {
        f.delete();
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
        f.delete();
      }
    }
  }

  public static void saveImage(String imagePath, Bitmap bm){
    saveImage(imagePath,bm,false);
  }

  /**
   * Get the size in bytes of a bitmap.
   * 
   * @param bitmap
   * @return size in bytes
   */
  @SuppressLint("NewApi")
  public static int getBitmapSize(Bitmap bitmap) {
    if (VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
      return bitmap.getByteCount();
    }
    // Pre HC-MR1
    return bitmap.getRowBytes() * bitmap.getHeight();
  }

  /**
   * 创建倒影图，只包含倒影部分
   * 
   * @param originalImage
   * @return
   */
  public static Bitmap createReflectedBitmapOnly(Bitmap originalImage) {

    int width = originalImage.getWidth();
    int height = originalImage.getHeight();
    // 创建矩阵对象
    Matrix matrix = new Matrix();

    // 指定一个角度以0,0为坐标进行旋转
    // matrix.setRotate(30);

    // 指定矩阵(x轴不变，y轴相反)
    // 1表示放大比例，不放大也不缩小。
    // -1表示在y轴上相反，即旋转180度
    matrix.preScale(1, -1);

    // 将矩阵应用到该原图之中，返回一个宽度不变，高度为原图2/5的倒影位图
    // 第2个参数为剪切的起始x坐标，第3个参数为剪切的起始y坐标，因为只取2/5，所以y的起始坐标为height * 3 / 5
    Bitmap reflectionImage =
        Bitmap.createBitmap(originalImage, 0, height * 4 / 5, width, height * 1 / 5, matrix, false);

    // 创建一个最终效果的图， 倒影。
    Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height * 1 / 5), Config.ARGB_8888);

    // 将上面创建的位图初始化到画布
    Canvas canvas = new Canvas(bitmapWithReflection);
    canvas.drawBitmap(reflectionImage, 0, 0, null);

    Paint paint = new Paint();

    /**
     * 创建LinearGradient，从而给定一个由上到下的渐变色。 参数一:为渐变起初点坐标x位置， 参数二:为y轴位置， 参数三和四:分辨对应渐变终点， 最后参数为平铺方式，
     * 这里设置为镜像Gradient是基于Shader类，所以我们通过Paint的setShader方法来设置这个渐变
     */
    LinearGradient shader =
        new LinearGradient(0, 0, 0, bitmapWithReflection.getHeight(), 0x30ffffff, 0x00ffffff,
            TileMode.MIRROR);
    // 设置阴影
    paint.setShader(shader);
    paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.DST_IN));
    // 用已经定义好的画笔构建一个矩形阴影渐变效果
    canvas.drawRect(0, 0, width, bitmapWithReflection.getHeight(), paint);
    return bitmapWithReflection;
  }

  public static Bitmap zoomBitmap(Bitmap bm, int toWidth, int toHeight){
    int width = bm.getWidth();
    int height = bm.getHeight();
    float sw = (float)toWidth / width;
    float sh = (float)toHeight / height;
    float s = Math.min(Math.min(sw,sh),1f);
    Matrix matrix = new Matrix();
    matrix.postScale(s, s);
    Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
    return newbm;
  }

}
