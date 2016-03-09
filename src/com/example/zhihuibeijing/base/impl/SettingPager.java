package com.example.zhihuibeijing.base.impl;

import com.example.zhihuibeijing.base.BasePager;
import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;


/**
 * 
 * 设置页面
 * */
public class SettingPager extends BasePager {

	public SettingPager(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		
		tvTitle.setText("设置");
		imButton.setVisibility(View.GONE);//隐藏菜单
		setSlidingMenuEnable(false);//屏蔽SlidingMenu的侧边栏效果
		TextView tView = new TextView(mActivity);
		tView.setText("设置页面");
		tView.setTextColor(Color.RED);
		tView.setTextSize(27);
		
		//在frameLayout中动态添加布局控件
		flContent.addView(tView);
	}


}
