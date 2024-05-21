package com.seu.mapper;

import com.seu.pojo.Room;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RoomMapper {
    /**
     * 分页获取列表数据
     * @param building
     * @param roomName
     * @param storageBegin
     * @param storageEnd
     * @return
     */
    //@Select("select * from room")
    List<Room> list(@Param("building")String building, @Param("roomName")String roomName, @Param("begin")Short storageBegin, @Param("end")Short storageEnd);

    /**
     * 分页获取列表数据, 但是只要id
     * @param building
     * @param roomName
     * @param storageBegin
     * @param storageEnd
     * @return
     */
    List<Integer> idList(@Param("building")String building, @Param("roomName")String roomName, @Param("begin")Short storageBegin, @Param("end")Short storageEnd);


    /**
     * 根据ID查询教学楼-教室名
     * @param roomId
     * @return
     */
    @Select("SELECT CONCAT(building, '-', room_name) AS roomDetails FROM room WHERE id = #{roomId}")
    String getRoomById(Integer roomId);

    /**
     * 根据教学楼和教室编号搜索教室id
     * @param building
     * @param roomName
     * @return
     */
    List<Integer> getIdsByName(@Param("building")String building, @Param("roomName")String roomName);
}
