package org.java.service;

import org.java.entity.PageResult;
import org.java.entity.QueryPageBean;
import org.java.pojo.Address;

import java.util.List;

/**
 * @author zyhstart
 */
public interface AddressService {

    /**
     * 加载所有地图
     * @return
     */
    List<Address> findAllMaps();

    /**
     * 分页
     * @param queryPageBean
     * @return
     */
    PageResult findPage(QueryPageBean queryPageBean);

    /**
     * 添加地址
     * @param address
     */
    void addAddress(Address address);

    /**
     * 根据id删除地址
     * @param id
     */
    void deleteById(Integer id);
}
