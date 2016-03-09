package com.example.zhihuibeijing.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.zhihuibeijing.R;
/**
 * 下拉刷新的ListView
 * @author Administrator
 *
 */
public class RefreshListView extends ListView implements OnScrollListener, android.widget.AdapterView.OnItemClickListener   {

	private View mHeaderView;
	private int mHeaderViewHeight;
	private int startY=-1;//Y轴开始移动的Y轴坐标
	private int endY;
	private static final int STATE_PULL_REFRESH=0;//下拉刷新 
	private static final int STATE_RELEASE_REFRESH=1;//松开刷新 
	private static final int STATE_REFRESHING=2;//正在刷新 	
	private int mCurrentState = STATE_PULL_REFRESH;//当前状态	
	private TextView tvTitle;
	private TextView tvTime;
	private ImageView ivArrow;
	private ProgressBar pbProgress;
	
	private RotateAnimation animationUP;
	private RotateAnimation animationDOWN;
	private View mFooterView;
	private int mFooterViewHeight;
	public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initHeaderView();	
		initFooterView();
}

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initHeaderView();
		initFooterView();
	}

	public RefreshListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initHeaderView();
		initFooterView();
	}
	/**
	 * 初始化头布局
	 */
	private void initHeaderView() {
		// TODO Auto-generated method stub
		mHeaderView = View.inflate(getContext(), R.layout.refresh_header, null);
		//添加头布局
		this.addHeaderView(mHeaderView);
		
		 tvTitle =  (TextView) mHeaderView.findViewById(R.id.tv_title);
		 tvTime = (TextView) mHeaderView.findViewById(R.id.tv_time);
		 ivArrow = (ImageView) mHeaderView.findViewById(R.id.iv_arr);
		 pbProgress = (ProgressBar) mHeaderView.findViewById(R.id.pb_progress);
		
		mHeaderView.measure(0, 0);
		mHeaderViewHeight = mHeaderView.getMeasuredHeight();//获取头布局的高度
		//隐藏头布局
		mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);//设为负值,向上移动
		
		initArrow();
		tvTime.setText("最后刷新时间:"+getCurrentTime());
	}
	
	/**
	 * 初始化脚布局
	 */
	private void initFooterView() {
		mFooterView = View.inflate(getContext(), R.layout.refresh_listview_footer, null);
		this.addFooterView(mFooterView);
		
		//先测量
		mFooterView.measure(0, 0);
		//获取mFooterView的高度
		mFooterViewHeight = mFooterView.getMeasuredHeight();
		//默认隐藏
		mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);
		
		this.setOnScrollListener(this);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startY = (int) ev.getRawY();
			System.out.println("下拉刷新滑动的Y轴开始坐标:"+startY);
			break;
		case MotionEvent.ACTION_MOVE:
			if (startY==-1) {
				startY = (int) ev.getRawY();//确保startY有效
			}
			
			if (mCurrentState ==STATE_REFRESHING) {//判断:如果处于正在刷新的状态  则不会再次触发下拉刷新的状态
				break;
			}
			
			  endY = (int) ev.getRawY();//获取滑动事件结束时的Y轴坐标
			  System.out.println("下拉刷新滑动的Y轴结束坐标:"+endY);
			 int dy =   endY - startY;//获取滑动事件在Y轴的移动偏移量
			 
			 if (dy>0 && getFirstVisiblePosition() == 0) {//只有下拉并且当前是第一个item,才可以有下拉刷新的操作
				 int padding = dy - mHeaderViewHeight;//计算padding 即下拉刷新的移动
				 mHeaderView.setPadding(0, padding, 0, 0);
				if (padding>0 && mCurrentState != STATE_RELEASE_REFRESH) {//状态改为松开刷新
					mCurrentState = STATE_RELEASE_REFRESH;//
					refreshState();
				}else if (padding<0 && mCurrentState != STATE_PULL_REFRESH) {//状态改为下拉刷新
					mCurrentState = STATE_PULL_REFRESH;
					refreshState();
				}
				 return true;
			}
			
			break;
		case MotionEvent.ACTION_UP:
			startY = -1 ;//重置下拉刷新
			if (mCurrentState == STATE_RELEASE_REFRESH) {
				mCurrentState = STATE_REFRESHING;//设置为正在刷新
				mHeaderView.setPadding(0, 0, 0, 0);//设置显示
				refreshState();
			}else if (mCurrentState == STATE_PULL_REFRESH) {
				mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);//隐藏
			}
			break;

		default:
			break;
		}
		return super.onTouchEvent(ev);
	}
	/**
	 * 刷新下拉控件的布局
	 */
	private void refreshState() {
		// TODO Auto-generated method stub
		switch (mCurrentState) {
		case STATE_PULL_REFRESH:
			tvTitle.setText("下拉刷新");
			ivArrow.setVisibility(View.VISIBLE);//显示
			pbProgress.setVisibility(View.INVISIBLE);//隐藏
			ivArrow.startAnimation(animationDOWN);
			break;
		case STATE_RELEASE_REFRESH:
			tvTitle.setText("松开刷新");
			ivArrow.setVisibility(View.VISIBLE);//显示
			pbProgress.setVisibility(View.INVISIBLE);//隐藏
			ivArrow.startAnimation(animationUP);
			break;
		case STATE_REFRESHING:
			tvTitle.setText("正在刷新");
			ivArrow.clearAnimation();//必须先清除动画才能隐藏
			ivArrow.setVisibility(View.INVISIBLE);//隐藏
			pbProgress.setVisibility(View.VISIBLE);//显示
			
			if (mListener!=null) {
				mListener.onRefresh();
			}
			
			
			break;
			

		default:
			break;
		}
	}
	/**
	 * 箭头动画
	 */
	
	
	private void initArrow(){
		//箭头向下的动画
		animationUP  = new RotateAnimation(0, -180,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animationUP.setDuration(200);
		animationUP.setFillAfter(true);
		//箭头向上的动画
		animationDOWN = new RotateAnimation(-180, 0,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animationDOWN.setDuration(200);
		animationDOWN.setFillAfter(true);
	}
	
	OnRefreshListener mListener;
	
	public void setOnRefreshListener(OnRefreshListener listener){
		mListener = listener;
	}
	
	public interface OnRefreshListener{
		public void onRefresh();//下拉刷新数据
		
		public void onLoadMore();//加载下一页数据
	}
	/**
	 * 收起下拉刷新的控件
	 */
	public void onRefreshComplete(boolean success){
		if (isLoadingMore) {//正在加载更多
			mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);//
			isLoadingMore = false;
		} else {
			mCurrentState = STATE_PULL_REFRESH;
			tvTitle.setText("下拉刷新");
			ivArrow.setVisibility(View.VISIBLE);//显示
			pbProgress.setVisibility(View.INVISIBLE);//隐藏
			
			mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);//隐藏
			
			if (success) {
				tvTime.setText("最后刷新时间:"+getCurrentTime());
			}

		}
		
	}
	/**
	 * 获取当前的时间
	 * 
	 */
	public String getCurrentTime(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return  format.format(new Date());
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		
		
	}
	private boolean isLoadingMore;
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		if (scrollState==SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_FLING ) {
						
			if (getLastVisiblePosition()==getCount()-1 && !isLoadingMore) {//判断是否到当前listview的最后 一个item
				System.out.println("到底了..");
				mFooterView.setPadding(0, 0, 0, 0);//显示脚布局
				setSelection(getCount()-1);//改变lisetView的显示位置
				
				isLoadingMore = true;							
				if (mListener !=null) {
					mListener.onLoadMore();
				}
			}
		}
	}
		
	OnItemClickListener mItemClickListener;
	@Override
	public void setOnItemClickListener(
			android.widget.AdapterView.OnItemClickListener listener) {
		// TODO Auto-generated method stub
		super.setOnItemClickListener(this);
		mItemClickListener = listener;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		if (mItemClickListener!=null) {
			mItemClickListener.onItemClick(arg0, arg1, arg2-getHeaderViewsCount(), arg3);
		}
	}

}
