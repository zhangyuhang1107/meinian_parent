package org.java.dao;

import com.github.pagehelper.Page;
import org.java.pojo.TravelItem;

import java.util.List;

/**
 * @author zyhstart
 */
public interface TravelItemDao {

    void add(TravelItem travelItem);

    Page<TravelItem> findPage(String queryString);

    void deleteById(Integer id);

    TravelItem getById(Integer id);

    void edit(TravelItem travelItem);

    List<TravelItem> findAll();

    long findCountByTravelitemId(Integer id);

    /**
     * 帮助封装跟团游的travelItems属性方法
     * @param id
     * @return
     */
    List<TravelItem> findTravelItemById(Integer id);
}
