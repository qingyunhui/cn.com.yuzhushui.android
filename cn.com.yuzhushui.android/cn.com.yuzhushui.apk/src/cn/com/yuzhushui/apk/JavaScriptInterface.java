package cn.com.yuzhushui.apk;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

final public class JavaScriptInterface {
	private Context context;

	JavaScriptInterface(Context context) {
		this.context = context;
	}

	public String getVersion() {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			String version = info.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String clearCache(){
		Tool.clearCacheFolder(context.getCacheDir());
		return "success";
	}
	
	public void exit(){
		System.exit(0);
	}
}
