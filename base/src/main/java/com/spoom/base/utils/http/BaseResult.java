package com.spoom.base.utils.http;

/**
 * package com.spoom.utils.http
 *
 * @author spoomlan
 * @date 01/01/2018
 */

public class BaseResult {
    private String msg;
    private int code;
    private Object data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
