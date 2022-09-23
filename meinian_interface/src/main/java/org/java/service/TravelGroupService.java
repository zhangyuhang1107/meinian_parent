package org.java.service;

import org.java.entity.PageResult;
import org.java.pojo.TravelGroup;

import java.util.List;

/**
 * @author zyhstart
 */
public interface TravelGroupService {

    void add(Integer[] travelItemIds, TravelGroup travelGroup);


    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    TravelGroup getById(Integer id);

    List<Integer> getTravelItemIdsByTravelGroupId(Integer travelGroupId);

    void edit(Integer[] travelItemIds, TravelGroup travelGroup);

    void deleteById(Integer id);

    List<TravelGroup> findAll();

}
