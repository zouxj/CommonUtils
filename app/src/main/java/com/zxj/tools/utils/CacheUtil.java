package com.zxj.tools.utils;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by zxj on 2015/10/27.
 * 缓存文件工具类
 */
public class CacheUtil {
    public static String cacheUtilKey ="";
    public static String datailCacheOBDID="";
    public static String cacheObdID="";

    private CacheUtil(){}
    private static CacheUtil mCacheUtil;
    /**
     * 实现单例
     */
   private static String name = "CacheUtil";
    public static CacheUtil getCacheUtil(){
        if(null==mCacheUtil){
            mCacheUtil = new CacheUtil();
        }
        return  mCacheUtil;
    }
    private  SharedPreferences sharedPreferences = UIUtils.getContext().getSharedPreferences(name, Activity.MODE_PRIVATE);
    private  SharedPreferences.Editor editor = sharedPreferences.edit();
    /**
     * 存integer类型
     * @param key
     * @param num
     */
    public  void saveInteger(String key,int num){
        editor.putInt(cacheUtilKey +key, num);
        editor.commit();
    }

    /**
     * 取出数据类型
     * @param key
     * @return
     */
    public  int getInteger(String key){
            return sharedPreferences.getInt(cacheUtilKey +key,-1);
    }

    /**
     * 缓存字段类型
     * @param key
     * @param value
     */
    public void saveData(String key,String value,boolean flag){
        if(flag){
            editor.putString(cacheUtilKey +key,value);
        }
       else{
            editor.putString(key,value);
        }
        editor.commit();
    }

    /**
     * 取出字段类型
     * @param key
     * @return
     */
    public String getData(String key){
        return sharedPreferences.getString(key,"");
    }

    /**
     * 缓存boolean变量
     * @param key
     * @param value
     */
    public void saveBoolean(String key,Boolean value){
        editor.putBoolean(cacheUtilKey +key,value);
        editor.commit();
    }
    /**
     * 取出boolean变量
     */
    public Boolean getBoolean(String key){
        return sharedPreferences.getBoolean(key,false);

    }
}
