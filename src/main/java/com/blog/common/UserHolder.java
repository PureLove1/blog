package com.blog.common;

import com.blog.pojo.User;


/**
 * 使用ThreadLocal进行数据存储
 * @author 贺畅
 * @date 2022/11/28
 */
public class UserHolder {
    /**
     * ThreadLocal存储当前线程用户信息
     */
    private static ThreadLocal<User> threadLocal = new ThreadLocal<>();

    /**
     * 设置当前线程的用户信息
     * @param user
     */
    public static void setCurrentUser(User user) {
        threadLocal.set(user);
    }

    /**
     * 获取当前线程存储的用户信息
     * @return
     */
    public static User getCurrentUser(){
        return threadLocal.get();
    }

    /**
     * 清除用户信息数据，防止内存泄露
     */
    public static void clear(){
        threadLocal.remove();
    }
}
