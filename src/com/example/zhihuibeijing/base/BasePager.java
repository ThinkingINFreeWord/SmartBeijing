package com.example.zhihuibeijing.base;

import com.example.zhihuibeijing.MainActivity;
import com.example.zhihuibeijing.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;


public class BasePager {
	
	/**
	 * 主页面的五个子页面的基类
	 * 
	 * */
	public Activity mActivity;
	public View mRootvView;
	
	public TextView tvTitle;//标题栏对象
	
	public FrameLayout flContent ;//内容
	
	public ImageButton imButton;//菜单按钮
	public ImageButton btnPhoto;
		
	public BasePager(Activity activity){
		mActivity = activity;
		
		initViews();
		
	}
	/**
	 * 初始化布局
	 * */
	public void initViews() {
		mRootvView = View.inflate(mActivity, R.layout.base_pager, null);
		tvTitle = (TextView) mRootvView.findViewById(R.id.tv_title);
		flContent = (FrameLayout) mRootvView.findViewById(R.id.fl_content);
		imButton = (ImageButton) mRootvView.findViewById(R.id.btn_menu);
		 btnPhoto = (ImageButton) mRootvView.findViewById(R.id.btn_photo);
		imButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				toggleSlidingMenu();
			}
		});
	}
	/**
	 * 切换SlidingMenu的状态
	 * @param 
	 */
	private void toggleSlidingMenu() {
		// TODO Auto-generated method stub
		MainActivity mainActivity = (MainActivity) mActivity;
		SlidingMenu slidingMenu =  mainActivity.getSlidingMenu();
		slidingMenu.toggle();//设置SlidingMenu  显示时隐藏   隐藏是显示
	}
	
	/**
	 * 初始化数据
	 * */
	public void initData() {
		
	}
	public void setSlidingMenuEnable(boolean enable){
		MainActivity mainActivity = (MainActivity) mActivity;		
		SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
		if (enable) {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		} else {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);//屏蔽SlidingMenu的侧边栏效果
		}
		
	}
}
