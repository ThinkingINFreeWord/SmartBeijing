package com.example.zhihuibeijing.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


/**
 * 11个子页签的水平滑动的ViewPager
 * 由于后续的滑动事件冲突  不用
 * @author Administrator
 *
 */
public class HorizontalViewPager extends ViewPager {

	public HorizontalViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public HorizontalViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
    /**
     * 事件分发    请求上层控件不要拦截事件
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
    	// TODO Auto-generated method stub
    	//用getParent去请求
    	//进行判断   当在item不为0即不在北京页签下  往右侧滑动  不可以滑出右侧侧边栏
    	if (getCurrentItem()!=0) {
    		getParent().requestDisallowInterceptTouchEvent(true);//请求不拦截
		}else {
			//当在item为0即北京页签下  往右侧滑动  可以滑出右侧侧边栏
			getParent().requestDisallowInterceptTouchEvent(false);//拦截
		}
    	
    	return super.dispatchTouchEvent(ev);
    }

}
