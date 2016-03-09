package com.example.zhihuibeijing.utils;

import android.content.Context;

public class DensityUtils {
	
	/**
	 * dp转px
	 * @param context
	 * @param dp
	 * @return
	 */
	public static int dp2px(Context context,float dp){
		float density = context.getResources().getDisplayMetrics().density;//获取屏幕密度
		int px = (int) (dp*density+0.5f);
		return px;
		
	}
	/**
	 * px转dp
	 * @param context  
	 * @param px
	 * @return
	 */
	public static float px2dp(Context context,int px){
		
		float density = context.getResources().getDisplayMetrics().density;//获取屏幕密度
		
		float dp = px/density;
		 
		return dp;
		
		
	}

}
