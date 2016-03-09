package com.example.zhihuibeijing.base.menudetail;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.zhihuibeijing.base.BaseMenuDetailPager;
/**
 * 
 * 菜单详情页-专题
 * @author Administrator
 *
 */
public class TopicMenuDetailPager extends BaseMenuDetailPager {

	public TopicMenuDetailPager(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View initViews() {
		// TODO Auto-generated method stub
		TextView text = new TextView(mActivity);
		text.setText("我是马云,买买买!");
		text.setTextColor(Color.RED);
		text.setTextSize(25);
		text.setGravity(Gravity.CENTER);
		return text;
	}

}
