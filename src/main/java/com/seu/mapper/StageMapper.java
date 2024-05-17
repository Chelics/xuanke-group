package com.seu.mapper;

import com.seu.pojo.Stage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
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
     * 查询未审核阶段
     * @return
     */
    @Select("select 1 from stage")
    List<Stage> listNotReviewed();

    /**
     * 查询已通过阶段
     * @return
     */
    @Select("select 2 from stage")
    List<Stage> listPassed();

    /**
     * 查询已驳回阶段
     * @return
     */
    @Select("select 3 from stage")
    List<Stage> listRejected();

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

    /**
     * 根据时间获取阶段
     * @param time
     * @return
     */
    @Select("SELECT * FROM stage WHERE start_time<=#{time} AND end_time>=#{time}")
    Stage getCurrStage(LocalDateTime time);

}
