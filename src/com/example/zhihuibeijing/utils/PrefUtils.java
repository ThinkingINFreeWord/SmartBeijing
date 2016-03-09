package com.example.zhihuibeijing.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefUtils {
	
	/**
	 * SharePreference的封装
	 * 
	 * */
	
	public static final String PREF_NAME = "config";
	
	public static boolean getBoolean(Context context,String key,boolean defaultValue) {
		//判断之前有没有显示过新手引导
		SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		
		return sp.getBoolean(key, defaultValue);
		
	}
	public static void setBoolean(Context context,String key,boolean value) {
		//获取之前有没有显示过新手引导的值
		SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);				
		sp.edit().putBoolean(key, value).commit();
		
	}
	
	public static String getString(Context context,String key,String defaultValue) {
		
		SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		
		return sp.getString(key, defaultValue);
		
	}
	public static void setString(Context context,String key,String value) {
		
		SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);				
		sp.edit().putString(key, value).commit();
		
	}

}
