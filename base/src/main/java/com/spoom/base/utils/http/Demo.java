package com.spoom.base.utils.http;

import android.os.Environment;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.spoom.base.utils.http.response.DownloadResponseHandler;
import com.spoom.base.utils.http.response.JsonResponseHandler;
import com.spoom.base.utils.log.Logger;
import okhttp3.OkHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * package com.spoom.plugin.utils.http
 *
 * @author spoomlan
 * @date 21/01/2018
 */

public class Demo {
    private static HttpUtil httpUtil;

    public static void main(String[] args) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .build();
        httpUtil = new HttpUtil(okHttpClient);
        doPostJSON();
        doGet();
        doUpload();
        doDownload();
    }

    private static void doPostJSON() {
        String url = "http://192.168.0.200:8087/api/user/login";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", "lin");
            jsonObject.put("password", "asdf1234");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        httpUtil.post()
                .url(url)
                .jsonParams(jsonObject.toString())          //与params不共存 以jsonParams优先
                //.tag(this)
                .enqueue(new JsonResponseHandler() {
                    @Override
                    public void onSuccess(Integer code, String msg, JsonObject data) {
                        Logger.d("code: " + code);
                        Logger.d("msg: " + msg);
                        Logger.d("data: " + data);
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {

                    }
                });
    }

    private static void doGet() {
        String url = "http://192.168.0.230:8087/api/user/info";
        httpUtil.get()
                .url(url)
                //.tag(this)
                .addHeader("token", "856f12f1c501423c89de6ba781943710")
                .enqueue(new JsonResponseHandler() {
                    @Override
                    public void onSuccess(Integer code, String msg, JsonObject data) {
                        Logger.d("code: " + code);
                        Logger.d("msg: " + msg);
                        Logger.d(data);
                        Gson gson = new Gson();
                        //gson.fromJson(data, User.class);
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {

                    }
                });
    }

    private static void doUpload() {
        httpUtil.upload()
                .url("http://192.168.0.230:8087/api/upload")
                .addHeader("token", "53e50fd2baf04cb3a464ea52c7e1c358")
                .addFile("file", new File(Environment.getExternalStorageDirectory() + "/DCIM/bk.png"))
                //.tag(this)
                .enqueue(new JsonResponseHandler() {
                    @Override
                    public void onSuccess(Integer code, String msg, JsonObject data) {
                        Logger.d("code: " + code);
                        Logger.d("msg: " + msg);
                        Logger.d("data: " + data);
                    }

                    @Override
                    public void onProgress(long currentBytes, long totalBytes) {
                        Logger.i("doUpload onProgress:" + currentBytes + "/" + totalBytes);
                    }

                    @Override
                    public void onFailure(int code, String errorMsg) {

                    }
                });

    }

    private static void doDownload() {
        httpUtil.download()
                .url("http://192.168.0.230/img/632ddca0bf4e4d2f99e524e804d82094.png")
                .filePath(Environment.getExternalStorageDirectory() + "/yiChat/1.png")
                //.tag(this)
                .enqueue(new DownloadResponseHandler() {
                    @Override
                    public void onStart(long totalBytes) {
                        Logger.i("totalBytes: " + totalBytes);
                    }

                    @Override
                    public void onFinish(File downloadFile) {
                        Logger.d("download finished");
                    }

                    @Override
                    public void onProgress(long currentBytes, long totalBytes) {
                        //Logger.d("doDownload onProgress:" + currentBytes + "/" + totalBytes);
                    }

                    @Override
                    public void onFailure(String errorMsg) {
                        Logger.e("download error");
                    }
                });
    }
}
