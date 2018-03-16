package com.spoom.base.utils.http;

import android.os.Handler;
import android.os.Looper;
import com.spoom.base.utils.http.builder.DownloadBuilder;
import com.spoom.base.utils.http.builder.GetBuilder;
import com.spoom.base.utils.http.builder.PostBuilder;
import com.spoom.base.utils.http.builder.UploadBuilder;
import okhttp3.Call;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;

/**
 * package com.spoom.utils.http
 *
 * @author spoomlan
 * @date 01/01/2018
 */

public class HttpUtil {
    private static OkHttpClient httpClient;
    public static Handler handler = new Handler(Looper.getMainLooper());

    public OkHttpClient getHttpClient() {
        return httpClient;
    }

    public HttpUtil(OkHttpClient okHttpClient) {
        if (httpClient == null) {
            if (okHttpClient == null) {
                httpClient = new OkHttpClient();
            } else {
                httpClient = okHttpClient;
            }
        }
    }

    public GetBuilder get() {
        return new GetBuilder(this);
    }

    public PostBuilder post() {
        return new PostBuilder(this);
    }

    public UploadBuilder upload(){
        return new UploadBuilder(this);
    }

    public DownloadBuilder download(){
        return new DownloadBuilder(this);
    }

    /**
     * 根据tag取消该次http请求
     *
     * @param tag
     */
    public void cancel(Object tag) {
        Dispatcher dispatcher = httpClient.dispatcher();
        for (Call call : dispatcher.queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : dispatcher.runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }
}
