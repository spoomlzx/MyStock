package com.spoom.base.utils.http.response;

import okhttp3.Response;

/**
 * package com.spoom.utils.http.response
 *
 * @author spoomlan
 * @date 01/01/2018
 */

public interface IResponseHandler {

    void onSuccess(Response response);

    void onFailure(int code, String errorMsg);

    /**
     * progress when upload a file
     *
     * @param currentBytes bytes had been uploaded
     * @param totalBytes   total bytes of the file
     */
    void onProgress(long currentBytes, long totalBytes);
}
