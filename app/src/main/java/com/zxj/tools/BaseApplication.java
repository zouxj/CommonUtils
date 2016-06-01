package com.zxj.tools;
import android.app.Application;
import android.os.Handler;
import android.os.Looper;


/**
 * Created by zxj on 2015/10/26.
 */
public class BaseApplication extends Application {
    //获取主线程上下文
    private   static BaseApplication mContext=null;
    //获取到主线程Handler
    private  static Handler mMainThreadHandler=null;
    //获取主线程Looper
    private static Looper mMainThreadLooper = null;
    //获取到主线程
    private static Thread mMainThread = null;
    //获取主线成ID
    private static int mMainThreadId = 0;
    public static BaseApplication getApplication(){
        return mContext;
    }
    public static Handler getMainThreadHandler(){
        return mMainThreadHandler;
    }
    public static Looper getMainThreadLooper(){
        return mMainThreadLooper;
    }
    public static Thread getMainThread(){
        return mMainThread;
    }
    public  static int getmMainThreadId(){
        return mMainThreadId;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.mContext = this;
        this.mMainThreadHandler = new Handler();
        this.mMainThread = Thread.currentThread();
        this.mMainThreadLooper = getMainLooper();
        this.mMainThreadId = android.os.Process.myPid();
    }
    }