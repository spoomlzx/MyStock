package com.spoom.base.utils.http.builder;

import com.spoom.base.utils.http.HttpUtil;
import com.spoom.base.utils.http.response.IResponseHandler;
import okhttp3.Headers;
import okhttp3.Request;

import java.util.HashMap;
import java.util.Map;

/**
 * package com.spoom.utils.http.builder
 *
 * @author spoomlan
 * @date 01/01/2018
 */

abstract public class HttpRequestBuilder<T extends HttpRequestBuilder> {
    protected String url;
    protected Object tag;
    protected Map<String, String> headers;
    protected Map<String, String> params;

    protected HttpUtil httpUtil;

    /**
     * 设置response的回调
     *
     * @param responseHandler 实现该接口的方法来对http返回结果进行处理
     */
    public abstract void enqueue(final IResponseHandler responseHandler);

    public HttpRequestBuilder(HttpUtil httpUtil) {
        this.httpUtil = httpUtil;
    }

    /**
     * 设置请求的url
     *
     * @param url
     * @return
     */
    public T url(String url) {
        this.url = url;
        return (T) this;
    }

    /**
     * 设置一次http请求的tag，用于cancel请求
     *
     * @param tag
     * @return
     */
    public T tag(Object tag) {
        this.tag = tag;
        return (T) this;
    }

    public T headers(Map<String, String> headers) {
        this.headers = headers;
        return (T) this;
    }

    public T addHeader(String key, String value) {
        if (this.headers == null) {
            headers = new HashMap<>();
        }
        headers.put(key, value);
        return (T) this;
    }

    protected void appendHeaders(Request.Builder builder, Map<String, String> headers) {
        Headers.Builder headerBuilder = new Headers.Builder();
        if (headers == null || headers.isEmpty()) {
            return;
        }
        for (String key : headers.keySet()) {
            headerBuilder.add(key, headers.get(key));
        }
        builder.headers(headerBuilder.build());
    }

    public T params(Map<String, String> params) {
        this.params = params;
        return (T) this;
    }

    public T addParam(String key, String value) {
        if (this.params == null) {
            params = new HashMap<>();
        }
        params.put(key, value);
        return (T) this;
    }
}
