package com.example.zhihuibeijing.base.menudetail;

import java.util.ArrayList;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhihuibeijing.R;
import com.example.zhihuibeijing.base.BaseMenuDetailPager;
import com.example.zhihuibeijing.domain.PhotoDate;
import com.example.zhihuibeijing.domain.PhotoDate.PhotoInfo;
import com.example.zhihuibeijing.global.GlobalContants;
import com.example.zhihuibeijing.utils.CacheUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
/**
 * 
 * 菜单详情页-组图
 * @author Administrator
 *
 */
public class PhotoMenuDetailPager extends BaseMenuDetailPager {
	
	
	private ListView lvphoto;
	private GridView glphoto;
	private String cache;
	private PhotoDate photoDate;
	private ArrayList<PhotoInfo> mPhotoList;
	private PhotoAdapter mAdapter;
	private BitmapUtils utils;
	private ImageButton btnPhoto;
	
	public PhotoMenuDetailPager(Activity activity, ImageButton btnPhoto) {
		super(activity);
		// TODO Auto-generated constructor stub
		this.btnPhoto = btnPhoto;
		btnPhoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ChangeDisPlay();
			}
		});
	}
	
	private boolean isListDisplay = true ;
	/**
	 * 切换展示组图的模式
	 */
	protected void ChangeDisPlay() {
		// TODO Auto-generated method stub
		
		if (isListDisplay) {//判断 当前展示模式,默认为列表展示,点击图标后,切换展示模式,设isListDisplay为false
			isListDisplay = false ;
			lvphoto.setVisibility(View.GONE);
			glphoto.setVisibility(View.VISIBLE);
			btnPhoto.setImageResource(R.drawable.icon_pic_list_type);
		} else {
			isListDisplay = true ;
			lvphoto.setVisibility(View.VISIBLE);
			glphoto.setVisibility(View.GONE);
			btnPhoto.setImageResource(R.drawable.icon_pic_grid_type);
		}
		
	}

	@Override
	public View initViews() {
		// TODO Auto-generated method stub
		View view = View.inflate(mActivity, R.layout.menu_photo_pager, null);
		lvphoto = (ListView) view.findViewById(R.id.lv_photo);
		glphoto = (GridView) view.findViewById(R.id.gl_photo);
		return view;
	}
	
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		cache = CacheUtils.getCache(GlobalContants.PHOTOS_URL, mActivity);
		if (!TextUtils.isEmpty(cache)) {
			parseDate(cache);
		}
		getDataFromService();//网络请求数据
	}
	
	private void getDataFromService() {
		// TODO Auto-generated method stub
		HttpUtils utils = new HttpUtils();
		
		utils.send(HttpMethod.GET, GlobalContants.PHOTOS_URL, new RequestCallBack<String>() {

			private String result;

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				// TODO Auto-generated method stub
				result = responseInfo.result;
				System.out.println("请求结果:"+result);
				
				parseDate(result);
				//设置缓存  在请求网络数据后,进行缓存的存储
				CacheUtils.setCache(GlobalContants.PHOTOS_URL, result, mActivity);
				
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				// TODO Auto-generated method stub
				Toast.makeText(mActivity, "服务器链接超时,请求数据失败...请检查网络是否畅通!", 
						Toast.LENGTH_LONG).show();
				error.printStackTrace();//打印错误日志
			}
						
		});
	}

	private void parseDate(String result) {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		photoDate = gson.fromJson(result,PhotoDate.class );
		
		mPhotoList = photoDate.data.news;//获取组图集合列表
		
		mAdapter = new PhotoAdapter();
		lvphoto.setAdapter(mAdapter);
		glphoto.setAdapter(mAdapter);
	}

	/**
	 * Photo ListView 的适配器
	 * @author Administrator
	 *
	 */
	class PhotoAdapter extends BaseAdapter{
		
		public PhotoAdapter(){
			utils  = new BitmapUtils(mActivity);
			utils.configDefaultLoadFailedImage(R.drawable.news_pic_default);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mPhotoList.size();
		}

		@Override
		public PhotoInfo getItem(int position) {
			// TODO Auto-generated method stub
			return mPhotoList.get(position);
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
				convertView = View.inflate(mActivity, R.layout.list_photo_item,	 null);
				holder = new ViewHolder();
				holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
				holder.ivPic = (ImageView) convertView.findViewById(R.id.iv_pic);	
				
				convertView.setTag(holder);
			}else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			PhotoInfo item = getItem(position);
			
			holder.tvTitle.setText(item.title);
			
			utils.display(holder.ivPic, item.listimage);
			return convertView;
		}
					
	}
	
	public class ViewHolder {
		public TextView tvTitle;
		public ImageView ivPic;
	}

}
