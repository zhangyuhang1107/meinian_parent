package org.java.service;

import org.java.entity.Result;

import java.util.Map;

/**
 * @author zyhstart
 */
public interface OrderService {

    /**
     * 保存预约信息
     * @param map
     * @return
     */
    Result saveOrder(Map<String, Object> map);

    /**
     * 根据id查询预约成功信息
     * @param id
     * @return
     */
    Map<String, Object> findById(Integer id);
}
