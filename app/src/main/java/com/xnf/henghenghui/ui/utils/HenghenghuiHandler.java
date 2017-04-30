package com.xnf.henghenghui.ui.utils;

import android.os.Handler;
import android.os.Message;

import com.xnf.henghenghui.util.L;
import com.xnf.henghenghui.util.CodeUtil.CmdCode;
public class HenghenghuiHandler extends Handler {
	
	private static final String TAG = "HenghenghuiHandler";

	private HandlerInterface callback;
	
	public HenghenghuiHandler(HandlerInterface callback) {
		if(callback == null){
			throw new IllegalArgumentException("HandlerInterface can not be null!");
		}
		this.callback = callback;
	}
	
	@Override
	public void handleMessage(Message msg) {
		if (msg == null) {
			return;
		}
		// L.i(TAG,
		// 		"msg code = handleMessage " + Integer.toHexString(msg.what));
		switch (msg.what) {
		//common handle
		case CmdCode.MSG_COMMON_HANDLE:
			callback.handleCommonMsg(msg);
			break;
		default:
			dispatchMsg(msg);
			break;
		}
	}
	
	
	public boolean dispatchMsg(Message msg) {
		if (msg == null) {
			L.i(TAG, "bad code msg is null");
			return false;
		}
		if (CmdCode.isUICode(msg.what)) {
			// L.i(TAG, "ui code" + Integer.toHexString(msg.what));
			return callback.handleUIMessage(msg);
		}else if(CmdCode.isNotifyCode(msg.what)){
			// L.i(TAG, "notify code" + Integer.toHexString(msg.what));
			return callback.handleNotifyMessage(msg);
		}else{
			return callback.handleMessage(msg);
		}
	}
	
	
}
