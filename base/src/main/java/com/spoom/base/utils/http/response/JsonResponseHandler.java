package com.spoom.base.utils.http.response;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.spoom.base.utils.http.HttpUtil;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;

/**
 * package com.spoom.utils.http.response
 *
 * @author spoomlan
 * @date 01/01/2018
 */

public abstract class JsonResponseHandler implements IResponseHandler {
    @Override
    public void onSuccess(Response response) {
        String responseBodyStr = "";
        try (ResponseBody responseBody = response.body()) {
            if (responseBody != null) {
                responseBodyStr = responseBody.string();
            }
        } catch (IOException e) {
            e.printStackTrace();
            HttpUtil.handler.post(() -> onFailure(400, "fail to read response body"));
            return;
        }
        final String finalResponseBodyStr = responseBodyStr;
        JsonParser parser = new JsonParser();
        try {
            JsonObject jsonObject = parser.parse(finalResponseBodyStr).getAsJsonObject();
            Integer code = jsonObject.get("code").getAsInt();
            String msg = jsonObject.get("msg").getAsString();
            // code start with 4 ,means failure
            if (code / 100 == 4) {
                HttpUtil.handler.post(() -> onFailure(code, msg));
            }
            if (jsonObject.get("data").isJsonArray()) {
                JsonArray array = jsonObject.get("data").getAsJsonArray();
                HttpUtil.handler.post(() -> onSuccess(code, msg, array));
            } else if (jsonObject.get("data").isJsonObject()) {
                JsonObject object = jsonObject.get("data").getAsJsonObject();
                HttpUtil.handler.post(() -> onSuccess(code, msg, object));
            } else {
                HttpUtil.handler.post(() -> onSuccess(code, msg));
            }
        } catch (Exception e) {
            HttpUtil.handler.post(() -> onFailure(400, "parse result json error"));
        }
    }

    public void onSuccess(Integer code, String msg) {

    }

    public void onSuccess(Integer code, String msg, JsonObject object) {
    }

    public void onSuccess(Integer code, String msg, JsonArray array) {

    }

    @Override
    public void onProgress(long currentBytes, long totalBytes) {

    }
}
