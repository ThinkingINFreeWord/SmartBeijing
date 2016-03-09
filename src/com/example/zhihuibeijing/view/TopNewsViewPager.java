package com.example.zhihuibeijing.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


/**
 * 头条新闻的ViewPager
 * @author Administrator
 *
 */
public class TopNewsViewPager extends ViewPager {
	int startX;
	int startY;
	

	public TopNewsViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public TopNewsViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
    /**
     * 事件分发    请求上层控件不要拦截事件
     * 1. 右滑 而且是第一个页面,需要父控件进行拦截
     * 2. 左滑,而且是最后一个页面,需要父控件拦截
     * 3. 上下滑动,需要父控件拦截
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
    	
    	
    	// TODO Auto-generated method stub
    	//用getParent去请求
    	/**
    	 * 通过计算滑动事件的开始与结束的XY轴坐标,进行手势的判断,从而针对做出不同的处理
    	 */
   		
    		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				//为了保证ACTION_MOVE方法的调用
				getParent().requestDisallowInterceptTouchEvent(true);//请求不拦截
				
			 startX = (int) ev.getRawX();//获取开始时X轴坐标
			 startY = (int) ev.getRawY();//获取开始时Y轴坐标
				
				break;
			case MotionEvent.ACTION_MOVE:
				int endX = (int) ev.getRawX();//获取结束时X轴坐标
				int endY = (int) ev.getRawY();//获取结束时Y轴坐标
				if (Math.abs(endX-startX)>Math.abs(endY-startY)) {//判断滑动手势  ,该状态下是左右滑动
					if (endX>startX) {//右滑
						if (getCurrentItem()==0) {//第一个页面,需要父控件拦截
							getParent().requestDisallowInterceptTouchEvent(false);//拦截
						}
						
					}else {//左滑
						if (getCurrentItem()==getAdapter().getCount()-1) {//判断为最后一张,需要拦截
							getParent().requestDisallowInterceptTouchEvent(false);
							
						}
					}
					
				}else {//上下滑动
					getParent().requestDisallowInterceptTouchEvent(false);//拦截
				}
				break;

				

			default:
				break;
			}

    	return super.dispatchTouchEvent(ev);
    }

}
