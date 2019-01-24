package com.yk.customsdk.customlibiary;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;



public class DensityUtils {

	private static boolean isFullScreen = false;
	private static DisplayMetrics dm = null;

	private DensityUtils() {
		// 不能被实例化
		new UnsupportedOperationException("cannot be instantiated");
	}

	/**
	 * dp转px
	 * 
	 * @param context
	 * @param dpVal
	 * @return
	 */
	public static int dp2px(Context context, float dpVal) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dpVal, context.getResources().getDisplayMetrics());
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
	/**
	 * sp转px
	 * 
	 * @param context
	 * @param spVal
	 * @return
	 */
	public static int sp2px(Context context, float spVal) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
				spVal, context.getResources().getDisplayMetrics());
	}

	public static boolean isOver600dp(Context context) {
		Resources resources = context.getResources();
		DisplayMetrics displayMetrics = resources.getDisplayMetrics();
		return displayMetrics.widthPixels / displayMetrics.density >= 600;
	}
	/**
	 * px转dp
	 * 
	 * @param context
	 * @param pxVal
	 * @return
	 */
	public static float px2dp(Context context, float pxVal) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (pxVal / scale);
	}

	/**
	 * px值转换成dp值
	 *
	 * @param pxValue px值
	 * @return dp值
	 */
	public static int px2dip(Context context, final float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * px转sp
	 * 
	 * @param context
	 * @param pxVal
	 * @return
	 */
	public static float px2sp(Context context, float pxVal) {
		return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
	}
	/**
	 * 获取屏幕分辨率
	 *
	 * @param context
	 * @return
	 */
	public static String getPhoneSize(final Context context) {
		DisplayMetrics dm;
		dm = context.getResources().getDisplayMetrics();

       /* int densityDPI = dm.densityDpi; // 屏幕密度（每寸像素：120/160/240/320）
        float xdpi = dm.xdpi;
        float ydpi = dm.ydpi;*/
		int screenWidth = dm.widthPixels; // 屏幕宽（像素，如：480px）
		int screenHeight = dm.heightPixels; // 屏幕高（像素，如：800px）

		return screenWidth + "*" + screenHeight;
	}

	/**
	 * 通过比例设置图片的高度
	 *
	 * @param width 图片的宽
	 * @param bili  图片比例
	 * @param type  1:外层 LinearLayout 2：外层 RelativeLayout
	 */
	public static void formartHight(ImageView imageView, int width, float bili, int type) {
		int height = (int) (width / bili);
		if (type == 1) {
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
			imageView.setLayoutParams(lp);
		} else {
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
			imageView.setLayoutParams(lp);
		}
	}

	public static DisplayMetrics getDisplayMetrics(Context context) {
		DisplayMetrics metric = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metric);
		return metric;
	}



	public static DisplayMetrics displayMetrics(Context context) {
		if (null != dm) {
			return dm;
		}
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(dm);
		return dm;
	}

	public static int widthPixels(Context context) {
		return displayMetrics(context).widthPixels;
	}

	public static int heightPixels(Context context) {
		return displayMetrics(context).heightPixels;
	}

	public static float density(Context context) {
		return displayMetrics(context).density;
	}

	public static int densityDpi(Context context) {
		return displayMetrics(context).densityDpi;
	}

	public static boolean isFullScreen() {
		return isFullScreen;
	}

	public static void toggleFullScreen(Activity activity) {
		Window window = activity.getWindow();
		int flagFullscreen = WindowManager.LayoutParams.FLAG_FULLSCREEN;
		if (isFullScreen) {
			window.clearFlags(flagFullscreen);
			isFullScreen = false;
		} else {
			window.setFlags(flagFullscreen, flagFullscreen);
			isFullScreen = true;
		}
	}

	/**
	 * 保持屏幕常亮
	 */
	public static void keepBright(Activity activity) {
		//需在setContentView前调用
		int keepScreenOn = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		activity.getWindow().setFlags(keepScreenOn, keepScreenOn);
	}

	/**
	 * 获取屏幕的高度
	 * @param context
	 * @return
	 */
	public static int getScreenHeight(Context context) {
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getHeight();
	}

	/**
	 * 获得屏幕高度
	 *
	 * @param context
	 * @return
	 */
	public static int getScreenWidth(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.widthPixels;
	}
}
