package com.zxj.tools.utils;

import android.os.Debug;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * @author Ivan.Lu
 * @date 2012-1-3
 * @version V1.0
 * @description JsonUtils工具类
 */
public class JsonUtils {

    /**
     * 将json对象转换成java对象
     * @param obj
     * @return
     * @throws JSONException
     */
    public static String getJson2String(String jsonData,String key) throws JSONException{

        if(null == key)return null;

        JSONObject obj = new JSONObject(jsonData);
        String value = obj.get(key).toString();
        if(Debug.isDebuggerConnected()){
            Log.i("parseObj2Json", value);
        }
        return value;
    }

    /**
     * 将java对象转换成json对象
     * @param obj
     * @return
     */
    public static String parseObj2Json(Object obj){

        if(null == obj)return null;

        Gson gson=new Gson();
        String objstr=gson.toJson(obj);
        if(Debug.isDebuggerConnected()){
            Log.i("parseObj2Json", objstr);
        }
        return objstr;
    }


    /**
     * 将java对象的属性转换成json的key
     * @param obj
     * @return
     */
    public static String parseObj2JsonOnField(Object obj){

        if(null == obj)return null;

        Gson gson=new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES).create();
        String objstr=gson.toJson(obj);
        if(Debug.isDebuggerConnected()){
            Log.i("parseObj2JsonOnField", objstr);
        }
        return objstr;
    }

    /**
     * 将json对象转换成java对象
     * @param <T>
     * @param jsonData
     * @param c
     * @return
     */
    public static <T> Object parseJson2Obj(String jsonData, Class<T> c) {

        if(null==jsonData)return null;

        Gson gson = new Gson();
        Object obj = gson.fromJson(jsonData, c);
        return obj;
    }

    /**
     * 将json对象转换成数组对象
     * @param <T>
     * @param jsonData
     * @param c
     * @return
     * @throws JSONException
     */
    public static <T> ArrayList<T> parseJson2List(String jsonData, Class<T> c) throws JSONException {

        if(null==jsonData || "".equals(jsonData))return null;

        Gson gson = new Gson();
        ArrayList<T> list = new ArrayList<T>();
        JSONArray jsonArray = new JSONArray(jsonData);
        JSONObject objItem = null;
        T objT = null;

        for (int i = 0; i < jsonArray.length(); i++) {
            objItem = (JSONObject) jsonArray.get(i);
            if(null != objItem){
                objT = gson.fromJson(objItem.toString(), c);
                list.add(objT);
            }
        }

        if(Debug.isDebuggerConnected()){
            Log.i("parseJson2List", list.toString());
        }
        return list;
    }

    /**
     * 将输入流转成json格式
     * @param data
     * @return
     */
    public static String jsonReader(InputStream data) {
        BufferedReader reader = null;
        String laststr = "";
        try {
            reader = new BufferedReader(new InputStreamReader(data, "utf-8"));
            String tempString = null;
            int line = 1;
            while ((tempString = reader.readLine()) != null) {
                laststr = laststr + tempString;
                line++;
            }
            reader.close();
            return laststr;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return null;
    }
}
