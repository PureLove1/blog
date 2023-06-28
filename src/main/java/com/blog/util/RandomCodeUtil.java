package com.blog.util;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author PureLove1
 * @author 贺畅
 */
public class RandomCodeUtil {

    /**
     * 生成随机验证码
     * @return
     */
    public static String randomCode(){
        // 避免 Random 实例被多线程使用，虽然共享该实例是线程安全的，但会因竞争同一 seed导致的性能下降
        int i = ThreadLocalRandom.current().nextInt(0, 999999);
        return String.valueOf(i);
    }

}
