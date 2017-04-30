package com.xnf.henghenghui.dao;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.xnf.henghenghui.model.SmsInfo;

public class SmsDao extends AbstractDao{  
	
	private Uri uri = Uri.parse("content://sms/inbox");
	
	public SmsDao(Context context, HenghenghuiAsyncQueryResultCallback mCallback) {
		super(context, mCallback);
	}

  
    public SmsInfo getSmsInfo(long date) {  
        String[] projection = new String[] { "_id", "address", "person",  
                "body", "date", "type" };  
        ContentResolver cr = mContext.getContentResolver();
        SmsInfo smsinfo = null;
        Cursor cusor = null;
        try {
            cusor = cr.query(uri, projection, " date > ? ",
                    new String[]{ date+ "" }, " date desc ");
            if (cusor != null) {
                int nameColumn = cusor.getColumnIndex("person");
                int phoneNumberColumn = cusor.getColumnIndex("address");
                int smsbodyColumn = cusor.getColumnIndex("body");
                int dateColumn = cusor.getColumnIndex("date");
                int typeColumn = cusor.getColumnIndex("type");
                if (cusor.moveToFirst()) {
                    smsinfo = new SmsInfo();
                    smsinfo.setName(cusor.getString(nameColumn));  
                    smsinfo.setDate(cusor.getString(dateColumn));  
                    smsinfo.setPhoneNumber(cusor.getString(phoneNumberColumn));  
                    smsinfo.setSmsbody(cusor.getString(smsbodyColumn));  
                    smsinfo.setType(cusor.getString(typeColumn));
                }  
                cusor.close();
            }  
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cusor != null && !cusor.isClosed()) {
                cusor.close();
            }
        }
        return smsinfo;
    }

	@Override
	protected Object cursorToModelOrModelList(int token, Object cookie,
			Cursor cursor) {
		return null;
	}  
}  
