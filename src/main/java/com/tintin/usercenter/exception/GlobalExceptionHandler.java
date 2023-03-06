package com.tintin.usercenter.exception;

import com.tintin.usercenter.common.BaseResponse;
import com.tintin.usercenter.common.ErrorCode;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHandler(BusinessException e){
        return new BaseResponse(e.getCode(), null, e.getMessage(), e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeExceptionHandler(RuntimeException e){
        return new BaseResponse(ErrorCode.PARAMS_ERROR, e.getMessage(), "");
    }

}
