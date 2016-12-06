package cn.com.yuzhushui.apk;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("NewApi") 
public class MyWebViewClient extends WebViewClient {
	private ProgressDialog prDialog = null;
	
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		if(url.endsWith(".apk") || url.indexOf("download.json?id=")>0){
			Uri uri = Uri.parse(url);
            Intent intent =new Intent(Intent.ACTION_VIEW, uri);
            view.getContext().startActivity(intent);
            return true;
		}
		view.loadUrl(url);
		return true;
	}

	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
	    super.onPageStarted(view, url, favicon);
		/*if(prDialog == null){
			prDialog = ProgressDialog.show(view.getContext(), null, "正在拼命加载...");
		}
		prDialog.show();*/
 	}

	@Override
	public void onPageFinished(WebView view, String url) {
		super.onPageFinished(view, url);
		//prDialog.hide();
	}
	
	@Override
	public void onReceivedError(WebView view, int errorCode,String description, String failingUrl) {
		//view.stopLoading();
		//prDialog.hide();
		
		super.onReceivedError(view, errorCode, description, failingUrl);
		view.loadUrl(Constant.DOMAIN);
		/*if(errorCode == WebViewClient.ERROR_CONNECT || errorCode == WebViewClient.ERROR_HOST_LOOKUP){
			view.loadUrl(Constant.DOMAIN);
		}*/
	}
}
