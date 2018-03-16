package com.spoom.base.utils.http.body;

import com.spoom.base.utils.http.response.DownloadResponseHandler;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.*;

import java.io.IOException;

/**
 * package com.spoom.utils.http.body
 *
 * @author spoomlan
 * @date 01/01/2018
 */

public class ProgressResponseBody extends ResponseBody {
    private ResponseBody responseBody;
    private DownloadResponseHandler responseHandler;
    private BufferedSource bufferedSource;

    public ProgressResponseBody(ResponseBody responseBody, DownloadResponseHandler handler) {
        this.responseBody = responseBody;
        this.responseHandler = handler;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                //这个的进度应该是读取response每次内容的进度，在写文件进度之前 所以暂时以写完文件的进度为准
                long bytesRead = super.read(sink, byteCount);
                totalBytesRead += ((bytesRead != -1) ? bytesRead : 0);
                return bytesRead;
            }
        };
    }
}
