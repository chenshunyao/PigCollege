package com.xnf.henghenghui.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.os.Handler.Callback;

import com.xnf.henghenghui.model.UserInfo;
import com.xnf.henghenghui.ui.base.BaseActivity;
import com.xnf.henghenghui.ui.utils.Utils;

public class Cache {

	private static Cache mCache = null;

	private Cache() {

	}

	public static Cache getCache() {
	    synchronized (Cache.class) {
	        if (null == mCache) {
	            mCache = new Cache();
            }
        }
        return mCache;
    }

	private Callback callBack;
	private UserInfo userInfo;
	private boolean isNeedUpdate;
	private String mPhoneNumber;
	//add by linmeng begin
	private String mCodeGroupId;
	//add by linmeng end
	private boolean mIsDBVersion34;
	private boolean netWorkIsOk;
	private boolean isNetWorkInit = false;
	private String mMobile;
	

    private List<BaseActivity> activitys = new ArrayList<BaseActivity>();

	public boolean isNetWorkOk(Context context) {
	    if (!isNetWorkInit) {
	        netWorkIsOk = !Utils.hasNoNetwork(context);
	        isNetWorkInit = true;
	    }
        return netWorkIsOk;
    }

    public void setNetWorkIsOk(boolean netWorkIsOk) {
        this.netWorkIsOk = netWorkIsOk;
    }

    public boolean isNeedUpdate() {
		return isNeedUpdate;
	}

	public void setNeedUpdate(boolean isNeedUpdate) {
		this.isNeedUpdate = isNeedUpdate;
	}

    private HashMap<Long, Integer> updateEnterpriseMap = new HashMap<Long, Integer>();
	
	private HashMap<String, Object> selectionContactsMap = new HashMap<String, Object>();

	public Callback getCallBack() {
		return callBack;
	}

	public void setCallBack(Callback callBack) {
		this.callBack = callBack;
	}

    public UserInfo getUserInfo() {
        return userInfo;
    }

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}
	
    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }
    
    //add by linmeng begin
    public String getCodeGroupId() {
        return mCodeGroupId;
    }

    public void setCodeGroupId(String mCodeGroupId) {
        this.mCodeGroupId = mCodeGroupId;
    }
    //add by linmeng end
    
	// select all contacts map
	public HashMap<String, Object> getSelectionContactsMap() {
		return selectionContactsMap;
	}

	public void addSelectionContactMap(String mobile, Object model) {
		if (selectionContactsMap != null) {
			if (!selectionContactsMap.containsKey(mobile)) {
				selectionContactsMap.put(mobile, model);
			}
		}
	}

	public void removeSelectionContactMap(String mobile) {
		if (selectionContactsMap != null) {
			if (selectionContactsMap.containsKey(mobile)) {
				selectionContactsMap.remove(mobile);
			}
		}
	}

	public void clearContactSelectionCache() {
		if (selectionContactsMap != null) {
			selectionContactsMap.clear();
		}
	}

    public void setUpdateStatus(long enterpriseId, int status) {
        if (updateEnterpriseMap != null) {
            updateEnterpriseMap.put(enterpriseId, status);
        }
    }

    public void clearActivitys() {
        Iterator<BaseActivity> iterator = activitys.iterator();
        while(iterator.hasNext()) {
            BaseActivity activity = iterator.next();
            activity.finish();
            iterator.remove();
        }
    }

    public void addActivitys(BaseActivity activity) {
        activitys.add(activity);
    }

	public void setConversationMobile(String mobile) {
		mMobile = mobile;
	}
	
	public String getConversationMobile() {
		return mMobile;
	}
}
