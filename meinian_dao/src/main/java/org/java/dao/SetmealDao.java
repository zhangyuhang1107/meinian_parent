package org.java.dao;

import com.github.pagehelper.Page;
import org.java.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * @author zyhstart
 */
public interface SetmealDao {

    void add(Setmeal setmeal);

    void addSetmealAndTravelGrop(Map<String, Integer> map);

    Page<Setmeal> findPage(String queryString);

    void deleteById(Integer id);

    Long findCountBySetmealId(Integer id);

    List<Setmeal> findAll();

    Setmeal findById(Integer id);

    Setmeal getSetmealById(Integer id);

    List<Map<String, Object>> getSetmealReport();
}
