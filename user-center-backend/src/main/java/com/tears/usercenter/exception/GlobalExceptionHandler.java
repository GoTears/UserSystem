package com.tears.usercenter.exception;

import com.tears.usercenter.common.BaseResponse;
import com.tears.usercenter.common.ErrorCode;
import com.tears.usercenter.common.ResultUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


//集中处理
@RestControllerAdvice
//@Slf4j
public class GlobalExceptionHandler {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(GlobalExceptionHandler.class);

    //针对什么异常做什么处理
    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHandler(BusinessException e){
        log.error("BusinessException - Code: {}, Message: {}, Description: {}, StackTrace: {}", 
            e.getCode(), e.getMessage(), e.getDescription(), e.getStackTrace());
        return ResultUtils.error(e.getCode(), e.getMessage(), e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeExceptionHandler(RuntimeException e){
        log.error("RuntimeException - Message: {}, StackTrace: {}", e.getMessage(), e.getStackTrace());
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage(), "");
    }
    
    @ExceptionHandler(Exception.class)
    public BaseResponse exceptionHandler(Exception e){
        log.error("Exception - Message: {}, StackTrace: {}", e.getMessage(), e.getStackTrace());
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "系统内部异常", "");
    }


}
