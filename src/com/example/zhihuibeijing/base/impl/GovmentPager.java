package com.example.zhihuibeijing.base.impl;

import com.example.zhihuibeijing.base.BasePager;

import android.app.Activity;
import android.graphics.Color;
import android.widget.TextView;



/**
 * 
 * 政务页面
 * */
public class GovmentPager extends BasePager {

	public GovmentPager(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		
		tvTitle.setText("人口管理");
		
		setSlidingMenuEnable(true);//开启SlidingMenu的侧边栏效果
		TextView tView = new TextView(mActivity);
		tView.setText("政务");
		tView.setTextColor(Color.RED);
		tView.setTextSize(27);
		
		//在framelayout中动态添加布局控件
		flContent.addView(tView);
	}

}
