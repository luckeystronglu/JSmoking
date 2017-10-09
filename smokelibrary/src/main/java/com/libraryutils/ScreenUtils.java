package com.libraryutils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;

public class ScreenUtils {

	// 取屏幕密度
	public static float getScreenDensity(Activity context) {

		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.density;
	}

	// 取屏幕高度
	public static int getScreenHeight(Activity context) {

		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}
		
	@SuppressWarnings("deprecation")
	public static int getDisplayHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }

	// 取屏幕宽度
	public static int getScreenWidth(Activity context) {

		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	@SuppressWarnings("deprecation")
	public static int getDisplayWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }
	// 隱藏title
	public static void hideTitle(Activity context) {
		context.requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	// 是否全屏
	public static void makeFullScreen(Activity context, Boolean isFullScreen) {
		if (isFullScreen) {
			context.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		} else {
			context.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		}
	}

	// 状态栏的高度
	public static int getStatusBarHeight(Activity context) {

		Rect mainFrame = new Rect();
		context.getWindow().getDecorView().getWindowVisibleDisplayFrame(mainFrame);
		return mainFrame.top;
	}
	
	public static int getStatusBarHeight(Context context){
		int statusBarHeight = 0;
		try {
			Class<?> cls = Class.forName("com.android.internal.R$dimen");
		    Object obj = cls.newInstance();
		    Field field = cls.getField("status_bar_height");
		    int value = Integer.parseInt(field.get(obj).toString());
		    statusBarHeight = context.getResources().getDimensionPixelSize(value);
		} catch (Exception e1) {
		    e1.printStackTrace();
		} 
		return statusBarHeight;
	}

	public static float dp2px(Context context, float dp) {
        if (context == null) {
            return -1;
        }
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static float px2dp(Context context, float px) {
        if (context == null) {
            return -1;
        }
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static int dp2pxInt(Context context, float dp) {
        return (int)(dp2px(context, dp) + 0.5f);
    }

    public static int px2dpInt(Context context, float px) {
        return (int)(px2dp(context, px) + 0.5f);
    }
    public static Bitmap takeScreenShot(Activity activity) { 
    	
    	// 获取windows中最顶层的view
    	View view = activity.getWindow().getDecorView();
    	view.buildDrawingCache();
    	// 获取状态栏高度
    	Rect rect = new Rect();
    	view.getWindowVisibleDisplayFrame(rect);
//    	int statusBarHeights = rect.top;
    	int statusBarHeights = 0;
    	Display display = activity.getWindowManager().getDefaultDisplay();
    	// 获取屏幕宽和高
    	int widths = display.getWidth();
    	int heights = display.getHeight();
    	// 允许当前窗口保存缓存信息
    	view.setDrawingCacheEnabled(true);
    	// 去掉状态栏
    	Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache(), 0,
    	statusBarHeights, widths, heights-statusBarHeights);
    	// 销毁缓存信息
    	view.destroyDrawingCache();
    	return bmp;
    } 
  
    private static void savePic(Bitmap b, File filePath) { 
        FileOutputStream fos = null; 
        try { 
            fos = new FileOutputStream(filePath); 
            if (null != fos) { 
                b.compress(Bitmap.CompressFormat.PNG, 100, fos); 
                fos.flush(); 
                fos.close(); 
            } 
        } catch (FileNotFoundException e) { 
            // e.printStackTrace(); 
        } catch (IOException e) { 
            // e.printStackTrace(); 
        } 
    } 
  
    public static void shoot(Activity a, File filePath) { 
        if (filePath == null) { 
            return; 
        } 
        if (!filePath.getParentFile().exists()) { 
            filePath.getParentFile().mkdirs(); 
        } 
        ScreenUtils.savePic(ScreenUtils.takeScreenShot(a), filePath); 
    } 
}
