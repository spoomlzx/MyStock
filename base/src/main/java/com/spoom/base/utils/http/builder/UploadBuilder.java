package com.spoom.base.utils.http.builder;

import com.spoom.base.utils.http.HttpUtil;
import com.spoom.base.utils.http.body.ProgressRequestBody;
import com.spoom.base.utils.http.callback.HttpCallback;
import com.spoom.base.utils.http.response.IResponseHandler;
import okhttp3.*;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * package com.spoom.utils.http.builder
 *
 * @author spoomlan
 * @date 01/01/2018
 */

public class UploadBuilder extends HttpRequestBuilder<UploadBuilder> {
    private Map<String, File> files;
    private List<MultipartBody.Part> parts;

    public UploadBuilder(HttpUtil httpUtil) {
        super(httpUtil);
    }

    public UploadBuilder files(Map<String, File> files) {
        this.files = files;
        return this;
    }

    public UploadBuilder addFile(String key, File file) {
        if (this.files == null) {
            files = new HashMap<>();
        }
        files.put(key, file);
        return this;
    }

    public UploadBuilder addFile(String key, String fileName, byte[] fileContent) {
        if (this.parts == null) {
            this.parts = new ArrayList<>();
        }
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), fileContent);
        this.parts.add(MultipartBody.Part.create(Headers.of("Content-Disposition",
                "form-data; name=\"" + key + "\"; filename=\"" + fileName + "\""),
                fileBody));
        return this;
    }

    @Override
    public void enqueue(IResponseHandler responseHandler) {
        try {
            if (url == null || url.length() == 0) {
                throw new IllegalArgumentException("url can not be null !");
            }
            Request.Builder builder = new Request.Builder().url(this.url);
            appendHeaders(builder, this.headers);
            if (this.tag != null) {
                builder.tag(tag);
            }
            MultipartBody.Builder multipartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            appendParams(multipartBuilder, this.params);
            appendFiles(multipartBuilder, this.files);
            appendParts(multipartBuilder, this.parts);
            // 使用重写的RequestBody监听上传进度
            builder.post(new ProgressRequestBody(multipartBuilder.build(), responseHandler));
            Request request = builder.build();
            httpUtil.getHttpClient().newCall(request).enqueue(new HttpCallback(responseHandler));
        } catch (Exception e) {
            responseHandler.onFailure(400, "Upload enqueue error: " + e.getMessage());
        }
    }

    private void appendParams(MultipartBody.Builder builder, Map<String, String> params) {
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
                        RequestBody.create(null, params.get(key)));
            }
        }
    }

    private void appendFiles(MultipartBody.Builder builder, Map<String, File> files) {
        if (files != null && !files.isEmpty()) {
            RequestBody fileBody;
            for (String key : files.keySet()) {
                File file = files.get(key);
                String fileName = file.getName();
                fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
                builder.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\"" + key + "\"; filename=\"" + fileName + "\""), fileBody);
            }
        }
    }

    //appends parts into MultipartBody builder
    private void appendParts(MultipartBody.Builder builder, List<MultipartBody.Part> parts) {
        if (parts != null && parts.size() > 0) {
            for (int i = 0; i < parts.size(); i++) {
                builder.addPart(parts.get(i));
            }
        }
    }

    //获取mime type
    private String guessMimeType(String fileName) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(fileName);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }
}
