package cn.com.yuzhushui.apk;

import java.io.File;

public class Tool {
	public static void clearCacheFolder(File dir) {
    	if (dir!= null && dir.isDirectory()) {
    		try {
    			for (File child:dir.listFiles()) {
    				if (child.isDirectory()) {
    					clearCacheFolder(child);
    				}
    				child.delete();
    			}
    		} catch(Exception e) {   
    			e.printStackTrace();
    		}
    	}
    }
}
