package com.blog.controller;

import com.blog.annotation.HasAnyRole;
import com.blog.common.Result;
import com.blog.common.UserHolder;
import com.blog.pojo.FastDFSFile;
import com.blog.pojo.User;
import com.blog.util.FastDfsClient;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;

import static com.blog.constant.StatusCode.*;
import static com.blog.constant.UserRole.ROLE_VIP;


/**
 * @author 贺畅
 * @date 2022/11/28
 */
//跨域:
//不同的域名A 访问 域名B 的数据就是跨域
// 端口不同 也是跨域  loalhost:18081----->localhost:18082
// 协议不同 也是跨域  http://www.jd.com  --->  https://www.jd.com
// 域名不同 也是跨域  http://www.jd.com  ---> http://www.taobao.com
// 协议一直,端口一致,域名一致就不是跨域
// http://www.jd.com:80 --->http://www.jd.com:80 不是跨域
@RequestMapping("/file")
@RestController
//@CrossOrigin//支持跨域
public class FileController {
    /**
     * 支持的文件类型
     */
    private static HashSet<String> filenameExtensions = new HashSet();

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);
    static {
        filenameExtensions.add("png");
        filenameExtensions.add("jpg");
        filenameExtensions.add("jpeg");
    }
    /**
     * 返回 图片的全路径
     * @param file 页面的文件对象
     * @return
     */
    @ApiOperation("上传博文图片")
    @PostMapping
    @HasAnyRole(ROLE_VIP)
    public Result upload(@RequestParam(value = "file") MultipartFile file) {
        User currentUser = UserHolder.getCurrentUser();
        if (currentUser==null) {
            return Result.error("用户尚未登陆",USER_LOGIN_ERROR);
        }
        try {
            //获取文件扩展名
            String filenameExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
            if (!filenameExtensions.contains(filenameExtension)){
                logger.error("上传的文件类型错误{}",filenameExtension);
                return Result.error("不支持的文件类型",USER_UPLOAD_FILE_TYPE_UNMATCHED_ERROR);
            }
            logger.info("上传文件："+file.getOriginalFilename());
            //1. 创建图片文件对象(封装)
            FastDFSFile fastdfsfile = new FastDFSFile(
                    //原来的文件名
                    file.getOriginalFilename(),
                    //文件本身的字节数组
                    file.getBytes(),
                    //获取文件扩展名
                    filenameExtension
            );
            //2. 调用工具类实现图片上传
            String[] upload = FastDfsClient.upload(fastdfsfile);
            //3. 拼接图片的全路径返回
            logger.info("文件上传成功，返回图片请求地址"+FastDfsClient.getTrackerUrl()+"/"+upload[0]+"/"+upload[1]);
            return Result.ok(FastDfsClient.getTrackerUrl()+"/"+upload[0]+"/"+upload[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.error(LocalDateTime.now() + "上传文件过程中出错");
        return Result.error("文件上传过程中出错！",SYSTEM_EXECUTION_ERROR);
    }

    @ApiOperation("删除博文图片")
    @DeleteMapping
    public Result deleteFile(@RequestParam String url){
        User currentUser = UserHolder.getCurrentUser();
        if (currentUser==null){
            return Result.error("删除失败，权限不足");
        }
        if (FastDfsClient.deleteFile(url)) {
            return Result.ok("删除成功！");
        }
        return Result.error("删除失败");
    }

}
