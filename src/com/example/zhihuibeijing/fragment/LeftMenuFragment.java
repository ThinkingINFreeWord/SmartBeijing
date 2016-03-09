package com.example.zhihuibeijing.fragment;

import java.util.ArrayList;

import com.example.zhihuibeijing.MainActivity;
import com.example.zhihuibeijing.R;
import com.example.zhihuibeijing.base.impl.NewsCenterPager;
import com.example.zhihuibeijing.domain.NewsData;
import com.example.zhihuibeijing.domain.NewsData.NewsMenuData;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


/**
 * 侧边栏
 * */
public class LeftMenuFragment extends BaseFragment {
		
	private ListView lvlist;	
	private ArrayList<NewsMenuData> mMenuList;
	private MenuAdapter mAdapter;
	private int mCurrentPos;//当前点击的菜单
	
	@Override
	public View initViews() {	
		View view = View.inflate(mActivity, R.layout.fragmentleft_menu, null);
		lvlist = (ListView) view.findViewById(R.id.lv_list);
		return view;
	}
	
	@Override
	public void initDate() {
		lvlist.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub		
				mCurrentPos = arg2;
				mAdapter.notifyDataSetChanged();//通知适配器重新调用getView()方法
				
				setCurrentMenuDetailPager(arg2);
			}
		});
	}
	/**
	 * 设置当前详情页
	 * @param arg2
	 */
	protected void setCurrentMenuDetailPager(int arg2) {
		// TODO Auto-generated method stub
		MainActivity mainActivity = (MainActivity) mActivity;
		//获取contentFragment对象
		ContentFragment contentFragment = mainActivity.getContentFragment();
		//获取新闻中心页面
		 NewsCenterPager pager =  contentFragment.getNewsCenterPager();
		 //设置当前菜单详情
		 pager.setCurrentMenuDetailPager(arg2);
		 
		 toggleSlidingMenu();//实现效果:点击左侧侧边栏时  隐藏SlidingMenu.
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
	 * 初始化新闻侧边栏数据
	 * @param data 
	 */
	public void setMenuData(NewsData data) {
		//System.out.println("侧边栏数据:"+data);
		
		mMenuList = data.data;
		mAdapter = new MenuAdapter();
		lvlist.setAdapter(mAdapter);
	}
		
	/**
	 * 
	 * 侧边栏数据适配器
	 * @author Administrator
	 *
	 */
	class MenuAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return mMenuList.size();
		}
		@Override
		public NewsMenuData getItem(int position) {
			return mMenuList.get(position);
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(mActivity, R.layout.list_menu_item_left, null);
			
			TextView tvnewstitle =  (TextView) view.findViewById(R.id.tv_title);			
			NewsMenuData newsMenuData = getItem(position);
			tvnewstitle.setText(newsMenuData.title);
						
			if (mCurrentPos==position) {//判断当前绘制的view是否被选中
				//显示红色
				tvnewstitle.setEnabled(true);				
			}else {
				//显示baise
				tvnewstitle.setEnabled(false);				
			}
			return view;
		}
		
	}

}
