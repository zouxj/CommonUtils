package com.zxj.tools.utils;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;
import com.zxj.tools.bean.Param;
import com.zxj.tools.bean.ResponseBean;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by zxj on 2015/10/28.
 * 基于OKHttp开源框架的 网络请求
 */
public class OkHttpClientManager {
    /**
     * OKHTTP 对象
     */
    private OkHttpClient mOkHttpClient;

    /**
     * 封装GET代理请求类
     */
    private GetDelegate mGetDelegate = new GetDelegate();
    /**
     * 封装Post代理请求类
     */
    private PostDelegate mPostDelegate = new PostDelegate();
    /**
     * Handler
     */
    private Handler mDelivery;
    /**
     * Gson解析
     */
    private Gson mGson;
    /**
     * 编码设置
     */
    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("text/x-markdown; charset=utf-8");

    /**
     * 实现单利模式
     */
    private static OkHttpClientManager mInstance;
    private RequestBody bodyfile;
    private Request.Builder reqBuilder;
    private OkHttpClientManager(){
        mOkHttpClient = new OkHttpClient();
        mOkHttpClient.Builder(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        mGson= new Gson();
        mDelivery = new Handler(Looper.getMainLooper());
    }

    /**
     * 提供一个一个获取对象的静态方法
     * @return
     */
    public static OkHttpClientManager getInstance(){
        if (null==mInstance){
            synchronized (OkHttpClientManager.class){
                if (null==mInstance) {
                    mInstance = new OkHttpClientManager();
                }
            }
        }
        return  mInstance;

    }

    /**
     * 获取一个Post请求代理类
     * @return
     */
    public PostDelegate getPostDelegate()
    {
        return mPostDelegate;
    }
    /**
     * 获取一个GET请求代理类
     * @return
     */
    public GetDelegate getGetDelegate()
    {
        return mGetDelegate;
    }
    /**
     * ============Get方便的访问方式============
     */

    public  void getAsyn(String url, ResultCallback callback)
    {
        getInstance().getGetDelegate().getAsyn(url, callback, null);
    }

    public  void getAsyn(String url, ResultCallback callback, Object tag)
    {
        getInstance().getGetDelegate().getAsyn(url, callback, tag);
    }

    /**
     * ============POST方便的访问方式===============
     */
    public  void postAsyn(String url, Param[] params, final ResultCallback callback)
    {
        getInstance().getPostDelegate().postAsyn(url, params, callback, null);
    }
    public  void postAsyn(String url, Map<String, String> params, final ResultCallback callback)
    {
        getInstance().getPostDelegate().postAsyn(url, params, callback, null);
    }
    public  void postAsyn(String url, Param[] params, final ResultCallback callback, Object tag)
    {
        getInstance().getPostDelegate().postAsyn(url, params, callback, tag);
    }

    public  void postAsyn(String url, Map<String, String> params, final ResultCallback callback, Object tag)
    {
        getInstance().getPostDelegate().postAsyn(url, params, callback, tag);
    }


    //==================== get GetDelegate=======================
    public class GetDelegate
    {
        /**
         * 获取一个Get请求的Request
         * @param url
         * @param tag
         * @return
         */
        private Request buildGetRequest(String url, Object tag)
        {
            Request.Builder builder = new Request.Builder().url(url);
            if (tag != null)
            {
                builder.tag(tag);
            }
            return builder.build();
        }
        /**
         * 通用的方法
         */
        public void getAsyn(Request request, ResultCallback callback)
        {
            deliveryResult(callback, request);
        }
        /**
         * 异步的get请求
         */
        public void getAsyn(String url, final ResultCallback callback, Object tag)
        {
            final Request request = buildGetRequest(url, tag);
            getAsyn(request, callback);
        }

        /**
         * 通用的方法
         */
        public Response get(Request request) throws IOException
        {
            Call call = mOkHttpClient.newCall(request);
            Response execute = call.execute();
            return execute;
        }

        /**
         * 同步的Get请求
         */
        public Response get(String url) throws IOException
        {
            return get(url, null);
        }

        public Response get(String url, Object tag) throws IOException
        {
            final Request request = buildGetRequest(url, tag);
            return get(request);
        }
    }



    /**
     * 请求的方法并且响应结果
     * @param callback
     * @param request
     */
    private void deliveryResult(ResultCallback callback, Request request)
    {

        if (callback == null) callback = DEFAULT_RESULT_CALLBACK;
        final ResultCallback resCallBack = callback;
        //UI thread
        callback.onBefore(request);
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailedStringCallback(call.request(), e, resCallBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final String string = response.body().string();
                    if (resCallBack.mType == String.class) {//判断返回结果是不是字符串类型
                        sendSuccessResultCallback(string, resCallBack);//是字符串类型直接回调
                    } else {
                        ResponseBean o = mGson.fromJson(string, resCallBack.mType);//不是则通过gson转化
                        sendSuccessResultCallback(o, resCallBack);
                    }
                } catch (IOException e) {
                    sendFailedStringCallback(response.request(), e, resCallBack);
                } catch (com.google.gson.JsonParseException e)//Json解析的错误
                {
                    sendFailedStringCallback(response.request(), e, resCallBack);
                }
            }

        });
    }

    /**
     * 自定义回调
     */
    private final ResultCallback<String> DEFAULT_RESULT_CALLBACK = new ResultCallback<String>()
    {
        @Override
        public void onError(Request request, Exception e)
        {
        }

        @Override
        public void onResponse(String response)
        {

        }
    };
    /**
     * 返回结果回掉类
     * @param <T>
     */
    public static abstract class ResultCallback<T>
    {
        Type mType;
        public ResultCallback()
        {
                mType = getSuperclassTypeParameter(getClass());
        }

        static Type getSuperclassTypeParameter(Class<?> subclass)
        {
             //getGenericSuperclass()获得带有泛型的父类
             //Type是 Java 编程语言中所有类型的公共高级接口。它们包括原始类型、参数化类型、数组类型、类型变量和基本类型
            Type superclass = subclass.getGenericSuperclass();
            if (superclass instanceof Class)
            {
                throw new RuntimeException("Missing type parameter.");
            }
            ParameterizedType parameterized = (ParameterizedType) superclass;
            return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
        }

        public void onBefore(Request request)
        {
        }

        public void onAfter()
        {
        }

        public abstract void onError(Request request, Exception e);

        public abstract void onResponse(T response);
    }

    /**
     *调用返回成功的结果
     * @param object
     * @param callback
     */
    private void sendSuccessResultCallback(final Object object, final ResultCallback callback)
    {
        mDelivery.post(new Runnable()
        {
            @Override
            public void run()
            {
                callback.onResponse(object);
                callback.onAfter();
            }
        });
    }

    /**
     * 处理失败返回的结果
     * @param request
     * @param e
     * @param callback
     */
    private void sendFailedStringCallback(final Request request, final Exception e, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(request, e);
                callback.onAfter();
            }
        });
    }
    //====================Post Delegate=======================
    public class PostDelegate{
        private final MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream;charset=utf-8");
        private final MediaType MEDIA_TYPE_STRING = MediaType.parse("text/plain;charset=utf-8");
        /**
         * 异步的post请求
         */
        public void postAsyn(String url, Param[] params, final ResultCallback callback, Object tag)
        {
            Request request = buildPostFormRequest(url, params, tag);
            deliveryResult(callback, request);
        }
        /**
         * 同步请求
         * @param url
         * @param params
         * @param callback
         * @param tag
         */
        public void postAsyn(String url, Map<String, String> params, final ResultCallback callback, Object tag)
        {
            Param[] paramsArr = map2Params(params);
            postAsyn(url, paramsArr, callback, tag);
        }
    }


    /**
     * 将map params转换为param[]
     * @param params
     * @return
     */
    private Param[] map2Params(Map<String, String> params)
    {
        if (params == null) return new Param[0];
        int size = params.size();
        Param[] res = new Param[size];
        Set<Map.Entry<String, String>> entries = params.entrySet();
        int i = 0;
        for (Map.Entry<String, String> entry : entries)
        {
            res[i++] = new Param(entry.getKey(), entry.getValue());
        }
        return res;
    }


    /**
     * 拼接参数
     * @param url
     * @param params
     * @param tag
     * @return
     */
    private Request buildPostFormRequest(String url, Param[] params, Object tag)
    {
        if (params == null)
        {
            params = new Param[0];
        }
        FormBody.Builder builder = new FormBody.Builder();
        for (Param param : params)
        {
                builder.add(param.getKey(), param.getValue());
        }
        RequestBody requestBody = builder.build();
        Request.Builder reqBuilder = new Request.Builder();
        reqBuilder.url(url).post(requestBody);
        if (tag != null)
        {
            reqBuilder.tag(tag);
        }
        return reqBuilder.build();
    }

