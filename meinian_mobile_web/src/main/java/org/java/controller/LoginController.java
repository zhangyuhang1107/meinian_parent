package org.java.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import org.java.constant.MessageConstant;
import org.java.constant.RedisMessageConstant;
import org.java.entity.Result;
import org.java.pojo.Member;
import org.java.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * @author zyhstart
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    JedisPool jedisPool;
    @Reference
    MemberService memberService;

    @RequestMapping("/check")
    public Result check(@RequestBody Map<String, Object> map, HttpServletResponse response) {

        // 手机号码
        String telephone = (String) map.get("telephone");
        // 验证码
        String validateCode = (String) map.get("validateCode");

        // 1. 从redis中获取缓存验证码，并判断是否正确
        String redisCode = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);

        if (redisCode == null || !validateCode.equals(redisCode)) {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        } else {
            // 2. 判断当前用户是否为会员
            Member member = memberService.findByTelephone(telephone);
            if (member == null) {
                // 快速注册
                member = new Member();
                member.setPhoneNumber(telephone);
                member.setRegTime(new Date());
                memberService.add(member);
            }
            // 3. 登录成功，写入cookie，跟踪用户
            Cookie cookie = new Cookie("login_member_telephone", telephone);
            cookie.setPath("/");
            // 有效时间 30 天（单位秒）
            cookie.setMaxAge(60 * 60 * 24 * 60);
            response.addCookie(cookie);

            return new Result(true, MessageConstant.LOGIN_SUCCESS);
        }


    }
}
