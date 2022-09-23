package org.java.dao;

import com.github.pagehelper.Page;
import org.java.pojo.TravelGroup;

import java.util.List;
import java.util.Map;

/**
 * @author zyhstart
 */
public interface TravelGroupDao {

    void add(TravelGroup travelGroup);

    void addTravelGroupAndTravelItem(Map<String, Integer> map);

    Page<TravelGroup> findPage(String queryString);

    TravelGroup getById(Integer id);

    List<Integer> getTravelItemIdsByTravelGroupId(Integer travelGroupId);

    void edit(TravelGroup travelGroup);

    void delete(Integer travelGroupId);

    void deleteById(Integer id);

    List<TravelGroup> findAll();

    long findCountByTravelgroupId(Integer id);

    /**
     * 帮助封装套餐对象的travelGroups属性方法
     * @param id
     * @return
     */
    List<TravelGroup> findTravelGroupById(Integer id);
}
