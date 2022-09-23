package org.java.controller;

import org.java.constant.MessageConstant;
import org.java.constant.RedisMessageConstant;
import org.java.entity.Result;
import org.java.util.SMSUtils;
import org.java.util.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

/**
 * @author zyhstart
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    JedisPool jedisPool;

    /**
     * 预约发送短信
     * @param telephone
     * @return
     */
    @RequestMapping("/send4Order")
    public Result send4Order(String telephone) {

        try {
            // 生成四位验证码
            Integer code = ValidateCodeUtils.generateValidateCode(4);
            // 发送验证码到手机号
            SMSUtils.sendShortMessage(telephone, code.toString());
            // 将验证码存储到redis中，进行后期验证，五分钟有效
            jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_ORDER, 5 * 60, code.toString());

            System.out.println("telephone = " + telephone + " code = " + code);
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }

    /**
     * 登录发送短信
     * @param telephone
     * @return
     */
    @RequestMapping("/send4Login")
    public Result send4Login(String telephone) {

        try {
            // 生成4位验证码
            Integer code = ValidateCodeUtils.generateValidateCode(4);
            // 发送验证码到手机号
            SMSUtils.sendShortMessage(telephone, code.toString());
            // 将验证码存储到redis中，进行后期验证，五分钟有效
            jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_LOGIN, 5 * 60, code.toString());

            System.out.println("telephone = " + telephone + " code" + code);
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }
}
