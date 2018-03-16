package com.spoom.base.utils.http.builder;

import com.spoom.base.utils.http.HttpUtil;
import com.spoom.base.utils.http.callback.HttpCallback;
import com.spoom.base.utils.http.response.IResponseHandler;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.util.Map;

/**
 * package com.spoom.utils.http.builder
 *
 * @author spoomlan
 * @date 01/01/2018
 */

public class PostBuilder extends HttpRequestBuilder<PostBuilder> {
    private String jsonParams = "";

    public PostBuilder(HttpUtil httpUtil) {
        super(httpUtil);
    }

    public PostBuilder jsonParams(String json){
        this.jsonParams=json;
        return this;
    }

    @Override
    public void enqueue(IResponseHandler responseHandler) {
        try {
            if (url == null || url.length() == 0) {
                throw new IllegalArgumentException("url can not be null !");
            }
            Request.Builder builder = new Request.Builder().url(this.url);
            // 将headers添加到request.builder中
            appendHeaders(builder, this.headers);

            if (this.tag != null) {
                builder.tag(this.tag);
            }
            // 如果有jsonParams
            if (jsonParams.length() > 0) {
                // 构造json的requestBody
                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonParams);
                builder.post(body);
            } else {
                // 如果没有jsonPrams，则使用Form格式的body，添加所有param
                FormBody.Builder formBuilder = new FormBody.Builder();
                appendParams(formBuilder, this.params);
                builder.post(formBuilder.build());
            }
            Request request = builder.build();
            httpUtil.getHttpClient().newCall(request).enqueue(new HttpCallback(responseHandler));
        } catch (Exception e) {
            responseHandler.onFailure(400, e.getMessage());
        }
    }

    private void appendParams(FormBody.Builder builder, Map<String, String> params) {
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                builder.add(key, params.get(key));
            }
        }
    }
}
