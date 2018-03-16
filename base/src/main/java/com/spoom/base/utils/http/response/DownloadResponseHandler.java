package com.spoom.base.utils.http.response;

import java.io.File;

/**
 * package com.spoom.utils.http.response
 * 必须实现onFinish、onProgress、onFailure这三个方法
 * onStart和onCancel根据需要实现
 *
 * @author spoomlan
 * @date 01/01/2018
 */

public abstract class DownloadResponseHandler {
    public void onStart(long totalBytes) {
    }

    public void onCancel() {
    }

    /**
     * download finished
     *
     * @param downloadFile downloaded file
     */
    public abstract void onFinish(File downloadFile);

    /**
     * download on progress
     *
     * @param currentBytes bytes had been downloaded
     * @param totalBytes   total bytes of the request target
     */
    public abstract void onProgress(long currentBytes, long totalBytes);

    /**
     * download failed
     *
     * @param errorMsg
     */
    public abstract void onFailure(String errorMsg);
}
