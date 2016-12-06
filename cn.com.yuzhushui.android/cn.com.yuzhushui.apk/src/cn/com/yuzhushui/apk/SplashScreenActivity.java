package cn.com.yuzhushui.apk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import cn.com.movie.apk.R;

public class SplashScreenActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		// 闪屏的核心代码
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				System.out.print("run...");
				Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);  //从启动动画ui跳转到主ui
				startActivity(intent);
				SplashScreenActivity.this.finish();    // 结束启动动画界面
			}
		}, 3000);    //启动动画持续3秒钟
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}
}