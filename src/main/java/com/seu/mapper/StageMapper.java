package com.seu.mapper;

import com.seu.pojo.Stage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface StageMapper {

    /**
     * 查询全部阶段
     * @return
     */
    @Select("select * from stage")
    List<Stage> list();

    /**
     * 根据ID查询阶段
     * @param id
     * @return
     */
    @Select("select * from stage where id=#{id}")
    Stage getById(Integer id);

    /**
     *修改阶段时间
     * @param stage
     */
    @Update("update stage set start_time=#{startTime},end_time=#{endTime} where id=#{id}")
    void update(Stage stage);

    /**
     * 回显学期名
     * @return
     */
    @Select("select * from stage where id=3")
    Stage getTerName();

    /**
     * 修改学期名
     * @param stage
     */
    @Update("update stage set stage_name=#{stageName} where id=#{id}")
    void updateTermName(Stage stage);
}
