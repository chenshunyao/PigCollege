package com.xnf.henghenghui.ui.image;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;

import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.xnf.henghenghui.util.L;

/**
 * Draw Circle Bitmap with / without border<BR />
 * designed for QiXin UserInfo Circle Avatar Icon<BR />
 * ImageScaleType cropCenter<BR />
 * default not border
 *  
 * @author laotian
 * @data 2014-04-05
 *
 */
public class CircleBitmapDisplayer implements BitmapDisplayer {
    protected final int border;
    protected final int margin;
    protected final int borderColor;
    
    
    public CircleBitmapDisplayer(){
        this(0,0,Color.WHITE);
    }

    public CircleBitmapDisplayer(int marginPixels) {
        this(marginPixels,0,Color.WHITE);    
    }
    
    /**
     * default border color : Color.WHITE;
     * @param marginPixels
     * @param borderPixels
     */
    public CircleBitmapDisplayer(int marginPixels,int borderPixels)
    {
        this(marginPixels,borderPixels,Color.WHITE);
    }
    
    public CircleBitmapDisplayer(int marginPixels,int borderPixels,int borderColor)
    {
        this.margin=marginPixels;
        this.borderColor=borderColor;
        this.border=borderPixels;
    }
    
    
    @Override
    public void display(Bitmap bitmap, ImageAware imageAware,
            LoadedFrom loadedFrom) {
        
        if (!(imageAware instanceof ImageViewAware)) {
            throw new IllegalArgumentException("ImageAware should wrap ImageView. ImageViewAware is expected.");
        }

        imageAware.setImageDrawable(new CircleDrawable(bitmap, margin,border,borderColor));
        
    }

    protected static class CircleDrawable extends Drawable {

        protected final float border;
        protected final int margin;
        protected final int borderColor;

        protected final RectF mRect = new RectF(),
                mBitmapRect;
        protected final BitmapShader bitmapShader;
        protected final Paint paint;
        protected final Paint paintBorder;
        
        protected final  int bitmapWidth,bitmapHeight;

        public CircleDrawable(Bitmap bitmap,  int margin ,int border, int borderColor) {
            this.border = border;
            this.margin = margin;
            this.borderColor=borderColor;

            bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
      
            bitmapWidth=bitmap.getWidth();;
            bitmapHeight=bitmap.getHeight();
            
            if(bitmapHeight>bitmapWidth)
                  mBitmapRect=new RectF(0, (bitmapHeight-bitmapWidth)/2, bitmapWidth, (bitmapHeight+bitmapWidth)/2);
            else 
                mBitmapRect=new RectF((bitmapWidth-bitmapHeight)/2,0,(bitmapHeight+bitmapWidth)/2,bitmapHeight);
             
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setShader(bitmapShader);
            
            paintBorder=new Paint();
            paintBorder.setColor(borderColor);
            paintBorder.setStyle(Style.FILL);
            paintBorder.setAntiAlias(true);            
        }

        @Override
        protected void onBoundsChange(Rect bounds) {
            super.onBoundsChange(bounds);
        
            mRect.set(margin, margin, bounds.width()-margin , bounds.height()-margin);
            
            if(bounds.height()>bounds.width())
            {
                mRect.set(0, (bounds.height()-bounds.width())/2, bounds.width(), (bounds.height()+bounds.width())/2);
            }
            else {
                mRect.set((bounds.width()-bounds.height())/2,0,(bounds.height()+bounds.width())/2,bounds.height());
            }

            // Resize the original bitmap to fit the new bound
            Matrix shaderMatrix = new Matrix();
            shaderMatrix.setRectToRect(mBitmapRect, mRect, Matrix.ScaleToFit.FILL);
            bitmapShader.setLocalMatrix(shaderMatrix);
            
        }
        

        @Override
        public int getIntrinsicWidth() {
            return bitmapWidth;
        }

        @Override
        public int getIntrinsicHeight() {
            return bitmapHeight;
        }

        @Override
        public void draw(Canvas canvas) {
            
            L.v("laotian", "bounds:" + getBounds());
            
            float radius=Math.min(mRect.width(), mRect.height()) / 2 -margin;
            
            if(border>0)
            {
                canvas.drawCircle(mRect.centerX(), mRect.centerY(), radius, paintBorder);
            }
            //draw Image
            canvas.drawCircle(mRect.centerX(), mRect.centerY(), radius-border, paint);            
        }

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSLUCENT;
        }

        @Override
        public void setAlpha(int alpha) {
            paint.setAlpha(alpha);
        }

        @Override
        public void setColorFilter(ColorFilter cf) {
            paint.setColorFilter(cf);
        }
    }
}