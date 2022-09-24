package org.java.dao;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
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

    @MapKey("name")
    List<Map<String, Object>> getSetmealReport();

    List<Integer> getTravelGroupIdsBySetmealId(Integer id);

    void update(Setmeal setmeal);

    void deleteTravelGroupIdBySetmealId(Integer id);

    void setSetmealIdAndtravelGroupIds(@Param("paramData") List<Map<String, Object>> paramData);
}
