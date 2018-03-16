package com.spoom.base.utils.http.builder;

import com.spoom.base.utils.http.HttpUtil;
import com.spoom.base.utils.http.callback.HttpCallback;
import com.spoom.base.utils.http.response.IResponseHandler;
import okhttp3.Request;

import java.util.Map;

/**
 * package com.spoom.utils.http.builder
 *
 * @author spoomlan
 * @date 01/01/2018
 */

public class GetBuilder extends HttpRequestBuilder<GetBuilder> {

    public GetBuilder(HttpUtil httpUtil) {
        super(httpUtil);
    }

    @Override
    public void enqueue(IResponseHandler responseHandler) {
        try {
            if (url == null || url.length() == 0) {
                throw new IllegalArgumentException("url can not be null !");
            }
            if (params != null && params.size() > 0) {
                url = appendParams(url, params);
            }
            Request.Builder builder = new Request.Builder().url(url).get();
            appendHeaders(builder, headers);
            if (this.tag != null) {
                builder.tag(this.tag);
            }
            Request request = builder.build();
            httpUtil.getHttpClient().newCall(request).enqueue(new HttpCallback(responseHandler));
        } catch (Exception e) {
            responseHandler.onFailure(400, e.getMessage());
        }
    }

    private String appendParams(String url, Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        sb.append(url).append("?");
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                sb.append(key).append("=").append(params.get(key)).append("&");
            }
        }
        sb = sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
