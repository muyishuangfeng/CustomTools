package com.yk.customsdk.customlibiary.gps;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.yk.fast.customfast.constants.Constants;
import com.yk.fast.customfast.model.GpsModel;

/**
 * Created by Silence on 2017/4/5.
 */

public class GetGpsThread extends Thread {

    private Context mContext;
    private Handler mHandler;
    private int mTime;
    private int mConnectTime;
    private static AMapLocationClient locationClient = null;
    private static AMapLocationClientOption locationOption = new AMapLocationClientOption();

    /**
     * 初始化
     *
     * @param context
     * @param handler
     * @param time
     */
    public GetGpsThread(Context context, Handler handler, int time, int connectTime) {
        this.mContext = context;
        this.mHandler = handler;
        this.mTime = time;
        this.mConnectTime = connectTime;
    }

    @Override
    public void run() {
        super.run();
        //初始化定位
        initLocation();
        startLocation();
    }

    /**
     * 发送消息
     */
    private void sendMessage(int what, Object object) {
        Message msg = new Message();
        msg.what = what;
        msg.obj = object;
        mHandler.sendMessage(msg);
    }


    /**
     * 初始化定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void initLocation() {
        //初始化client
        locationClient = new AMapLocationClient(mContext);
        //设置定位参数
        locationClient.setLocationOption(getDefaultOption());
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
    }

    /**
     * 默认的定位参数
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(true);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(true);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(true);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }

    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation loc) {
            if (null != loc) {
                //解析定位结果
                GpsModel getGpsBean = new GpsModel();
                getGpsBean.setAddress(loc.getAddress());
                getGpsBean.setCityName(loc.getCity());
                getGpsBean.setLatitude(loc.getLatitude());
                getGpsBean.setLongitude(loc.getLongitude());
                getGpsBean.setCityCode(loc.getCityCode());
                getGpsBean.setProvince(loc.getProvince());
                getGpsBean.setCountry(loc.getCountry());
                sendMessage(Constants.MESSAGE_LOCATION_SUCCESS, getGpsBean);
            } else {
                sendMessage(Constants.MESSAGE_LOCATION_FAILED, loc.getErrorInfo());
            }
        }
    };

    /**
     * 根据控件的选择，重新设置定位参数
     */
    private void resetOption() {
        if (locationOption != null) {
            // 设置是否需要显示地址信息
            locationOption.setNeedAddress(true);
            /**
             * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
             * 注意：只有在高精度模式下的单次定位有效，其他方式无效
             */
            locationOption.setGpsFirst(true);
            // 设置是否开启缓存
            locationOption.setLocationCacheEnable(true);
            // 设置是否单次定位
            locationOption.setOnceLocation(true);
            //设置是否等待设备wifi刷新，如果设置为true,会自动变为单次定位，持续定位时不要使用
            locationOption.setOnceLocationLatest(true);
            //设置是否使用传感器
            locationOption.setSensorEnable(true);
            //设置是否开启wifi扫描，如果设置为false时同时会停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
            try {
                // 设置发送定位请求的时间间隔,最小值为1000，如果小于1000，按照1000算
                locationOption.setInterval(Long.valueOf(mTime));
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                // 设置网络请求超时时间
                locationOption.setHttpTimeOut(Long.valueOf(mConnectTime));
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 开始定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void startLocation() {
        //根据控件的选择，重新设置定位参数
        resetOption();
        if (locationClient != null && locationOption != null) {
            // 设置定位参数
            locationClient.setLocationOption(locationOption);
            // 启动定位
            locationClient.startLocation();
        }
    }

    /**
     * 停止定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    public static void stopLocation() {
        if (locationClient != null) {
            // 停止定位
            locationClient.stopLocation();
            locationClient = null;
        }
    }

    /**
     * 销毁定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    public static void destroyLocation() {
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }


}
