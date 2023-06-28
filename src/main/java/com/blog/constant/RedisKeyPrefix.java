package com.blog.constant;

/**
 * 统一Redis Key的前缀
 * @author 贺畅
 * @date 2022/11/28
 */
public class RedisKeyPrefix {

    /**
     * 缓存blog id防止重复计数
     */
    public static final String BLOG_VIEWED_SET = "BLOG_VIEWED_SET:";

    /**
     * 缓存blog
     */
    public static final String BLOG_CACHE = "BLOG_CACHE:";

    /**
     * uv 键
     */
    public static final String DATE_UV = "DATE_UV:";

    /**
     * 区间uv键
     */
    public static final String RANGE_UV = "RANGE_UV:";
}
