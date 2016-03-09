package com.example.zhihuibeijing;

import com.example.zhihuibeijing.utils.PrefUtils;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

public class SplashActivity extends Activity {
	
	private RelativeLayout rlRoot;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		rlRoot = (RelativeLayout) findViewById(R.id.rl_root);
		
		startAnim();
	}		
	/**
	 * 开启动画
	 * */
	private void startAnim(){
		
		//动画集合
		AnimationSet set = new AnimationSet(false);
		
		//旋转动画
		RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF,
							0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		
		rotateAnimation.setDuration(1000);//动画持续时间
		rotateAnimation.setFillAfter(true);//保持动画后的状态
		
		//rlRoot.startAnimation(rotateAnimation);//指定开启动画的控件
		
		//缩放动画
		ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF,
							0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		
		scaleAnimation.setDuration(1000);//动画持续时间
		scaleAnimation.setFillAfter(true);//保持动画状态
		
		//rlRoot.startAnimation(scaleAnimation);
		
		
		//渐变动画  (淡入淡出)
		AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);//0完全透明  1完全不透明
		
		alphaAnimation.setDuration(1000);
		alphaAnimation.setFillAfter(true);
		
		set.addAnimation(scaleAnimation);
		set.addAnimation(rotateAnimation);
		set.addAnimation(alphaAnimation);
		//设置动画监听
		set.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			//监听到动画结束
			//动画结束
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				jumpNextPage();
			}
		});
		
		rlRoot.startAnimation(set);// 开启动画效果
			
	}
	
	private void jumpNextPage(){
		//动画结束后跳转页面
		//判断新手引导页之前是否显示过
		boolean guideValue = PrefUtils.getBoolean(SplashActivity.this, "is_user_guide_showed",
													false);
		if (!guideValue) {
			//默认为false  ,即没有展示过,动画结束后跳转到新手引导页面
			startActivity(new Intent(SplashActivity.this, GuideActivity.class));//跳转到新手引导页			
		}else {
			startActivity(new Intent(SplashActivity.this, MainActivity.class));//跳转到新手引导页
			
		}
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}

}
