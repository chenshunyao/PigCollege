package com.xnf.henghenghui.util;

import com.xnf.henghenghui.HengHengHuiAppliation;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferencesWrapper {

  private SharedPreferences prefs;
  private Editor sharedEditor;

  private static PreferencesWrapper mPreferencesWrapper;

  public PreferencesWrapper() {
    prefs =
        HengHengHuiAppliation.getInstance().getApplicationContext()
            .getSharedPreferences("henghenghui", Context.MODE_PRIVATE);
    sharedEditor = prefs.edit();
  }

  public static PreferencesWrapper getInstance() {
    if (mPreferencesWrapper == null) {
      return new PreferencesWrapper();
    }
    return mPreferencesWrapper;

  }

  /**
   * Enter in edit mode To use for bulk modifications
   */
  public void startEditing() {
    sharedEditor = prefs.edit();
  }

  /**
   * Leave edit mode
   */
  public void endEditing() {
    if (sharedEditor != null) {
      sharedEditor.commit();
      sharedEditor = null;
    }
  }

  // Public setters
  /**
   * Set a preference string value
   * 
   * @param key the preference key to set
   * @param value the value for this key
   */
  public void setPreferenceStringValue(String key, String value) {
    if (sharedEditor == null) {
      Editor editor = prefs.edit();
      editor.putString(key, value);
      editor.commit();
    } else {
      sharedEditor.putString(key, value);
      sharedEditor.commit();
    }
  }
  
  public String getPreferenceStringValue(String key){
    return prefs.getString(key, "20201001");
  }

  /**
   * Set a preference boolean value
   * 
   * @param key the preference key to set
   * @param value the value for this key
   */
  public void setPreferenceBooleanValue(String key, boolean value) {
    if (sharedEditor == null) {
      Editor editor = prefs.edit();
      editor.putBoolean(key, value);
      editor.commit();
    } else {
      sharedEditor.putBoolean(key, value);
      sharedEditor.commit();
    }
  }

  /***
   * 根据key获取对应的值
   */
  public boolean getPreferenceBooleanValue(String key) {
    return prefs.getBoolean(key, false);
  }

  /**
   * Set a preference int value
   * 
   * @param key the preference key to set
   * @param value the value for this key
   */
  public void setPreferenceIntValue(String key, int value) {
    if (sharedEditor == null) {
      Editor editor = prefs.edit();
      editor.putInt(key, value);
      editor.commit();
    } else {
      sharedEditor.putInt(key, value);
      sharedEditor.commit();
    }
  }

  /***
   * 根据key获取对应的值
   */
  public int getPreferenceIntValue(String key) {
    return prefs.getInt(key, 0);
  }

}
