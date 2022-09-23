package org.java.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import org.java.constant.MessageConstant;
import org.java.entity.PageResult;
import org.java.entity.QueryPageBean;
import org.java.entity.Result;
import org.java.pojo.TravelItem;
import org.java.service.TravelItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zyhstart
 */
@RestController // 组合注解 @Controller + @ResponseBody
@RequestMapping("/travelItem")
public class TravelItemController {

    // 远程服务调用
    @Reference
    TravelItemService travelItemService;

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {

        PageResult pageResult = travelItemService.findPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize(), queryPageBean.getQueryString());
        return pageResult;
    }

    // 表单项参数名称必须和实体对象的属性名称一致，提供对应的set方法，框架创建对象并封装数据
    @RequestMapping("/add")
    @PreAuthorize("hasAuthority('TRAVELITEM_ADD')")
    public Result add(@RequestBody TravelItem travelItem) { // @RequestBody 从请求体获取数据
        try {
            travelItemService.add(travelItem);
            return new Result(true, MessageConstant.ADD_TRAVELITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_TRAVELITEM_FAIL);
        }
    }

    @RequestMapping("/delete")
    @PreAuthorize("hasAuthority('TRAVELITEM_DELETE')")
    public Result delete(Integer id) { // @RequestParam("id")

        try {
            travelItemService.deleteById(id);
            return new Result(true, MessageConstant.DELETE_TRAVELITEM_SUCCESS);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.DELETE_TRAVELITEM_SUCCESS);
        }
    }

    @RequestMapping("/getById")
    public Result getById(Integer id) {

        try {
            TravelItem travelItem = travelItemService.getById(id);
            return new Result(true, MessageConstant.QUERY_TRAVELITEM_SUCCESS, travelItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_TRAVELITEM_FAIL);
        }
    }

    @RequestMapping("/edit")
    @PreAuthorize("hasAuthority('TRAVELITEM_EDIT')")//权限校验
    public Result edit(@RequestBody TravelItem travelItem) {

        try {
            travelItemService.edit(travelItem);
            return new Result(true, MessageConstant.EDIT_TRAVELITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_TRAVELITEM_FAIL);
        }
    }

    @RequestMapping("/findAll")
    public Result findAll() {

        try {
            List<TravelItem> list = travelItemService.findAll();
            return new Result(true, MessageConstant.QUERY_TRAVELITEM_SUCCESS, list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_TRAVELITEM_FAIL);
        }
    }
}
