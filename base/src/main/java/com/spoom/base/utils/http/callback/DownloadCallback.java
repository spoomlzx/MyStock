package com.spoom.base.utils.http.callback;

import com.spoom.base.utils.http.HttpUtil;
import com.spoom.base.utils.http.response.DownloadResponseHandler;
import com.spoom.base.utils.log.Logger;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

/**
 * package com.spoom.utils.http.callback
 *
 * @author spoomlan
 * @date 01/01/2018
 */

public class DownloadCallback implements Callback {
    private DownloadResponseHandler responseHandler;
    private String filePath;
    private Long completeBytes;

    public DownloadCallback(DownloadResponseHandler handler, String path, Long completeBytes) {
        this.responseHandler = handler;
        this.filePath = path;
        this.completeBytes = completeBytes;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        Logger.e("Download failure: " + e.getMessage());
        HttpUtil.handler.post(() -> responseHandler.onFailure(e.getMessage()));
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        ResponseBody body = response.body();
        try {
            if (response.isSuccessful()) {
                HttpUtil.handler.post(() -> responseHandler.onStart(body.contentLength()));
                try {
                    if (response.header("Content-Range") == null || response.header("Content-Range").length() == 0) {
                        //返回的没有Content-Range 不支持断点下载 需要重新下载
                        completeBytes = 0L;
                    }
                    saveFile(response, filePath, completeBytes);
                    final File file = new File(filePath);
                    HttpUtil.handler.post(() -> responseHandler.onFinish(file));
                } catch (Exception e) {
                    if (call.isCanceled()) {
                        HttpUtil.handler.post(() -> responseHandler.onCancel());
                    } else {
                        Logger.e("fail to save file: " + e.getMessage());
                        HttpUtil.handler.post(() -> responseHandler.onFailure("fail to save file: " + e.getMessage()));
                    }
                }
            } else {
                Logger.e("download error, code = " + response.code());
                HttpUtil.handler.post(() -> responseHandler.onFailure("download error, code = " + response.code()));
            }
        } finally {
            if (body != null) {
                body.close();
            }
        }
    }

    /**
     * 保存下载的文件
     *
     * @param response
     * @param filePath
     * @param completeBytes
     * @throws Exception
     */
    private void saveFile(Response response, String filePath, Long completeBytes) throws Exception {
        InputStream inputStream = null;
        byte[] buf = new byte[4 * 1024];           //每次读取4kb
        int len;
        RandomAccessFile file = null;

        try {
            inputStream = response.body().byteStream();
            file = new RandomAccessFile(filePath, "rwd");
            if (completeBytes > 0L) {
                file.seek(completeBytes);
            }

            long complete_len = 0;
            final long total_len = response.body().contentLength();
            while ((len = inputStream.read(buf)) != -1) {
                file.write(buf, 0, len);
                complete_len += len;

                //已经下载完成写入文件的进度
                final long final_complete_len = complete_len;
                HttpUtil.handler.post(() -> {
                    if (responseHandler != null) {
                        responseHandler.onProgress(final_complete_len, total_len);
                    }
                });
            }
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException ignored) {
            }
            try {
                if (file != null) {
                    file.close();
                }
            } catch (IOException ignored) {
            }
        }
    }
}
