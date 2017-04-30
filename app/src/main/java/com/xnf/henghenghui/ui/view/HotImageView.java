package com.xnf.henghenghui.ui.view;
import android.util.Log;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.android.volley.VolleyError;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xnf.henghenghui.ui.image.HotAsyncImageDefine;
import com.xnf.henghenghui.ui.image.HotImageHandleInterface;

/**
 * Created by Administrator on 2016/2/14.
 */
public class HotImageView extends ImageView{
    private boolean isNeedScaled;
    private int mDefaultImageId;
    private int mErrorImageId;
    //private ImageLoader.ImageContainer mImageContainer;
    //private HotImageHandleInterface mImageHandleConfig;
    private int mImageHeight;
    private ImageLoader mImageLoader;
    private int mImageWidth;
    private String mUrl;
    //private HotAsyncImageDefine.ImageType type;

    public HotImageView(Context context) {
        this(context,null);
    }

    public HotImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HotImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /*private void loadImageIfNecessary(final boolean paramBoolean){
        int i,j,k;
        try{
            i = getWidth();
            j = getHeight();
            if ((getLayoutParams() == null) || (getLayoutParams().height != -2) || (getLayoutParams().width != -2)){
                k = 0;
            }else{
                k = 1;
            }
            if((i == 0) && (j == 0) && (k == 0))
                return;
            if (TextUtils.isEmpty(mUrl)){
                if (this.mImageContainer != null){
                    this.mImageContainer.cancelRequest();
                    this.mImageContainer = null;
                }
                setDefaultImageOrNull();
                return;
            }
        }catch(Exception ex){
            ex.printStackTrace();
            return;
        }
        if ((this.mImageContainer != null) && (this.mImageContainer.getRequestUrl() != null)){
            if (!this.mImageContainer.getRequestUrl().equals(this.mUrl)){
                this.mImageContainer.cancelRequest();
                setDefaultImageOrNull();
            }
        }else{
            this.mImageContainer = this.mImageLoader.get(mUrl,new ImageLoader.ImageListener(){
                @Override
                public void onResponse(final ImageLoader.ImageContainer response, boolean isImmediate) {
                    if(isImmediate && paramBoolean){
                        HotImageView.this.post(new Runnable() {
                            @Override
                            public void run() {
                                onResponse(response,false);
                            }
                        });
                    }

                    if(HotImageView.this.mDefaultImageId == 0){
                        if(response.getBitmap() != null){
                            HotImageView.this.setImageBitmap(response.getBitmap());
                        }
                    }else{
                        HotImageView.this.setImageResource(HotImageView.this.mDefaultImageId);
                    }
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    if(HotImageView.this.mErrorImageId != 0){
                        HotImageView.this.setImageResource(HotImageView.this.mErrorImageId);
                    }
                }
            },this.mImageWidth,this.mImageHeight);
        }
    }

    private void setDefaultImageOrNull(){
        if (this.mDefaultImageId != 0)
            setImageResource(this.mDefaultImageId);
    }

    protected void drawableStateChanged(){
        super.drawableStateChanged();
        invalidate();
    }

    protected void onDetachedFromWindow(){
        if (this.mImageContainer != null){
            this.mImageContainer.cancelRequest();
            setImageBitmap(null);
            this.mImageContainer = null;
        }
        super.onDetachedFromWindow();
    }

    protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4){
        super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
        loadImageIfNecessary(true);
    }*/

    public void setDefaultImageResId(int paramInt){
        this.mDefaultImageId = paramInt;
        HotImageView.this.setImageResource(paramInt);
    }

    /*public void setErrorImageResId(int paramInt){
        this.mErrorImageId = paramInt;
    }

    public void setImageBitmap(Bitmap paramBitmap){
        if (this.mImageHandleConfig != null){
            Bitmap localBitmap = this.mImageHandleConfig.scaleBitmap(paramBitmap, this.mImageWidth, this.mImageHeight);
            if (localBitmap != null)
                paramBitmap = localBitmap;
            paramBitmap = this.mImageHandleConfig.handleBitmapShape(paramBitmap, this.type);
        }
        super.setImageBitmap(paramBitmap);
    }*/

    public void setImageUrl(String paramString, ImageLoader paramImageLoader,DisplayImageOptions options){
        //setImageUrl(paramString, paramImageLoader, 0, 0, false, null);
        this.mUrl = paramString;
        this.mImageLoader = paramImageLoader;
        Log.d("csy","setImageUrl paramString:"+paramString);
        paramImageLoader.displayImage(paramString,HotImageView.this,options);
    }

    /*public void setImageUrl(String paramString, ImageLoader paramImageLoader, int paramInt1, int paramInt2){
        setImageUrl(paramString, paramImageLoader, paramInt1, paramInt2, false, null);
    }

    public void setImageUrl(String paramString, ImageLoader paramImageLoader, int paramInt1, int paramInt2, boolean paramBoolean){
        setImageUrl(paramString, paramImageLoader, paramInt1, paramInt2, paramBoolean, null);
    }

    public void setImageUrl(String paramString, ImageLoader paramImageLoader, int paramInt1, int paramInt2, boolean paramBoolean, HotAsyncImageDefine.ImageType paramImageType){
        setImageUrl(paramString, paramImageLoader, paramInt1, paramInt2, paramBoolean, paramImageType, null);
    }

    public void setImageUrl(String paramString, ImageLoader paramImageLoader, int paramInt1, int paramInt2, boolean paramBoolean, HotAsyncImageDefine.ImageType paramImageType, HotImageHandleInterface paramMCImageHandleInterface){
        this.mUrl = paramString;
        this.mImageLoader = paramImageLoader;
        this.mImageWidth = paramInt1;
        this.mImageHeight = paramInt2;
        this.isNeedScaled = paramBoolean;
        this.type = paramImageType;
        this.mImageHandleConfig = paramMCImageHandleInterface;
        loadImageIfNecessary(false);
    }*/
}
