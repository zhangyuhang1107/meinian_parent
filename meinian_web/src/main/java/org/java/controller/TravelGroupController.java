package org.java.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import org.java.constant.MessageConstant;
import org.java.entity.PageResult;
import org.java.entity.QueryPageBean;
import org.java.entity.Result;
import org.java.pojo.TravelGroup;
import org.java.service.TravelGroupService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zyhstart
 */
@RestController
@RequestMapping("/travelGroup")
public class TravelGroupController {

    @Reference
    TravelGroupService travelGroupService;

    @RequestMapping("/add")
    @PreAuthorize("hasAuthority('TRAVELGROUP_ADD')")
    public Result add(Integer[] travelItemIds, @RequestBody TravelGroup travelGroup) { // 接收两部分数据

        try {
            travelGroupService.add(travelItemIds, travelGroup);
            return new Result(true, MessageConstant.ADD_TRAVELGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_TRAVELGROUP_FAIL);
        }
    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {

        PageResult pageResult = travelGroupService.findPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize(), queryPageBean.getQueryString());
        return pageResult;
    }

    @RequestMapping("/getById")
    public Result getById(Integer id) {

        try {
            TravelGroup travelGroup = travelGroupService.getById(id);
            return new Result(true, MessageConstant.QUERY_TRAVELGROUP_SUCCESS, travelGroup);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_TRAVELGROUP_FAIL);
        }
    }

    @RequestMapping("/getTravelItemIdsByTravelGroupId")
    public Result getTravelItemIdsByTravelGroupId(Integer travelGroupId) {

        try {
            List<Integer> travelItemIds = travelGroupService.getTravelItemIdsByTravelGroupId(travelGroupId);
            return new Result(true, "根据跟团游查询自由行成功", travelItemIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "根据跟团游查询自由行失败");
        }
    }

    @RequestMapping("/edit")
    @PreAuthorize("hasAuthority('TRAVELGROUP_EDIT')")
    public Result edit(Integer[] travelItemIds, @RequestBody TravelGroup travelGroup) {

        try {
            travelGroupService.edit(travelItemIds, travelGroup);
            return new Result(true, MessageConstant.EDIT_TRAVELITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_TRAVELITEM_FAIL);
        }
    }

    @RequestMapping("/deleteById")
    @PreAuthorize("hasAuthority('TRAVELGROUP_DELETE')")
    public Result deleteById(Integer id) {

        try {
            travelGroupService.deleteById(id);
            return new Result(true, MessageConstant.DELETE_TRAVELGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_TRAVELGROUP_FAIL);
        }
    }

    @RequestMapping("/findAll")
    public Result findAll() {

        try {
            List<TravelGroup> travelGroupList = travelGroupService.findAll();
            return new Result(true, MessageConstant.QUERY_TRAVELGROUP_SUCCESS, travelGroupList);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_TRAVELGROUP_FAIL);
        }

    }
}
