package com.example.zhihuibeijing.domain;

import java.util.ArrayList;




/**
 * 组图页面详情
 * @author Administrator
 *
 */

public class PhotoDate {
	
	//组图Json数据结构
	public int  retcode;
	public PhotosInfo data;
	
	//组图多图数据结构
	 public class PhotosInfo{
		 
		 public String title;
		 public ArrayList<PhotoInfo> news;
		 
	 }
	 
	 	//组图图片详细
	 	public class  PhotoInfo{
	 		public String id;
	 		public String listimage;
	 		public String title;
	 		public String type;
	 		public String url;
	 		public String pubdate;
	 	}
	
	


}
