package org.java.service;

import org.java.entity.PageResult;
import org.java.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * @author zyhstart
 */
public interface SetmealService {

    void add(Integer[] travelgroupIds, Setmeal setmeal);

    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    void deleteById(Integer id);

    List<Setmeal> findAll();

    Setmeal findById(Integer id);

    Setmeal getSetmealById(Integer id);

    /**
     * 统计套餐预约人数占比
     * @return
     */
    List<Map<String, Object>> getSetmealReport();

    /**
     * 根据套餐游id查询跟团游id集合
     * @param id
     * @return
     */
    List<Integer> getTravelGroupIdsBySetmealId(Integer id);

    /**
     * 修改套餐
     * @param setmeal
     * @param travelGroupIds
     */
    void update(Setmeal setmeal, Integer[] travelGroupIds);
}
