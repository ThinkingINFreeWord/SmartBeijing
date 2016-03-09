package com.example.zhihuibeijing.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Fragment基类
 * @author Administrator
 *
 */
public abstract class BaseFragment extends Fragment {
	
	
	public Activity mActivity;
	
	//fragment创建 (Activity对象已存在)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mActivity = getActivity(); 
	}
	
	//处理fragment的布局
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return initViews();
	}
	//依附activity创建完成
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
		
		initDate();
	}
	//子类必须实现初始化布局的方法     抽象方法
	public abstract View initViews();
	
	//初始化数据,可以不实现
	public void initDate() {
		
	}

}

