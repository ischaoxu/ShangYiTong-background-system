package com.atguigu.syt.enums;

public enum GuiguExceptionEnum {
    ILLEGAL_DATA(4400,"非法的数据");

    private Integer statusCode;
    private String msg;

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getMsg() {
        return msg;
    }

    GuiguExceptionEnum(Integer statusCode, String msg) {
        this.statusCode = statusCode;
        this.msg = msg;
    }
}
