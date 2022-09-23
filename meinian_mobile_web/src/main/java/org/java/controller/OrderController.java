package org.java.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import org.java.constant.MessageConstant;
import org.java.constant.RedisMessageConstant;
import org.java.entity.Result;
import org.java.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * @author zyhstart
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    OrderService orderService;

    @Autowired
    JedisPool jedisPool;

    /**
     * 由于页面表单数据来自多张表单数据操作，用pojo对象接收，接收不完整，就用map接收
     * 1. 点击提交预约，把用户信息提交到服务器
     * 2. controller里面，获得用户信息
     * - 校验验证码（redis里面存的和用户输入的进行比较）
     * - 调用业务，进行预约，响应
     *
     * @param map
     * @return
     */
    @RequestMapping("/saveOrder")
    public Result saveOrder(@RequestBody Map<String, Object> map) {

        try {
//            System.out.println("map = " + map);
            // 获取手机号
            String telephone = (String) map.get("telephone");
            // 获取用户输入的验证码
            String validateCode = (String) map.get("validateCode");
            String redisCode = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);

            if (redisCode == null || !validateCode.equals(redisCode)) {
                return new Result(false, MessageConstant.VALIDATECODE_ERROR);
            }

            Result result = orderService.saveOrder(map);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDER_FAIL);
        }
    }

    @RequestMapping("/findById")
    public Result findById(Integer id) {

        try {
            Map<String, Object> map = orderService.findById(id);
            // 查询预约信息成功
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}
