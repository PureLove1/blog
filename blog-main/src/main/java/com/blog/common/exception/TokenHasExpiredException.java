package com.blog.common.exception;

import lombok.Getter;

/**
 * @author 贺畅
 * @date 2022/11/28
 */
@Getter
public class TokenHasExpiredException extends RuntimeException{
    /**
     * 信息
     */
    private String message ;

    public TokenHasExpiredException(){
        message = "登录已过期";
    }

}
