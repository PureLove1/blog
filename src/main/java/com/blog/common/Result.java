package com.blog.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

import static com.blog.constant.StatusCode.OK;
import static com.blog.constant.StatusCode.USER_ERROR;

/**
 * 返回结果集
 * @author 贺畅
 * @date 2022/11/28
 * @param <R>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<R> {
    private boolean result;
    private String statusCode;
    private String message;
    private R data;
    private static final int[] NO_DATA = new int[]{};

    public static Result ok(){
        return new Result(true, OK, "请求成功", NO_DATA);
    }

    public static Result ok(String message){
        return new Result(true, OK, message, NO_DATA);
    }

    public static Result ok(Object data){
        return new Result(true, OK, "请求成功", data);
    }

    public static Result<Object> ok(String message, Object data){
        return new Result<Object>().setResult(true).setStatusCode(OK).setMessage(message).setData(data);
    }
    public static Result error(){
        return new Result(false, USER_ERROR, "请求失败", NO_DATA);
    }

    public static Result error(String message){
        return new Result(false, USER_ERROR, message, NO_DATA);
    }

    public static Result error(String message, String statusCode, Object data){
        return new Result<Object>().setResult(false).setMessage(message).setStatusCode(statusCode).setData(data);
    }

    public static Result error(String message, String statusCode){
        return new Result<Object>().setResult(false).setMessage(message).setStatusCode(statusCode);
    }

    @Deprecated
    public Result setMessage(String message){
        this.message = message;
        return this;
    }
    @Deprecated
    public Result setResult(boolean result){
        this.result = result;
        return this;
    }
    @Deprecated
    public Result setStatusCode(String statusCode){
        this.statusCode = statusCode;
        return this;
    }
    @Deprecated
    public Result<R> setData(R data){
        this.data = data;
        return this;
    }

}
