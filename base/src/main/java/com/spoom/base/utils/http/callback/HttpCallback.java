package com.spoom.base.utils.http.callback;

import com.spoom.base.utils.http.HttpUtil;
import com.spoom.base.utils.http.response.IResponseHandler;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.IOException;

/**
 * package com.spoom.utils.http.callback
 * 这里通过主线程的handler post message，run的是主线程中的回调，这样可以更新UI
 * 如果是直接在okHttp线程中回调，则不能更新UI
 *
 * @author spoomlan
 * @date 01/01/2018
 */

public class HttpCallback implements Callback {
    private IResponseHandler responseHandler;

    public HttpCallback(IResponseHandler responseHandler) {
        this.responseHandler = responseHandler;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        // run()中的回调方法在Main Thread中运行
        HttpUtil.handler.post(() -> responseHandler.onFailure(400, e.toString()));
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if (response.isSuccessful()) {
            responseHandler.onSuccess(response);
        } else {
            HttpUtil.handler.post(() -> responseHandler.onFailure(400, "response code: " + response.code()));
        }
    }
}
