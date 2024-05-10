package com.seu.mapper;

import com.seu.pojo.Room;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StaffRoomMapper {
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
}