//    public void uploadFile(String url,RequestBean beas,ResultCallback callback){
//
//        String filePath = null;
//        String fileName = null;
//        SetUserInfoRequest setUserInfoRequest = (SetUserInfoRequest) beas;
//         filePath = setUserInfoRequest.headPic.replaceAll("file://", "");
////          filePath = filePath.substring(filePath.lastIndexOf("/"));
//          fileName= "headPic";
//        File file = new File(filePath);
//        RequestBody requestBody = new MultipartBuilder().type(MultipartBuilder.FORM).
//                addFormDataPart("sex",setUserInfoRequest.sex).
//                addFormDataPart("emergencyContactPhone",setUserInfoRequest.emergencyContactPhone).
//                addFormDataPart("emergencyContact",setUserInfoRequest.emergencyContact).
//                addFormDataPart("driverNum",setUserInfoRequest.driverNum).
//                addFormDataPart("driverEffectiveYear",setUserInfoRequest.driverEffectiveYear).
//                addFormDataPart("nickName",setUserInfoRequest.nickName).
//                addFormDataPart("driverEffectiveDate",setUserInfoRequest.driverEffectiveDate)
//                .addFormDataPart("token",setUserInfoRequest.token).
//                        addFormDataPart(fileName, file.getName(), RequestBody.create(null, file)).build();
//        Request request = new Request.Builder()//建立请求
//         .url(url)//请求的地址
// .post(requestBody)//请求的内容（上面建立的requestBody）
//                .build();
//        deliveryResult(callback,request);
//
//    }


}
