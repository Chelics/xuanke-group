//package com.seu.mapper;
//
//import org.apache.ibatis.annotations.Mapper;
//import org.apache.ibatis.annotations.Select;
//
//@Mapper
//public interface ApplyingMapper {
//
//    /**
//     * 分页获取待审核课程信息
//     * 注意状态为1才是待审核
//     * @param
//     * @return
//     */
//    @Select("select 1 from course_status")
//    List<ApplyingCourse> listNotReviewed();
//
//    /**
//     * 分页获取已通过课程信息
//     * 注意状态为2才是已通过
//     * @param
//     * @return
//     */
//    @Select("select 2 from course_status")
//    List<ApplyingCourse> listPassed();
//
//    /**
//     * 分页获取已驳回课程信息
//     * 注意状态为3才是已通过
//     * @param
//     * @return
//     */
//    @Select("select 3 from course_status")
//    List<ApplyingCourse> listRejected();
//}
