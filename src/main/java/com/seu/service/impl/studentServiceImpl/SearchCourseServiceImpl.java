package com.seu.service.impl.studentServiceImpl;

import com.seu.dto.response.FullFullCourse;
import com.seu.mapper.*;
import com.seu.service.impl.CourseServiceImpl;
import com.seu.service.studentService.SearchCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SearchCourseServiceImpl implements SearchCourseService {
    @Autowired
    CourseMapper courseMapper;
    @Autowired
    CourseServiceImpl courseService;
    @Autowired
    TeacherMapper teacherMapper;
    @Autowired
    CourseTeacherMapper courseTeacherMapper;
    @Autowired
    RoomMapper roomMapper;
    @Autowired
    CourseStudentMapper courseStudentMapper;

    private static final Pattern PATTERN_BUILDINGROOM = Pattern.compile("教\\d-\\d\\d\\d");
    private static final Pattern PATTERN_BUILDING = Pattern.compile("教\\d");
    private static final Pattern PATTERN_ROOM = Pattern.compile("\\d\\d\\d");

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<FullFullCourse> searchCoursesByKeyWord(String keyWord) {

        //教师名的搜索结果
        List<Integer> teacherIdsList = teacherMapper.searchByName(keyWord);
        List<Integer> idsSearchedByTeacher = courseTeacherMapper.getCoursesByTeachers(teacherIdsList);

        //教室名的搜索结果
        List<Integer> idsSearchedByRooms = searchByRoom(keyWord);

        //再加上课程名, 课程编号, 学院, 一起去数据库中查
        List<FullFullCourse> fullCourseList = courseMapper.searchCoursesByKeyWord(keyWord, idsSearchedByTeacher, idsSearchedByRooms);
        courseService.getFullsByBasics(fullCourseList);
        for(FullFullCourse course : fullCourseList){
            Integer studentNum = courseStudentMapper.getStudentCountForCourse(course.getId());
            course.setStudentsNum(Objects.requireNonNullElse(studentNum, 0));
        }
        return fullCourseList;
    }

    /**
     * 根据与关键词有关的教室搜索对应的课程
     * @param keyWord
     * @return
     */
    private List<Integer> searchByRoom(String keyWord){
        Matcher matcherBuildingRoom = PATTERN_BUILDINGROOM.matcher(keyWord);
        Matcher matcherBuilding = PATTERN_BUILDING.matcher(keyWord);
        Matcher matcherRoom = PATTERN_ROOM.matcher(keyWord);

        String buildingCode = "";
        String roomNum = "";

        if(matcherBuildingRoom.matches()){  //输入"教1-101时
            buildingCode = keyWord.substring(0, 1);
            roomNum = keyWord.substring(3);
        }else if(matcherBuilding.matches()){    //输入"教1"
            buildingCode = keyWord;
        }else if(matcherRoom.matches()){    //输入"101"
            roomNum = keyWord;
        }

        return roomMapper.getIdsByName(buildingCode, roomNum);
    }
}
