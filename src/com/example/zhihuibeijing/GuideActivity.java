package com.example.zhihuibeijing;

import java.util.ArrayList;

import com.example.zhihuibeijing.utils.DensityUtils;
import com.example.zhihuibeijing.utils.PrefUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


public class GuideActivity extends Activity {
	
	private ViewPager vpGuidePager;
	private static final int[] imgIds = new int[]{R.drawable.guide_1,
										R.drawable.guide_2,R.drawable.guide_3};
	
	private ArrayList<ImageView> imageArrayList;

	private LinearLayout llPointGroup;//引导圆点的父控件
	
	private int mPointWidth; //圆点间的距离
	private View pointRed;
	private Button btstart;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_guide);
		llPointGroup = (LinearLayout) findViewById(R.id.ll_point_gray);
		vpGuidePager = (ViewPager) findViewById(R.id.vp_guide);
		pointRed = findViewById(R.id.point_red);
		btstart = (Button) findViewById(R.id.bt_start);
		
		initViews();
		vpGuidePager.setAdapter(new myadapter());
		vpGuidePager.setOnPageChangeListener(new GuidePageListener());
		btstart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//点击"开始体验"按钮后   在sp中写入true,使新手引导页在下次开启时不会展示
				PrefUtils.setBoolean(GuideActivity.this, "is_user_guide_showed", true);
				
				startActivity(new Intent(GuideActivity.this, MainActivity.class));
				finish();
			}
		});
	}
		
	private void initViews(){
		imageArrayList = new ArrayList<ImageView>();
		
		
		//初始化引导页的三个页面
		for (int i = 0; i < imgIds.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setBackgroundResource(imgIds[i]);
			
			imageArrayList.add(imageView);
		}
		
		//初始化 引导页的小圆点
		for (int i = 0; i < imgIds.length; i++) {//圆点数量由加载的imageView确定
			View point = new View(this);
			point.setBackgroundResource(R.drawable.shape_point_gray);//设置引导页默认圆点
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtils.dp2px(this, 10),
					DensityUtils.dp2px(this, 10));
			if (i>0) {
				params.leftMargin = DensityUtils.dp2px(this, 10);//设置小圆点之间的间隔  (判断是从第二个小圆点开始)
				
			}
			point.setLayoutParams(params);//设置引导圆点的大小
			
			llPointGroup.addView(point);//在父控件中添加point   把圆点添加给线性布局
									
		}
		//对layout结束事件进行监听
		// ViewTreeObserver  获取视图树
		llPointGroup.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
			//当Layout执行结束后回调此方法
			@SuppressWarnings("deprecation")
			@Override
			public void onGlobalLayout() {
				// TODO Auto-generated method stub
				System.out.println("layour  ----结束");
				//监听执行一次后删除,避免系统多次回调时出现多次监听事件
				llPointGroup.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				/**
				 * measure(先测量)     layout(确定界面位置)  ondrraw(进行绘制)
				 * 获取小圆点的相对距离     也就是红色小圆点的移动距离   从而实现红色圆点跟随滑动进行移动
				 *  不能直接获取的原因:初始化视图还没有layout  无法获取距离 ,通过视图树监听layout结束,从而获取距离				       
				 * */
				mPointWidth =  llPointGroup.getChildAt(1).getLeft()-llPointGroup.getChildAt(0).getLeft();
				System.out.println("小灰点间隔距离:"+mPointWidth);
			}
		}); 
	}
		
	class myadapter extends PagerAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return imageArrayList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0==arg1;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			container.addView(imageArrayList.get(position));
			return imageArrayList.get(position);
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			container.removeView((View) object);
			//super.destroyItem(container, position, object);
		}
		
	}
	
	class GuidePageListener implements OnPageChangeListener{
		
		
		//滑动状态发生变化
		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}
		//滑动事件
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {//当前位置,偏移的百分比,偏移了具体像素
			// TODO Auto-generated method stub
			System.out.println("GuideActivity.GuidePageListener.onPageScrolled()"+
								"当前位置:"+arg0+"____"+"百分比:"+arg1+"___"+"移动距离:"+arg2+"____");
			int len =	(int) (mPointWidth*arg1)+arg0*mPointWidth;
			//int len = mPointWidth*(arg0+agr1);
			System.out.println("距离:"+len);
			//获取当前小红点的布局参数
		   RelativeLayout.LayoutParams params =  (RelativeLayout.LayoutParams) pointRed.getLayoutParams();
		   //设置左边距
		   params.leftMargin = len;//
		   
		   pointRed.setLayoutParams(params);//重新给小红点设置布局参数
		}
		//某个页面被选中
		@Override
		public void onPageSelected(int arg0) {//
			// TODO Auto-generated method stub
			//设置  开始体验 按钮 的显示 时间
			if (arg0==imgIds.length-1) {
				btstart.setVisibility(View.VISIBLE);
			}else {
				btstart.setVisibility(View.INVISIBLE);
			}
		}
		
	}
	
	
}
