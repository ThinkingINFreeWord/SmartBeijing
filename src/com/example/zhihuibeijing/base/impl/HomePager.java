package com.example.zhihuibeijing.base.impl;

import com.example.zhihuibeijing.base.BasePager;
import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;


/**
 * 
 * 首页页面
 * */
public class HomePager extends BasePager {

	public HomePager(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		
		tvTitle.setText("智慧北京");
		imButton.setVisibility(View.GONE);//隐藏菜单
		setSlidingMenuEnable(false);//屏蔽SlidingMenu的侧边栏效果
		
		TextView tView = new TextView(mActivity);
		tView.setText("首页");
		tView.setTextColor(Color.RED);
		tView.setTextSize(27);
		
		//在frameLayout中动态添加布局控件
		flContent.addView(tView);
	}
	

}
