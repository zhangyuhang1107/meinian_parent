package org.java.job;

import org.java.constant.RedisConstant;
import org.java.util.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * @author zyhstart
 */
public class ClearImgJob {

    @Autowired
    JedisPool jedisPool;

    public void clearImg() {

        // 计算redis中两个集合的差集，获取垃圾图片的名称
        Set<String> pics = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);

        for (String pic : pics) {
            System.out.println("删除垃圾图片：pic = " + pic);
            // 删除图片服务器中的图片文件
            QiniuUtils.deleteFileFromQiniu(pic);
            // 删除redis中数据
            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES, pic);
        }
    }
}
