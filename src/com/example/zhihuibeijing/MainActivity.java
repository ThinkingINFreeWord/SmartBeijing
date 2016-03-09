package com.example.zhihuibeijing;

import com.example.zhihuibeijing.fragment.ContentFragment;
import com.example.zhihuibeijing.fragment.LeftMenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

public class MainActivity extends SlidingFragmentActivity  {
	
	private static final String FRAGMENT_LEFT_MENU ="fragment_left_menu";
	private static final String FRAGMENT_CONTENT ="fragment_content";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		//设置侧边栏
		setBehindContentView(R.layout.activity_left);
		SlidingMenu slidingMenu = getSlidingMenu();//获取侧边栏对象
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);//设置为全屏触摸可触发模式
		int width = getWindowManager().getDefaultDisplay().getWidth();//获取屏幕的宽度
		slidingMenu.setBehindOffset((int) (width*0.6));//设置侧边栏滑出时屏幕预留的面积(不是侧边栏的面积)
		
		initFragment();
		
	}
	
	/**
	 * 初始化fragment  ,将fragment数据填充给布局文件
	 * 
	 * */
	private void  initFragment() {
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();//获取事务
		/**
		 * 给fragment设置Tag   
		 * 在后期可以使用manager.findFragmentByTag(FRAGMENT_CONTENT)使用fragment
		 * */
		
		transaction.replace(R.id.fl_left_menu, new LeftMenuFragment(),FRAGMENT_LEFT_MENU);
		transaction.replace(R.id.fl_main, new ContentFragment(),FRAGMENT_CONTENT);
		//manager.findFragmentByTag(FRAGMENT_CONTENT)
		transaction.commit();
	}
	
	/**
	 * 获取LeftMenuFragment对象
	 */
	public LeftMenuFragment getLeftMenuFragment() {
		
		FragmentManager manager = getSupportFragmentManager();
		
		//通过设置的Tag  找到LeftMenuFragment
		LeftMenuFragment leftMenuFragment = (LeftMenuFragment) manager.findFragmentByTag(FRAGMENT_LEFT_MENU);
		return leftMenuFragment;
	}
	/**
	 * 获取ContentFragment对象
	 */
	public ContentFragment getContentFragment() {
		
		FragmentManager manager = getSupportFragmentManager();
		
		//通过设置的Tag  找到LeftMenuFragment
		ContentFragment contentFragment = (ContentFragment) manager.findFragmentByTag(FRAGMENT_CONTENT);
		return contentFragment;
	}

}
