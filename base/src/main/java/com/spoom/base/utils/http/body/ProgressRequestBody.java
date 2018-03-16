package com.spoom.base.utils.http.body;

import com.spoom.base.utils.http.response.IResponseHandler;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.*;

import java.io.IOException;

/**
 * package com.spoom.utils.http.body
 *
 * @author spoomlan
 * @date 01/01/2018
 */

public class ProgressRequestBody extends RequestBody {
    private IResponseHandler responseHandler;
    private RequestBody requestBody;
    private BufferedSink sink;

    public ProgressRequestBody(RequestBody body, IResponseHandler handler) {
        this.requestBody = body;
        this.responseHandler = handler;
    }

    @Override
    public MediaType contentType() {
        return this.requestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink bufferedSink) throws IOException {
        if (sink == null) {
            sink = Okio.buffer(sink(bufferedSink));
        }
        requestBody.writeTo(sink);
        sink.flush();
    }

    private Sink sink(Sink sink) {
        return new ForwardingSink(sink) {
            //当前写入字节数
            long bytesWritten = 0L;
            //总字节长度，避免多次调用contentLength()方法
            long contentLength = 0L;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if (contentLength == 0) {
                    //获得contentLength的值，后续不再调用
                    contentLength = contentLength();
                }
                //增加当前写入的字节数
                bytesWritten += byteCount;
                //回调
                responseHandler.onProgress(bytesWritten, contentLength);
            }
        };
    }
}
