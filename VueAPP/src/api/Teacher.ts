import axios, { AxiosRequestConfig, AxiosResponse } from 'axios';

export interface Conditions {
    value: {
        status?: string;
        page: number;
        pageSize: number;
    };
}

export async function courseSearchStatusService(conditions: Conditions): Promise<any> {
    const token = 'eyJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoi5byg6ICB5LiJIiwiaWQiOjEsInVzZXJuYW1lIjoiMTEyMzQ1Njc4IiwiZXhwIjoxNzE2NTUyMjA0fQ.CyVLdzXw53ttwc43IUncuJl4pVqNEBUQUEQFSIgciek';

    const config: AxiosRequestConfig = {
        headers: {
            'Authorization': `Bearer ${token}`,
            'User-Agent': 'Apifox/1.0.0 (https://apifox.com)',
            'Accept': '*/*',
            'Host': 'localhost:8080',
            'Connection': 'keep-alive'
        },
        params: {
            ...conditions.value
        }
    };

    try {
        const response: AxiosResponse = await axios.get('http://localhost:8080/teacher/apply', config);
        return response.data;
    } catch (error) {
        console.error(error);
        throw error;
    }
}

export async function courseGetAllService(){
    return await axios.get('https://mock.apifox.com/m2/4461960-4108146-default/175216009/')
    //return axios.get('https:localhost:8080/teacher/apply')
    .then(result=>{
        return result.data;
    }).catch(err=>{
        console.log(err);
    });
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