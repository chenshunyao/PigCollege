package com.xnf.henghenghui.dao;

import android.net.Uri;

public interface HenghenghuiAsyncQueryResultCallback {

	abstract void onDeleteComplete(int token, Object cookie, int result);

	abstract void onInsertComplete(int token, Object cookie, Uri uri);

	abstract void onQueryComplete(int token, Object cookie, Object object);

	abstract void onUpdateComplete(int token, Object cookie, int result);
}
