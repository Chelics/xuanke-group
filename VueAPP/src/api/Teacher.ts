import request from '@/util/request'


//已申请课程列表
export const ApplyedCourseListService = ()=>{
    return request.get('/teacher/course')
}

//新增申请课程
export const ApplyedCourseAddService = (courseAddData:any)=>{
    return request.post('/teacher/apply',courseAddData)
}

//查询
export const ApplyedCourseSearchService = (params:any)=>{
    return request.get('/teacher/search',{params:params})
}
