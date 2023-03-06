package com.tintin.usercenter.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 全局返回类型
 *
 * @param <T>
 *
 * @author tintin
 */
@Data
public class BaseResponse<T> implements Serializable {

    private int code;

    private T data;

    private String message;

    private String description;


    public BaseResponse(int code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }
    public BaseResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public BaseResponse(int code, T data) {
        this(code, data, "", "");
    }

    public BaseResponse(ErrorCode errorCode){
        this(errorCode.getCode(), null, errorCode.getMessage(), errorCode.getDescription());
    }

    public BaseResponse(ErrorCode errorCode, String description){
        this(errorCode.getCode(), null, errorCode.getMessage(), description);
    }

    public BaseResponse(ErrorCode errorCode,String message, String description){
        this(errorCode.getCode(), null, errorCode.getMessage(), description);
    }

}
