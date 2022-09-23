package org.java.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import org.java.constant.MessageConstant;
import org.java.constant.RedisConstant;
import org.java.entity.PageResult;
import org.java.entity.QueryPageBean;
import org.java.entity.Result;
import org.java.pojo.Setmeal;
import org.java.service.SetmealService;
import org.java.util.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.UUID;

/**
 * @author zyhstart
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    SetmealService setmealService;
    @Autowired
    JedisPool jedisPool;

    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile) {

        try {
            // 1. 获取原始文件名称
            String originalFilename = imgFile.getOriginalFilename();

            // 2. 生成新的文件（防止上传文件同名文件覆盖）
            int index = originalFilename.lastIndexOf("."); // xxx.jpg
            String suffix = originalFilename.substring(index); // .jpg
            String filename = UUID.randomUUID().toString().replace("-", "") + suffix;

            // 3. 调用工具类，上传图片到七牛云
            QiniuUtils.upload2Qiniu(imgFile.getBytes(), filename);

            // *********************** 补充代码，解决七牛云垃圾图片的问题
            // 将上传图片名称存入redis，基于redis的set集合存储
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES, filename);
            // ***********************

            // 4. 返回结果
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, filename);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    @RequestMapping("/add")
    @PreAuthorize("hasAuthority('SETMEAL_ADD')")
    public Result add(Integer[] travelgroupIds, @RequestBody Setmeal setmeal) {

        try {
            setmealService.add(travelgroupIds, setmeal);
            return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {

        PageResult pageResult = setmealService.findPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize(), queryPageBean.getQueryString());
        return pageResult;
    }

    @RequestMapping("/deleteById")
    @PreAuthorize("hasAuthority('SETMEAL_DELETE')")
    public Result deleteById(Integer id) {

        try {
            setmealService.deleteById(id);
            return new Result(true, MessageConstant.DELETE_SETMEAL_SUCCESS);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_SETMEAL_FAIL);
        }

    }
}
