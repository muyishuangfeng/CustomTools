package com.yk.customsdk.customlibiary;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class ToastUtils {

    private static volatile ToastUtils sInstance;
    private Toast mToast;

    private ToastUtils(){}

    /**
     * 单例模式
     * @return
     */
    public static ToastUtils getInstance(){
        if (sInstance==null){
            synchronized (ToastUtils.class){
                if (sInstance==null){
                    sInstance=new ToastUtils();
                }
            }
        }
        return sInstance;
    }

    /**
     * 长时间显示
     * @param context
     * @param id
     */
    public final void longToast(Context context, int id) {
        longToast(context, context.getString(id));
    }

    /**
     * 长时间显示
     * @param context
     * @param toast
     */
    public final void longToast(Context context, final String toast) {
        toast(context, toast, Toast.LENGTH_LONG);
    }

    /**
     * 弹出
     * @param context
     * @param toast
     * @param length
     */
    private final void toast(final Context context, final String toast,
                             final int length) {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            doShowToast(context, toast, length);
        } else {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    doShowToast(context, toast, length);
                }
            });
        }
    }

    /**
     * 显示
     * @param context
     * @param toast
     * @param length
     */
    private final void doShowToast(Context context, String toast, int length) {
        try {
            final Toast t = getToast(context);
            t.setText(toast);
            t.setDuration(length);
            t.show();
        } catch (Exception e) {
            Toast.makeText(context, toast, length).show();
        }
    }

    /**
     * 获取
     * @param context
     * @return
     */
    private Toast getToast(Context context) {
        if (mToast != null) {
            mToast = new Toast(context.getApplicationContext());
        }
        return mToast;
    }

    /**
     * 短时间显示
     * @param context
     * @param id
     */
    public final void shortToast(Context context, int id) {
        shortToast(context, context.getString(id));
    }
    /**
     * 短时间显示
     * @param context
     */
    public final void shortToast(Context context, String toast) {
        toast(context, toast, Toast.LENGTH_SHORT);
    }

    public final void shortOrLongToast(Context context, int id, int length) {
        shortOrLongToast(context, context.getString(id), length);
    }

    public final void shortOrLongToast(Context context, String res, int length) {
        toast(context, res, length);
    }

    /**
     * 结束
     */
    public void onDestroy() {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
    }
}
