import axios from 'axios';

export async function courseGetAllService(){
    return await axios.get('https://mock.apifox.com/m2/4461960-4108146-default/175216009/')
    //return axios.get('https:localhost:8080/teacher/apply')
    .then(result=>{
        return result.data;
    }).catch(err=>{
        console.log(err);
    });
}

export async function courseSearchStatusService(conditions){
   //return axios.get('https://mock.apifox.com/m2/4461960-4108146-default/175216009',{params:{...conditions.value}})
   return axios.get('https://mock.apifox.com/m2/4461960-4108146-default/175216009?status=${conditions.status}&page=${conditions.page}&pageSize=${conditions.pageSize}',{params:{...conditions.value}})
    .then(result=>{
        return result.data;
    }).catch(err=>{
        console.log(err);
    })
}

export async function teacherIDsGetService(teacherName) {
    return axios.get('https://mock.apifox.com/m1/4461960-4108146-default/teacher/teachers',{params:{...teacherName.value}})
    .then(result=>{
        return result.data;
    }).catch(err=>{
        console.log(err);
    })
}


export async function classIDsGetService(className) {
    return axios.get('https://mock.apifox.com/m1/4461960-4108146-default/teacher/classes',{params:{...className.value}})
    .then(result=>{
        return result.data;
    }).catch(err=>{
        console.log(err);
    })
}

export const courseAddService = (courseData=>{
    return axios.post('https://mock.apifox.com/m2/4461960-4108146-default/175216824',courseData);
})