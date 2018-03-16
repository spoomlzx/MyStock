package com.spoom.base.utils.http.builder;

import android.support.annotation.NonNull;
import com.spoom.base.utils.http.HttpUtil;
import com.spoom.base.utils.http.body.ProgressResponseBody;
import com.spoom.base.utils.http.callback.DownloadCallback;
import com.spoom.base.utils.http.response.DownloadResponseHandler;
import com.spoom.base.utils.log.Logger;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * package com.spoom.utils.http.builder
 *
 * @author spoomlan
 * @date 01/01/2018
 */

public class DownloadBuilder {
    private HttpUtil httpUtil;
    private String url;
    private Object tag;
    private Map<String, String> headers;
    private String fileName;
    private String fileDir;
    private String filePath;
    private Long completeBytes = 0L;

    public DownloadBuilder(HttpUtil httpUtil) {
        this.httpUtil = httpUtil;
    }

    public DownloadBuilder url(String url) {
        this.url = url;
        return this;
    }

    public DownloadBuilder fileName(String name) {
        this.fileName = name;
        return this;
    }

    public DownloadBuilder fileDir(String dir) {
        this.fileDir = dir;
        return this;
    }

    /**
     * 设置下载文件的保存路径
     * 设置path则不需要设置name和dir
     *
     * @param path
     * @return
     */
    public DownloadBuilder filePath(String path) {
        this.filePath = path;
        return this;
    }

    public DownloadBuilder tag(Object tag) {
        this.tag = tag;
        return this;
    }

    public DownloadBuilder headers(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public DownloadBuilder addHeader(String key, String value) {
        if (this.headers == null) {
            this.headers = new HashMap<>();
        }
        this.headers.put(key, value);
        return this;
    }

    /**
     * 用于断点续传使用，开始下载任务时传入completeBytes
     *
     * @param completeBytes 已经下载的bytes数
     * @return
     */
    public DownloadBuilder setCompleteBytes(@NonNull Long completeBytes) {
        if (completeBytes > 0L) {
            this.completeBytes = completeBytes;
            addHeader("RANGE", "bytes=" + completeBytes + "-");     //添加断点续传header
        }
        return this;
    }

    public Call enqueue(DownloadResponseHandler responseHandler) {
        try {
            if (url == null || url.length() == 0) {
                throw new IllegalArgumentException("url can not be null !");
            }
            if (filePath.length() == 0) {
                if (fileDir.length() == 0 || fileName.length() == 0) {
                    throw new IllegalArgumentException("FilePath can not be null !");
                } else {
                    filePath = fileDir + fileName;
                }
            }
            checkFilePath(filePath, completeBytes);
            Request.Builder builder = new Request.Builder().url(this.url);
            appendHeaders(builder, this.headers);

            if (this.tag != null) {
                builder.tag(this.tag);
            }
            Request request = builder.build();
            Call call = httpUtil.getHttpClient().newBuilder()
                    .addNetworkInterceptor(chain -> {
                        Response originalResponse = chain.proceed(chain.request());
                        return originalResponse.newBuilder()
                                .body(new ProgressResponseBody(originalResponse.body(), responseHandler))
                                .build();
                    })
                    .build().newCall(request);
            call.enqueue(new DownloadCallback(responseHandler, this.filePath, this.completeBytes));
            return call;
        } catch (Exception e) {
            Logger.e("Download enqueue error");
            responseHandler.onFailure(e.getMessage());
            return null;
        }
    }

    /**
     * 检查filePath是否有效
     *
     * @param filePath
     * @param completeBytes
     * @throws Exception
     */
    private void checkFilePath(String filePath, Long completeBytes) throws Exception {
        File file = new File(filePath);
        if (file.exists()) {
            return;
        }

        if (completeBytes > 0L) {       //如果设置了断点续传 则必须文件存在
            throw new Exception("断点续传文件" + filePath + "不存在！");
        }

        if (filePath.endsWith(File.separator)) {
            throw new Exception("创建文件" + filePath + "失败，目标文件不能为目录！");
        }

        //判断目标文件所在的目录是否存在
        if (!file.getParentFile().exists()) {
            if (!file.getParentFile().mkdirs()) {
                throw new Exception("创建目标文件所在目录失败！");
            }
        }
    }

    /**
     * 添加http header
     *
     * @param builder
     * @param headers
     */
    private void appendHeaders(Request.Builder builder, Map<String, String> headers) {
        Headers.Builder headerBuilder = new Headers.Builder();
        if (headers == null || headers.isEmpty()) {
            return;
        }

        for (String key : headers.keySet()) {
            headerBuilder.add(key, headers.get(key));
        }
        builder.headers(headerBuilder.build());
    }
}
