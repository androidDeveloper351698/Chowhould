package com.cwenhui.chowhound.utils;

import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferencesHelper {
	private static SharedPreferencesHelper INSTANCE;

	private SharedPreferences preferences;

	private SharedPreferencesHelper() {
	}

	public static synchronized SharedPreferencesHelper getInstance(
			Context context) {
		if (INSTANCE == null) {
			INSTANCE = new SharedPreferencesHelper();
			INSTANCE.preferences = PreferenceManager.getDefaultSharedPreferences(context);
		}
		return INSTANCE;
	}

	/**
	 * 储存String值
	 * 
	 * @param key
	 * @param value
	 */
	public void setStringValue(String key, String value) {
		preferences.edit().putString(key, value).commit();
	}

	/**
	 * 获取String值
	 * 
	 * @param key
	 * @return
	 */
	public String getStringValue(String key) {
		return preferences.getString(key, "");
	}

	/**
	 * 获取String值
	 * 
	 * @param key
	 * @return
	 */
	public String getStringValue(String key, String devalue) {
		return preferences.getString(key, devalue);
	}

	/**
	 * 储存Boolean值
	 * 
	 * @param key
	 * @param value
	 */
	public void setBooleanValue(String key, Boolean value) {
		preferences.edit().putBoolean(key, value).commit();
	}

	/**
	 * 获取Boolean值
	 * 
	 * @param key
	 * @return
	 */
	public boolean getBooleanValue(String key) {
		return preferences.getBoolean(key, false);
	}

	/**
	 * 获取Long值
	 * 
	 * @param key
	 * @return
	 */
	public long getLongValue(String key) {
		return preferences.getLong(key, 0L);
	}

	/**
	 * 获取Boolean值
	 * 
	 * @param key
	 * @return
	 */
	public boolean getBooleanValue(String key, boolean defaultValue) {
		return preferences.getBoolean(key, defaultValue);
	}

	/**
	 * @Title: get3gValue
	 * @Description: 获取默认值为true的方法
	 * @param: @param key
	 * @param: @return
	 * @return: boolean
	 * @throws
	 */
	public boolean getBoolValue(String key) {
		return preferences.getBoolean(key, true);
	}

	/**
	 * 储存int值
	 * 
	 * @param key
	 * @param value
	 */
	public void setIntValue(String key, int value) {
		preferences.edit().putInt(key, value).commit();
	}

	/**
	 * 获取int值
	 * 
	 * @param key
	 * @return
	 */
	public int getIntValue(String key) {
		return preferences.getInt(key, 0);
	}

	public int getIntValue(String key, int value) {
		return preferences.getInt(key, value);
	}

	public void remove(String name) {
		preferences.edit().remove(name).commit();
	}

	public void setLongValue(String key, long value) {
		preferences.edit().putLong(key, value).commit();
	}

	public float getFloatValue(String key, float value) {
		return preferences.getFloat(key, value);
	}
	
	public Set<String> getStringSetValue(String key){
		return preferences.getStringSet(key, null);
	}
	public Set<String> getStringSetValue(String key, Set<String> value){
		return preferences.getStringSet(key, value);
	}
	public void setStringSetValue(String key, Set<String> value){
		preferences.edit().putStringSet(key, value).commit();
	}
}
