package com.example.zhihuibeijing.base.impl;

import java.util.ArrayList;

import com.example.zhihuibeijing.MainActivity;
import com.example.zhihuibeijing.base.BaseMenuDetailPager;
import com.example.zhihuibeijing.base.BasePager;
import com.example.zhihuibeijing.base.menudetail.InteractMenuDetailPager;
import com.example.zhihuibeijing.base.menudetail.NewsMenuDetailPager;
import com.example.zhihuibeijing.base.menudetail.PhotoMenuDetailPager;
import com.example.zhihuibeijing.base.menudetail.TopicMenuDetailPager;
import com.example.zhihuibeijing.domain.NewsData;
import com.example.zhihuibeijing.domain.NewsData.NewsMenuData;
import com.example.zhihuibeijing.fragment.LeftMenuFragment;
import com.example.zhihuibeijing.global.GlobalContants;
import com.example.zhihuibeijing.utils.CacheUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

/**
 * 
 * 新闻中心
 * */
public class NewsCenterPager extends BasePager {
	
	private ArrayList<BaseMenuDetailPager> mPagers;//4个菜单详情页面的集合
	
	private NewsData mNewsData;

	private String cache;

	public NewsCenterPager(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		
		tvTitle.setText("新闻");
		setSlidingMenuEnable(true);//开启SlidingMenu的侧边栏效果
//		TextView tView = new TextView(mActivity);
//		tView.setText("新闻中心");
//		tView.setTextColor(Color.RED);
//		tView.setTextSize(27);		
//		//在frameLayout中动态添加布局控件
//		flContent.addView(tView);
		
		//获取缓存
		cache = CacheUtils.getCache(GlobalContants.CATRGORIES_URL, mActivity);	
		//进行判断  如果有缓存则读取缓存
		if (!TextUtils.isEmpty(cache)) {
			parseDate(cache);
		}
		
		//不管有没有缓存  , 都从服务器获取数据,保证数据最新, 首先打开时先加载的是本地缓存的数据,同时请求服务器的数据
		getDataFromService();//网络请求数据
		
				
	}		
	/**
	 * 从服务器获取数据
	 * */
	private void getDataFromService() {
		// TODO Auto-generated method stub
		HttpUtils utils = new HttpUtils();
		
		//使用xUtils发送请求
		utils.send(HttpMethod.GET, GlobalContants.CATRGORIES_URL, new RequestCallBack<String>() {
			
			
			//请求成功
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				// TODO Auto-generated method stub
			String	result =   responseInfo.result;
			System.out.println("请求结果:"+result);
			
			parseDate(result);
			//设置缓存  在请求网络数据后,进行缓存的存储
			CacheUtils.setCache(GlobalContants.CATRGORIES_URL, result, mActivity);
				
			}
						
			//请求失败
			@Override
			public void onFailure(HttpException error, String msg) {
				// TODO Auto-generated method stub
				Toast.makeText(mActivity, "服务器链接超时,请求数据失败...请检查网络是否畅通!", Toast.LENGTH_LONG).show();
				error.printStackTrace();//打印错误日志
			}
		});
	}		
	/**
	 * 
	 * 使用Gson进行解析数据
	 * @param result
	 */
	private void parseDate(String result) {
		// TODO Auto-generated method stub
		
		Gson gson = new Gson();
		mNewsData =  gson.fromJson(result, NewsData.class);
		//System.out.println("解析的数据:"+mNewsData);
		//刷新侧边栏的数据
		MainActivity mainActivity = (MainActivity) mActivity;
		//获取leftMenuFragment
		LeftMenuFragment leftMenuFragment  =  mainActivity.getLeftMenuFragment();
		leftMenuFragment.setMenuData(mNewsData);
		
		//准备4个菜单详情页
		mPagers = new ArrayList<BaseMenuDetailPager>();
		mPagers.add(new NewsMenuDetailPager(mActivity,mNewsData.data.get(0).children));
		mPagers.add(new TopicMenuDetailPager(mActivity));
		mPagers.add(new PhotoMenuDetailPager(mActivity,btnPhoto));
		mPagers.add(new InteractMenuDetailPager(mActivity));
		
		setCurrentMenuDetailPager(0);//设置菜单详情页-为新闻中心的默认页面内容
	}
	
	/**
	 * 设置当前菜单详情页
	 */
	public void setCurrentMenuDetailPager(int postion) {
		//获取当前要显示的菜单详情页
		BaseMenuDetailPager pager = mPagers.get(postion);
		//将菜单详情页的布局设置给帧布局
		flContent.removeAllViews();//在设置之前清除所有的之前的布局
		flContent.addView(pager.mRootView);
		//设置当前页的标题
		 NewsMenuData menuData =  mNewsData.data.get(postion);
		tvTitle.setText(menuData.title);
		
		pager.initData();//初始化当前页面的数据
		
		if (pager instanceof PhotoMenuDetailPager) {
			btnPhoto.setVisibility(View.VISIBLE);//判断当在组图页面时,该按钮显示
			
		}else {
			btnPhoto.setVisibility(View.GONE);
		}
	}
	
		
}
