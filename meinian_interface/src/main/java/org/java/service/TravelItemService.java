package org.java.service;

import org.java.entity.PageResult;
import org.java.pojo.TravelItem;

import java.util.List;

/**
 * @author zyhstart
 */
public interface TravelItemService { // ctl + alt + b
    void add(TravelItem travelItem);

    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    void deleteById(Integer id);

    TravelItem getById(Integer id);

    void edit(TravelItem travelItem);

    List<TravelItem> findAll();
}
