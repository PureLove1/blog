package com.blog.constant;

/**
 * 返回状态码，参照阿里巴巴Java开发手册黄山版
 * @author 贺畅
 * @date 2022/11/28
 */
public class StatusCode {
    /**
     * 一切 ok 正确执行后的返回
     */
    public static String OK="00000";

    /**
     * 用户端错误 一级宏观错误码
     */
    public static String USER_ERROR="A0001";

    /**
     * 用户注册错误 二级宏观错误码
     */
    public static String USER_REGISTER_ERROR="A0100";

    /**
     * 用户未同意隐私协议
     */
    public static String USER_PRIVACY_NOT_ALLOW_ERROR="A0101";

    /**
     * 用户名校验失败
     */
    public static String USER_UNCHECKED_NAME_ERROR="A0110";

    /**
     * 用户名已存在
     */
    public static String USER_ALREADY_EXISTS_ERROR="A0111";

    /**
     * 用户名包含敏感词
     */
    public static String USER_NAME_CONTAINS_SENSITIVE_WORDS_ERROR="A0112";

    /**
     * 密码校验失败
     */
    public static String USER_PASSWORD_INVALIDATE_ERROR="A0120";

    /**
     * 校验码输入错误
     */
    public static String USER_WRONG_IMAGE_CODE_ERROR="A0130";

    /**
     * 邮件校验码输入错误
     */
    public static String USER_WRONG_EMAIL_CODE_ERROR="A0132";

    /**
     * 邮箱验证码过期
     */
    public static String USER_EXPIRED_EMAIL_CODE_ERROR="A0133";

    /**
     * 邮箱格式校验失败
     */
    public static String USER_WRONG_EMAIL_FORMAT_ERROR="A0153";

    /**
     * 用户登录异常 二级宏观错误码
     */
    public static String USER_LOGIN_ERROR="A0200";

    /**
     * 用户账户不存在
     */
    public static String USER_NOT_EXISTS_ERROR="A0201";

    /**
     * 用户账户被冻结
     */
    public static String USER_ACCOUNT_LOCKED_ERROR="A0202";

    /**
     * 用户账户已作废
     */
    public static String USER_ACCOUNT_DISABLE_ERROR="A0203";

    /**
     * 用户密码错误
     */
    public static String USER_WRONG_PASSWORD_ERROR="A0210";

    /**
     * 用户登录已过期
     */
    public static String USER_LOGIN_EXPIRED_ERROR="A0230";

    /**
     * 访问权限异常 二级宏观错误码
     */
    public static String USER_ACCESS_ERROR="A0300";

    /**
     * 访问未授权
     */
    public static String USER_UNAUTHORIZED_ERROR="A0301";

    /**
     * 用户签名异常
     */
    public static String USER_SIGNATURE_ERROR="A0340";

    /**
     * RSA 签名错误
     */
    public static String USER_RSA_SIGNATURE_ERROR="A0341";

    /**
     * 用户请求参数错误 二级宏观错误码
     */
    public static String USER_WRONG_PARAMETER_ERROR="A0400";

    /**
     * 请求必填参数为空
     */
    public static String USER_REQUIRED_PARAMETER_IS_NULL_ERROR="A0410";

    /**
     * 用户上传文件异常 二级宏观错误码
     */
    public static String USER_UPLOAD_FILE_ERROR="A0700";

    /**
     * 用户上传文件类型不匹配
     */
    public static String USER_UPLOAD_FILE_TYPE_UNMATCHED_ERROR="A0701";

    /**
     * 用户上传文件太大
     */
    public static String USER_UPLOAD_FILE_TOO_BIG_ERROR="A0702";

    /**
     * 用户上传图片太大
     */
    public static String USER_UPLOAD_IMAGE_TOO_BIG_ERROR="A0703";

    /**
     * 用户上传视频太大
     */
    public static String USER_UPLOAD_VIDEO_TOO_BIG_ERROR="A0704";


    /**
     * 用户隐私未授权 二级宏观错误码
     */
    public static String USER_PRIVACY_UNAUTHORIZED_ERROR="A0900";

    /**
     * 系统执行出错 一级宏观错误码
     */
    public static String SYSTEM_EXECUTION_ERROR="B0001";

}
