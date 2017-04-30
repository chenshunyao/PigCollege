package com.xnf.henghenghui.ui.image;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2016/2/15.
 */
public abstract interface HotImageHandleInterface {
    public abstract Bitmap handleBitmapShape(Bitmap paramBitmap, HotAsyncImageDefine.ImageType paramImageType);

    public abstract Bitmap scaleBitmap(Bitmap paramBitmap, int paramInt1, int paramInt2);
}
