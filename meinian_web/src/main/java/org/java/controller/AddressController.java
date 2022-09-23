package org.java.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import org.java.constant.MessageConstant;
import org.java.entity.PageResult;
import org.java.entity.QueryPageBean;
import org.java.entity.Result;
import org.java.pojo.Address;
import org.java.service.AddressService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zyhstart
 */
@RestController
@RequestMapping("/address")
public class AddressController {

    @Reference
    AddressService addressService;

    @RequestMapping("/findAllMaps")
    public Map<String, Object> findAllMaps() {

        Map<String, Object> map = new HashMap<>();

        List<Address> list = addressService.findAllMaps();

        // 1、定义分店坐标集合
        List<Map> gridnMaps = new ArrayList<>();

        // 2. 定义分店名称集合
        List<Map<String, Object>> nameMaps = new ArrayList<>();

        for (Address address : list) {
            String addressName = address.getAddressName();
            Map<String, Object> mapName = new HashMap<>();
            // 获取地址名称
            mapName.put("addressName", addressName);

            nameMaps.add(mapName);

            Map<String, Object> gridnMap = new HashMap<>();
            // 获取经度
            gridnMap.put("lng", address.getLng());
            // 获取纬度
            gridnMap.put("lat", address.getLat());

            gridnMaps.add(gridnMap);
        }

        map.put("gridnMaps", gridnMaps);
        map.put("nameMaps", nameMaps);

        return map;
    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {

        PageResult pageResult = addressService.findPage(queryPageBean);
        return pageResult;
    }

    @RequestMapping("/addAddress")
    public Result addAddress(@RequestBody Address address) {

        try {
            addressService.addAddress(address);
            return new Result(true, MessageConstant.ADD_ADDRESS_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_ADDRESS_FAIL);
        }
    }

    @RequestMapping("/deleteById")
    public Result deleteById(Integer id) {

        try {
            addressService.deleteById(id);
            return new Result(true, MessageConstant.DELETE_ADDRESS_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_ADDRESS_FAIL);
        }
    }
}
