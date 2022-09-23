package org.java.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import org.java.constant.MessageConstant;
import org.java.entity.Result;
import org.java.pojo.Setmeal;
import org.java.service.SetmealService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zyhstart
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealMobileController {

    @Reference
    SetmealService setmealService;

    @RequestMapping("/getSetmeal")
    public Result getSetmeal() {

        try {
            List<Setmeal> list = setmealService.findAll();
            return new Result(true, MessageConstant.QUERY_SETMEALLIST_SUCCESS, list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.QUERY_SETMEALLIST_SUCCESS);
        }
    }

    /**
     * 查询套餐游以及自由行和跟团游
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(Integer id) {

        try {
            Setmeal setmeal = setmealService.findById(id);
            return new Result(true, MessageConstant.QUERY_SETMEALLIST_SUCCESS, setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }

    /**
     * 只查询套餐
     * @param id
     * @return
     */
    @RequestMapping("/getSetmealById")
    public Result getSetmealById(Integer id) {

        try {
            Setmeal setmeal = setmealService.getSetmealById(id);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }
}
