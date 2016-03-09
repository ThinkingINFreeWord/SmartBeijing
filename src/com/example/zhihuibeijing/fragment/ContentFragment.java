package com.example.zhihuibeijing.fragment;

import java.util.ArrayList;

import com.example.zhihuibeijing.R;
import com.example.zhihuibeijing.base.BasePager;
import com.example.zhihuibeijing.base.impl.GovmentPager;
import com.example.zhihuibeijing.base.impl.HomePager;
import com.example.zhihuibeijing.base.impl.NewsCenterPager;
import com.example.zhihuibeijing.base.impl.SettingPager;
import com.example.zhihuibeijing.base.impl.SmartServicePager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class ContentFragment extends BaseFragment {
	
	@ViewInject(R.id.tab_group)
	private RadioGroup tabGroup;
	
	@ViewInject(R.id.vp_content)
	private ViewPager vpcontent;
	
	private ArrayList<BasePager> mPagerList;

	@Override
	public View initViews() {
		// TODO Auto-generated method stub
		View view = View.inflate(mActivity, R.layout.fragment_content, null);
		//tabGroup = (RadioGroup) view.findViewById(R.id.tab_group);
		ViewUtils.inject(this, view);
		return view;
	}
	
	@Override
	public void initDate() {
		// TODO Auto-generated method stub
		
		tabGroup.check(R.id.tab_home);//设置默认选中为首页
		
		
		//初始化五个子页面
		mPagerList = new ArrayList<BasePager>();
//		for (int i = 0; i < 5; i++) {
//			BasePager pager = new BasePager(mActivity);
//			mPagerList.add(pager);
//		}
		mPagerList.add(new HomePager(mActivity));
		mPagerList.add(new NewsCenterPager(mActivity));
		mPagerList.add(new SmartServicePager(mActivity));
		mPagerList.add(new GovmentPager(mActivity));
		mPagerList.add(new SettingPager(mActivity));
		
		vpcontent.setAdapter(new contentAdapter());
		
		
		//监听RadioGroup的选择事件
		tabGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.tab_home:
					vpcontent.setCurrentItem(0,false);//首页
					break;
				case R.id.tab_news:
					vpcontent.setCurrentItem(1,false);//新闻中心
					break;
				case R.id.tab_smart:
					vpcontent.setCurrentItem(2,false);//智慧服务
					break;
				case R.id.tab_gov:
					vpcontent.setCurrentItem(3,false);//政务
					break;
				case R.id.tab_set:
					vpcontent.setCurrentItem(4,false);//设置
					break;
					

				default:
					break;
				}
			}
		});
		
		vpcontent.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				mPagerList.get(arg0).initData();//获取当前选中页面. 进行初始化该页面的数据
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		mPagerList.get(0).initData();//默认打开应用初始化首页数据
	}
	class contentAdapter extends PagerAdapter{

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
			BasePager pagers = mPagerList.get(position);
			
			container.addView(pagers.mRootvView);
			
			//pagers.initData();//初始化数据   (不能放在这里)
			return pagers.mRootvView;
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			container.removeView((View) object);
		}
	}
	
	/**
	 * 获取新闻中心页面
	 * @return
	 */
	public NewsCenterPager getNewsCenterPager() {
				
		return (NewsCenterPager) mPagerList.get(1);
		
	}

}
