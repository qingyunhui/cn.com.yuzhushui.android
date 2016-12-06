package cn.com.yuzhushui.apk;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import cn.com.movie.apk.R;

@SuppressLint("JavascriptInterface") 
public class MainActivity extends Activity {
	private WebView webview;
	private ValueCallback<Uri> mUploadMessage;
	private final static int FILECHOOSER_RESULTCODE = 1;
	private SharedPreferences preferences;  
	private Editor editor; 
	
    @SuppressWarnings("deprecation")
	@SuppressLint("SetJavaScriptEnabled") 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webview = (WebView) findViewById(R.id.webView);
        WebSettings ws = webview.getSettings();
     	ws.setJavaScriptEnabled(true);
     	ws.setAllowFileAccess(true);
     	//ws.setSupportZoom(true);//支持放大缩小...
     	//ws.setBuiltInZoomControls(true);
     	ws.setCacheMode(WebSettings.LOAD_NO_CACHE);
     	ws.setDomStorageEnabled(true);
     	ws.setDatabaseEnabled(true);
     	ws.setPluginState(PluginState.ON);
     	ws.setUseWideViewPort(true);
     	ws.setLoadWithOverviewMode(true);
     	ws.setRenderPriority(RenderPriority.HIGH);
     	ws.setSavePassword(false);
     	ws.setDefaultTextEncodingName("utf-8");
     	
     	webview.requestFocus();
     	webview.setHorizontalScrollBarEnabled(false);//水平不显示
     	webview.setVerticalScrollBarEnabled(false); //垂直不显示
     	webview.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);//滚动条在WebView内侧显示
     	webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);//滚动条在WebView外侧显示
     	
        webview.setWebViewClient(new MyWebViewClient());
        webview.setWebChromeClient(new MyWebChromeClient());
        webview.addJavascriptInterface(new JavaScriptInterface(this.getApplicationContext()), "webview");
        
        preferences = getSharedPreferences("phone", Context.MODE_PRIVATE);  
        //判断是不是首次登录，  
        if (preferences.getBoolean("firststart", true)) {  
	         editor = preferences.edit();  
	         //将登录标志位设置为false，下次登录时不在显示首次登录界面  
	         editor.putBoolean("firststart", false);  
	         editor.commit();  
	         Intent intent = new Intent();  
	         intent.setAction("android.intent.action.VIEW");  
	         Uri content_url = Uri.parse(Constant.DOMAIN);//Constant.DOMAIN 
	         intent.setData(content_url);
	         startActivity(intent);  
	         finish();  
        }  
        webview.loadUrl(Constant.LOGIN);
    }
    
    private class MyWebChromeClient extends WebChromeClient {
    	public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
    		AlertDialog.Builder b2 = new AlertDialog.Builder(webview.getContext())
    		.setTitle("提示").setMessage(message)
            .setPositiveButton("确定",
                new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                            int which) {
                        result.confirm();
                    }
                });
		    b2.setCancelable(false);  
		    b2.create();  
		    b2.show(); 
    		return true;
		}
    	
		// For Android 3.0+
		@SuppressWarnings("unused")
		public void openFileChooser(ValueCallback<Uri> uploadMsg) {
			mUploadMessage = uploadMsg;
			Intent i = new Intent(Intent.ACTION_GET_CONTENT);
			i.addCategory(Intent.CATEGORY_OPENABLE);
			i.setType("image/*");
			MainActivity.this.startActivityForResult
			(Intent.createChooser(i, "File Chooser"),FILECHOOSER_RESULTCODE);

		}
		// For Android 3.0+
		@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
		public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
			mUploadMessage = uploadMsg;
			Intent i = new Intent(Intent.ACTION_GET_CONTENT);
			i.addCategory(Intent.CATEGORY_OPENABLE);
			i.setType("*/*");
			MainActivity.this.startActivityForResult(
					Intent.createChooser(i, "File Browser"),
					FILECHOOSER_RESULTCODE);
		}
		// For Android 4.1
		@SuppressWarnings("unused")
		public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
			mUploadMessage = uploadMsg;
			Intent i = new Intent(Intent.ACTION_GET_CONTENT);
			i.addCategory(Intent.CATEGORY_OPENABLE);
			i.setType("*/*");
			MainActivity.this.startActivityForResult(
					Intent.createChooser(i, "File Chooser"),
					MainActivity.FILECHOOSER_RESULTCODE);
		}
    }
 	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if(webview.canGoBack()) {
				webview.goBack();
			}
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == FILECHOOSER_RESULTCODE) {
			if (null == mUploadMessage){
				return;
			}
			Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
			mUploadMessage.onReceiveValue(result);
			mUploadMessage = null;
		}
	}
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_exiting) {
        	AlertDialog.Builder builder = new Builder(this);
            builder.setMessage("确定退出?");
            builder.setTitle("提示");
            builder.setPositiveButton("确定",new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog,int which){
                    dialog.dismiss();
                    MainActivity.this.finish();
                    System.exit(0);
                }
            });
            builder.setNegativeButton("取消",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
            return true;
        }
        if (id == R.id.action_clear_cache) {
        	Tool.clearCacheFolder(this.getCacheDir());
        }
        return super.onOptionsItemSelected(item);
    }
}
