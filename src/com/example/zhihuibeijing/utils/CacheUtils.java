package com.example.zhihuibeijing.utils;

import android.content.Context;


/**
 * 缓存工具类
 * @author Administrator
 *缓存Json数据
 *其中key是url,Json是value
 */
public class CacheUtils {
	
	/**
	 * 设置缓存
	 */
	public static void setCache(String key,String value,Context context) {
		PrefUtils.setString(context, key, value);
	}
	
	/**
	 * 获取缓存
	 */
	public static String getCache(String key,Context context) {
		return PrefUtils.getString(context, key, null);
		
	}
	
	

}
