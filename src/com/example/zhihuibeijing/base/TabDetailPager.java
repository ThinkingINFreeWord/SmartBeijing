package com.example.zhihuibeijing.base;

import java.util.ArrayList;

import com.example.zhihuibeijing.NewsDetailActivity;
import com.example.zhihuibeijing.R;
import com.example.zhihuibeijing.domain.NewsData.NewsTabData;
import com.example.zhihuibeijing.domain.TabData;
import com.example.zhihuibeijing.domain.TabData.TabNewsData;
import com.example.zhihuibeijing.domain.TabData.TopNewsData;
import com.example.zhihuibeijing.global.GlobalContants;
import com.example.zhihuibeijing.utils.CacheUtils;
import com.example.zhihuibeijing.utils.PrefUtils;
import com.example.zhihuibeijing.view.RefreshListView;
import com.example.zhihuibeijing.view.RefreshListView.OnRefreshListener;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.CirclePageIndicator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;


/**
 * 页签详情页
 * @author Administrator
 *
 */
public class TabDetailPager extends BaseMenuDetailPager implements OnPageChangeListener {
	
	private NewsTabData  mTabData;
	private String mUrl;
	private TabData mTabDetailData;
	
	@ViewInject(R.id.vp_news)
	private ViewPager mViewPager;
	
	@ViewInject(R.id.lv_list)
	private RefreshListView mListView;//新闻列表
	
	@ViewInject(R.id.tv_title)
	private TextView tvtitle;//头条新闻的标题
	private ArrayList<TopNewsData> mtopNewsList;//头条新闻的集合
	private TopNewsData topNewsData;
	
	@ViewInject(R.id.indicator)
	private CirclePageIndicator mIndicator;//头条新闻的位置指示器   即跟随头条新闻ViewPager的滑动而滑动的小圆点
	
	private ArrayList<TabNewsData> mNewsList;//新闻数据集合
	private NewsAdapter mNewsAdapter;
	private String more;
	private String mMoreUrl;
	private ArrayList<TabNewsData> news;
	private String ids;
	private String id;
	private String cache;
	private Handler mHandler;
	
		
	public TabDetailPager(Activity activity, NewsTabData newsTabData) {
		super(activity);
		// TODO Auto-generated constructor stub
		mTabData = newsTabData;
		mUrl  = GlobalContants.SERVICE_URL+mTabData.url;
	}

