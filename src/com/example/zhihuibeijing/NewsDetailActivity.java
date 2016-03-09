package com.example.zhihuibeijing;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
/**
 * 新闻详情页面
 * @author Administrator
 *
 */
public class NewsDetailActivity extends Activity implements OnClickListener {
	
	private ImageButton btnback;
	private ImageButton btnSize;
	private ImageButton btnshare;
	private WebView mWebView;
	private String url;
	private WebSettings settings;
	private ProgressBar pbprProgressBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_news_detail);
		
		 mWebView = (WebView) findViewById(R.id.wv_web);
		 btnback = (ImageButton) findViewById(R.id.btn_back);
		 btnSize = (ImageButton) findViewById(R.id.btn_textsize);
		 btnshare = (ImageButton) findViewById(R.id.btn_share);
		 pbprProgressBar = (ProgressBar) findViewById(R.id.pb_progress);
		 
		 btnback.setOnClickListener(this);
		 btnSize.setOnClickListener(this);
		 btnshare.setOnClickListener(this);
		 
		 //取到传递过来的url,然后对url进行解析   获取新闻的详情
		 url = getIntent().getStringExtra("url");
		 
		 settings = mWebView.getSettings();
		 settings.setJavaScriptEnabled(true);//表示支持JS,默认不支持
		 settings.setBuiltInZoomControls(true);//添加放大缩小的快捷按键
		 settings.setUseWideViewPort(true);//支持双击缩放或者放大
		 mWebView.loadUrl(url);//
		 
		 /**
		  * WebView的监听事件
		  */
		 mWebView.setWebViewClient(new WebViewClient(){			 
			 /**
			  * 网页开始加载
			  */
			 @Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);
				System.out.println("网页开始加载...");
				pbprProgressBar.setVisibility(View.VISIBLE);//加载时ProgressBar显示
			}			 
			 /**
			  * 网页加载 结束
			  */
			 @Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				System.out.println("网页加载结束...");
				pbprProgressBar.setVisibility(View.GONE);//网页加载完成时ProgressBar隐藏
			}
			 			 
		 });
		 
		 
		 mWebView.setWebChromeClient(new WebChromeClient(){
			 /**
			  * 获取网页加载进度
			  */
			 @Override
			public void onProgressChanged(WebView view, int newProgress) {
				// TODO Auto-generated method stub
				super.onProgressChanged(view, newProgress);
			}
			 
			 /**
			  * 获取网页标题
			  */
			 @Override
			public void onReceivedTitle(WebView view, String title) {
				// TODO Auto-generated method stub
				super.onReceivedTitle(view, title);
			}
			 
		 });
		 
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_textsize:
			showChooseDialog();
			break;
		case R.id.btn_share:
			showShare();//第三方分享
			break;
			

		default:
			break;
		}
	}
	private int mCurrentChooseItem;//记录当前选择的item  点击确定时的item  which
	private int mCurrentItem = 2;//记录当前选择的item 点击确定后的item 

	/**
	 * 显示选择的对话框
	 */
	private void showChooseDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		String[] items = new String[]{"超大号字体","大号字体","正常字体","小号字体","超小号字体"};
		
		builder.setTitle("字体设置");
		builder.setSingleChoiceItems(items, mCurrentItem, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				mCurrentChooseItem = which;
			}
		});
		
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				switch (mCurrentChooseItem) {
				case 0:
					settings.setTextSize(TextSize.LARGEST);
					break;
				case 1:
					settings.setTextSize(TextSize.LARGER);
					break;
				case 2:
					settings.setTextSize(TextSize.NORMAL);
					break;
				case 3:
					settings.setTextSize(TextSize.SMALLER);
					break;
				case 4:
					settings.setTextSize(TextSize.SMALLEST);
					break;

				default:
					break;
				}
				//把确定之后的item值给mCurrentItem,达到记录选择的item
				mCurrentItem = mCurrentChooseItem;
			}
		});
		builder.setNegativeButton("取消", null);
		builder.show();
	}
	/**
	 * 第三方分享
	 */
	private void showShare() {
		 ShareSDK.initSDK(this);
		 OnekeyShare oks = new OnekeyShare();
		 //关闭sso授权
		 oks.disableSSOWhenAuthorize(); 
		 
		// 分享时Notification的图标和文字
		 oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
		 // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		 oks.setTitle(getString(R.string.share));
		 // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		 oks.setTitleUrl("http://sharesdk.cn");
		 // text是分享文本，所有平台都需要这个字段
		 oks.setText("我是分享文本");
		 // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		 oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
		 // url仅在微信（包括好友和朋友圈）中使用
		 oks.setUrl("http://sharesdk.cn");
		 // comment是我对这条分享的评论，仅在人人网和QQ空间使用
		 oks.setComment("我是测试评论文本");
		 // site是分享此内容的网站名称，仅在QQ空间使用
		 oks.setSite(getString(R.string.app_name));
		 // siteUrl是分享此内容的网站地址，仅在QQ空间使用
		 oks.setSiteUrl("http://sharesdk.cn");
		 
		// 启动分享GUI
		 oks.show(this);
		 }
	
	

}
