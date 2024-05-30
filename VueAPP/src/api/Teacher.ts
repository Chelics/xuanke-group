import service from '@/util/request';


export async function courseGetAllService(){
    //return await axios.get('https://mock.apifox.com/m2/4461960-4108146-default/175216009/')
    return service.get('/teacher/apply')
    .then(result=>{
        return result.data;
    }).catch(err=>{
        console.log(err);
    });
}


export async function teacherIDsGetService(teacherName) {
    //return axios.get('https://mock.apifox.com/m1/4461960-4108146-default/teacher/teachers',{params:{...teacherName.value}})
    return service.get('/teacher/teachers',{params:{...teacherName.value}})
    .then(result=>{
        return result.data;
    }).catch(err=>{
        console.log(err);
    })
}


export async function classIDsGetService(className) {
    return service.get('/teacher/classes',{params:{...className.value}})
    .then(result=>{
        return result.data;
    }).catch(err=>{
        console.log(err);
    })
}

export const courseAddService = (courseData=>{
    //return service.post('https://mock.apifox.com/m2/4461960-4108146-default/175216824',courseData);
    return service.post('/teacher/apply',courseData);
})