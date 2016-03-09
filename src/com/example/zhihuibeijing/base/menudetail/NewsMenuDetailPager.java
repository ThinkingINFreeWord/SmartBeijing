package com.example.zhihuibeijing.base.menudetail;

import java.util.ArrayList;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import com.example.zhihuibeijing.MainActivity;
import com.example.zhihuibeijing.R;
import com.example.zhihuibeijing.base.BaseMenuDetailPager;
import com.example.zhihuibeijing.base.TabDetailPager;
import com.example.zhihuibeijing.domain.NewsData.NewsTabData;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.TabPageIndicator;
/**
 * 
 * 菜单详情页-新闻
 * @author Administrator
 *
 */
public class NewsMenuDetailPager extends BaseMenuDetailPager implements OnPageChangeListener {
	
	private ViewPager mViewPager;
	private ArrayList<TabDetailPager> mPagerList;
	private ArrayList<NewsTabData> mNewsTabDatas;//页签的网络数据
	private TabPageIndicator indicator;


	public NewsMenuDetailPager(Activity activity, ArrayList<NewsTabData> children) {
		super(activity);
		// TODO Auto-generated constructor stub
		mNewsTabDatas = children;
	}
	@Override
	public View initViews() {
		View view  = View.inflate(mActivity, R.layout.news_menu_detail, null);
		mViewPager  = (ViewPager) view.findViewById(R.id.vp_menu_detail);
		
        ViewUtils.inject(this, view);
		//初始化自定义控件
        indicator = (TabPageIndicator)view.findViewById(R.id.indicator);
        //ViewPager滑动监听
       // mViewPager.setOnPageChangeListener(this);
       //当ViewPager和Indicator绑定时  , 该滑动监听事件需要设置给Indicator,而不是设给ViewPager 
       indicator.setOnPageChangeListener(this);
		// TODO Auto-generated method stub

		return view;
	}
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		mPagerList = new ArrayList<TabDetailPager>();
		
		for (int i = 0; i < mNewsTabDatas.size(); i++) {
			TabDetailPager pager = new TabDetailPager(mActivity,mNewsTabDatas.get(i));
			mPagerList.add(pager);
		}
		mViewPager.setAdapter(new MenuDetailAdapter());
		
		//将ViewPager和indicator关联起来
		indicator.setViewPager(mViewPager);//必须在ViewPager设置完Adapter后才能调用
	}
	/**
	 *imgButton的点击事件.
	 *点击跳转下一个页面
	 * @param view
	 */
	@OnClick(R.id.btn_next)
	public void nextPage(View view) {		
	  int currentItem = mViewPager.getCurrentItem();	  
	  mViewPager.setCurrentItem(currentItem+1);
	  
	}
	
	/**
	 * mViewPager的适配器
	 * @author Administrator
	 *
	 */
	class MenuDetailAdapter extends PagerAdapter{
		/**
		 * 重写此方法,用于viewPagerIndicator的页签显示
		 * Title
		 */
		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return mNewsTabDatas.get(position).title;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mPagerList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0==arg1;
		}
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			TabDetailPager pager = mPagerList.get(position);
			container.addView(pager.mRootView);
			pager.initData();
			return pager.mRootView;
		}		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			container.removeView((View) object);
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		MainActivity mainActivity = (MainActivity) mActivity;
		SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
		//进行判断 
		if (arg0==0) {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		} else {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
	}

}