	@Override
	public View initViews() {
		// TODO Auto-generated method stub
		View view = View.inflate(mActivity, R.layout.tab_detailpager, null);
		
		View headerView = View.inflate(mActivity, R.layout.list_header_topnews, null);
		ViewUtils.inject(this, view);
		ViewUtils.inject(this, headerView);
		//mViewPager.setOnPageChangeListener(this);
		//为ListView添加头目
		mListView.addHeaderView(headerView);
		//设置下拉刷新监听
		mListView.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				getDataFromServer();
			}

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				if (mMoreUrl!=null) {
					getMoreDataFromServer();
				} else {
					Toast.makeText(mActivity, "已经是最后一页了!", Toast.LENGTH_SHORT).show();
					mListView.onRefreshComplete(false);//隐藏脚布局
				}
			}
		});
		/**
		 * mListView的item的点击监听  
		 */
		mListView.setOnItemClickListener(new OnItemClickListener() {

			

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				System.out.println("点击了第"+arg2);				
				ids = PrefUtils.getString(mActivity, "read_ids", "");			
				id = mNewsList.get(arg2).id;//获取当前点击的Item的新闻id
				System.out.println(id);
				System.out.println(ids);
				//在本地记录已读
				if (!ids.contains(id)) {//判断受否已经含有该id(避免多次点击后导致的sp重复记录当前item新闻的id)
					ids = ids+id+",";
					PrefUtils.setString(mActivity, "read_ids", ids);
				}
				
				changeReadState(arg1);//实现局部的数据更新,这是view就是当前点击的item的View
				//mNewsAdapter.notifyDataSetChanged();//通知Adapter更新
				
				
				//跳转新闻详情页面
				Intent intent = new Intent(mActivity, NewsDetailActivity.class);
				//把url放入
				intent.putExtra("url", mNewsList.get(arg2).url);
				mActivity.startActivity(intent);
			}
		});
				
		return view;
	}
	/**
	 * 改变已点击item新闻Title的颜色
	 * @param view
	 */
	private void changeReadState(View view) {
	  TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
	  tvTitle.setTextColor(Color.GRAY);
	}
	
	/**
	 * 加载下一页的数据
	 */
	private void getMoreDataFromServer() {
		// TODO Auto-generated method stub
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				// TODO Auto-generated method stub
				String	result =   responseInfo.result;
				System.out.println("菜单详情页:"+result);
				
				parseDate(result,true);
				
				mListView.onRefreshComplete(true);//获取数据后调用隐藏下拉刷新控件
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				// TODO Auto-generated method stub
				Toast.makeText(mActivity, "服务器链接超时,请求数据失败...请检查网络是否畅通!", Toast.LENGTH_LONG).show();
				error.printStackTrace();//打印错误日志
				mListView.onRefreshComplete(false);//获取数据失败后调用隐藏下拉刷新控件
			}
		});
	}
	
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		//tView.setText(mTabData.title);
		cache = CacheUtils.getCache(mUrl, mActivity);//获取缓存
		
		if (!TextUtils.isEmpty(cache)) {
			parseDate(cache, false);
		}
		getDataFromServer();
	}

	private void getDataFromServer() {
		// TODO Auto-generated method stub
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, mUrl, new RequestCallBack<String>() {
			
			
			//Success
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				// TODO Auto-generated method stub
				String	result =   responseInfo.result;
				System.out.println("菜单详情页:"+result);
				
				parseDate(result,false);
				
				mListView.onRefreshComplete(true);//获取数据后调用隐藏下拉刷新控件
				
				CacheUtils.setCache(mUrl, result, mActivity);//设置缓存
			}
			
			//Failure
			@Override
			public void onFailure(HttpException error, String msg) {
				// TODO Auto-generated method stub
				Toast.makeText(mActivity, "服务器链接超时,请求数据失败...请检查网络是否畅通!", 
						Toast.LENGTH_LONG).show();
				error.printStackTrace();//打印错误日志
				mListView.onRefreshComplete(false);//获取数据失败后调用隐藏下拉刷新控件
			}
		});
	}

	protected void parseDate(String result,boolean isMore) {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		mTabDetailData = gson.fromJson(result, TabData.class);
		
		//处理下一页加载的链接
		more = mTabDetailData.data.more;//取到more   more的作用
		if (!TextUtils.isEmpty(more)) {
		mMoreUrl = GlobalContants.SERVICE_URL+ more;				
		}else {
			mMoreUrl = null;
		}
		
		
		//判断是否是加载下一页
		if (!isMore) {
			mtopNewsList = mTabDetailData.data.topnews;
			mNewsList = mTabDetailData.data.news;
			
			if (mtopNewsList!=null) {
				
			//	System.out.println("页签详情解析:"+mTabDetailData);
				//给头条新闻的viewpager设置适配器
				mViewPager.setAdapter(new TopnewsAdapter());				
				mIndicator.setViewPager(mViewPager);
				mIndicator.setSnap(true);//设置为快照显示
				mIndicator.setOnPageChangeListener(this);
				mIndicator.onPageSelected(0);
				tvtitle.setText(mtopNewsList.get(0).title);
			}		
			//填充新闻列表数据
			
			if (mNewsList!=null) {
				 mNewsAdapter = new NewsAdapter();				
				mListView.setAdapter(mNewsAdapter);
			}
			
			//设置头条新闻的轮播效果
			if (mHandler==null) {
				mHandler = new Handler(){

					@Override
					public void handleMessage(Message msg) {
						// TODO Auto-generated method stub
						int currentItem = mViewPager.getCurrentItem();//获取当前的item
						if (currentItem<mtopNewsList.size()-1) {
							currentItem++;
						} else {
							currentItem=0;
						}
						mViewPager.setCurrentItem(currentItem);//设置
						//延迟发送  与外层延迟发送构成循环,实现轮播的效果
						mHandler.sendEmptyMessageDelayed(0, 3000);
					}
					
				};
				mHandler.sendEmptyMessageDelayed(0, 3000);//延迟发送
				  
			} else {

			}
			
		}else {//如果是加载下一页,需要将数据追加给原来的集合
			news = mTabDetailData.data.news;
			mNewsList.addAll(news);
			mNewsAdapter.notifyDataSetChanged();//刷新
		}
	}
	
	/**
	 * 头条新闻的ViewPager的适配器
	 * @author Administrator
	 *
	 */
	class TopnewsAdapter extends PagerAdapter{
		BitmapUtils utils;
		
		public TopnewsAdapter(){
			utils = new BitmapUtils(mActivity);
			//设置默认的加载的图片
			utils.configDefaultLoadingImage(R.drawable.topnews_item_default);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mTabDetailData.data.topnews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0==arg1;
		}
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			ImageView image = new ImageView(mActivity);
//			image.setImageResource(R.drawable.topnews_item_default);
			image.setScaleType(ScaleType.FIT_XY);//基于控件的大小填充图片
			TopNewsData topNewsData =  mTabDetailData.data.topnews.get(position);
			utils.display(image, topNewsData.topimage);//传递imageView对象和图片地址
			container.addView(image);
			
			image.setOnTouchListener(new TopNewsTouchListener());
			
			return image;
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			container.removeView((View) object);
		}
	}
	/**
	 * 头条新闻 ViewPager的触摸监听事件
	 * 
	 *
	 * @author Administrator
	 *
	 */
	class TopNewsTouchListener implements  OnTouchListener{

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mHandler.removeCallbacksAndMessages(null);//点击时,清除发送延迟发送信息
				break;
			case MotionEvent.ACTION_UP:
				mHandler.sendEmptyMessageDelayed(0, 3000);//松开时,重新发送延迟消息
				break;
				
			case MotionEvent.ACTION_CANCEL:
				mHandler.sendEmptyMessageDelayed(0, 3000);//事件消耗掉时,也重新发送延迟消息
				break;
				

			default:
				break;
			}
			
			return true;
		}
		
	}
	
	/**
	 * 新闻列表的适配器
	 * @author Administrator
	 *
	 */
	class NewsAdapter extends BaseAdapter{
		
		private BitmapUtils utils;

		public  NewsAdapter() {
			utils = new BitmapUtils(mActivity);
			utils.configDefaultLoadingImage(R.drawable.pic_item_list_default);
		}
		 
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mNewsList.size();
		}

		@Override
		public TabNewsData getItem(int position) {
			// TODO Auto-generated method stub
			return mNewsList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if (convertView==null) {
				convertView = View.inflate(mActivity, R.layout.list_news_item, null);
				holder = new ViewHolder();
				holder.ivPic = (ImageView) convertView.findViewById(R.id.iv_pager);
				holder.tvtitle = (TextView) convertView.findViewById(R.id.tv_title);
				holder.tvdata = (TextView) convertView.findViewById(R.id.tv_data);
				convertView.setTag(holder);
			}else {
				holder = (ViewHolder) convertView.getTag();
			}			
			TabNewsData item = getItem(position);
			
			holder.tvtitle.setText(item.title);
			holder.tvdata.setText(item.pubdate);
			
			utils.display(holder.ivPic, item.listimage);
//			System.out.println("是否解析到日期:"+item.pubdate);
//			System.out.println("是否解析到Title:"+item.title);
//			System.out.println("是否解析到数据:"+item.url);
			
			ids = PrefUtils.getString(mActivity, "read_ids", "");
			
			if (ids.contains(getItem(position).id)) {//判断ids是否包含当前item的新闻id
				holder.tvtitle.setTextColor(Color.GRAY);//如果是已包含的新闻id,则设为灰色				
			}else {
				holder.tvtitle.setTextColor(Color.BLACK);//如果是没有记录的id,则不设,默认为黑色?	
			}
			return convertView;
		}
		
	}
	/**
	 * ViewHolder  ListView的优化
	 * @author Administrator
	 *
	 */
	static class ViewHolder {
		public TextView tvtitle;
		public TextView tvdata;
		public ImageView ivPic;
		
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
		 topNewsData = mtopNewsList.get(arg0);
		 tvtitle.setText(topNewsData.title);
	}
	

}
