package com.xnf.henghenghui.util;

import android.util.Log;

public class L {
	private static final boolean DEBUG = true;
	
	public static void v(String tag,String msg){
		if(DEBUG){
			Log.v(tag, msg);
		}
	}
	
	public static void d(String tag,String msg){
		if(DEBUG){
			Log.d(tag, msg);
		}
	}
	
	public static void i(String tag,String msg){
		if(DEBUG){
			Log.i(tag, msg);
		}
	}
	
	public static void w(String tag,String msg){
		if(DEBUG){
			Log.w(tag, msg);
		}
	}
	
	public static void e(String tag,String msg){
		if(DEBUG){
			Log.e(tag, msg);
		}
	}
	
	public static void w(String tag,String msg,Throwable t){
		if(DEBUG){
			Log.w(tag, msg, t);
		}
	}
	
	public static void e(String tag,String msg,Throwable t){
		if(DEBUG){
			Log.e(tag, msg, t);
		}
	}
}
