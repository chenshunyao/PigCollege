package com.xnf.henghenghui.dao;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * DB operation base Dao
 * All Dao must extends AbstractDao 
 * if Dao has asynchronous operation,
 * just use HenghenghuiAsyncQueryHandler to finish
 * @version 1.0 
 * @data 2014-04-11
 * */

public abstract class AbstractDao {
    protected final String TAG = this.getClass().getSimpleName();

    protected Context mContext;

    protected HenghenghuiAsyncQueryHandler mAsyncQueryHandler;

    protected HenghenghuiAsyncQueryResultCallback mCallback;

    public AbstractDao(Context context,HenghenghuiAsyncQueryResultCallback mCallback) {
        if (context == null) {
            throw new NullPointerException("can not create dao with null context.");
        }
        this.mContext = context.getApplicationContext();
        this.mCallback = mCallback;
        if(null != mCallback){
        	mAsyncQueryHandler = new HenghenghuiAsyncQueryHandler(mContext.getContentResolver());
        }
    }
    
    abstract protected Object cursorToModelOrModelList(int token, Object cookie, Cursor cursor);
    
    class HenghenghuiAsyncQueryHandler extends AsyncQueryHandler {

        public HenghenghuiAsyncQueryHandler(ContentResolver cr) {
            super(cr);
        }

        @Override
        protected void onDeleteComplete(int token, Object cookie, int result) {
            super.onDeleteComplete(token, cookie, result);
            if (mCallback != null) {
                mCallback.onDeleteComplete(token, cookie, result);
            }
        }

        @Override
        protected void onInsertComplete(int token, Object cookie, Uri uri) {
            super.onInsertComplete(token, cookie, uri);
            if (mCallback != null) {
                mCallback.onInsertComplete(token, cookie, uri);
            }
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            super.onQueryComplete(token, cookie, cursor);
            if (mCallback != null) {
                Object o = cursorToModelOrModelList(token, cookie, cursor);
                mCallback.onQueryComplete(token, cookie, o);
            }
        }

        @Override
        protected void onUpdateComplete(int token, Object cookie, int result) {
            super.onUpdateComplete(token, cookie, result);
            if (mCallback != null) {
                mCallback.onUpdateComplete(token, cookie, result);
            }
        }

    }

}
