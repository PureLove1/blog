package com.blog.common.exception;

import com.blog.common.Result;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static com.blog.constant.StatusCode.*;

/**
 * 全局异常处理器
 * @author 贺畅
 * @date 2022/11/28
 */
@Slf4j
@RestControllerAdvice
@ControllerAdvice(annotations = {Controller.class, RestController.class})
@ResponseBody
public class GlobalExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 用户未完成登录异常
     * @param e
     * @return
     */
    @ExceptionHandler({UnauthorizedException.class,UserNotLoginException.class})
    public Result handleUnauthorizedException(Exception e) {
        log.error(e.getMessage());
        return Result.error(e.getMessage(),USER_LOGIN_ERROR,null);
    }

    /**
     * 用户权限不足异常
     * @param e
     * @return
     */
    @ExceptionHandler(AccessDenyException.class)
    public Result handleAccessDenyException(AccessDenyException e) {
        log.error(e.getMessage());
        return Result.error(e.getMessage(),USER_ACCESS_ERROR,null);
    }

    /**
     * cookie解析出错
     */
    @ExceptionHandler(SignatureException.class)
    public Result handleSignatureException(SignatureException e) {
        log.error(e.getMessage());
        return Result.error("登录凭证校验失败，请重新登录",USER_LOGIN_ERROR,null);
    }
    /**
     * Cookie过期
     * @param e
     * @return
     */
    @ExceptionHandler(TokenHasExpiredException.class)
    public Result handleTokenHasExpiredException(TokenHasExpiredException e){
        return Result.error("登录信息已过期，请重新登录",USER_LOGIN_EXPIRED_ERROR,null);
    }

    @ExceptionHandler(CustomException.class)
    public Result handleCustomException(CustomException e){
        logger.error("错误信息{},错误原因{}",e.getMessage(),e.getCause());
        return Result.error(e.getMessage(),SYSTEM_EXECUTION_ERROR,null);
    }

    /**
     * 截取未处理的运行时异常
     * @param e
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    public Result handleRuntimeException(RuntimeException e) {
        log.error(e.toString(), e);
        return Result.error(e.getMessage(),SYSTEM_EXECUTION_ERROR,null);
    }

}
