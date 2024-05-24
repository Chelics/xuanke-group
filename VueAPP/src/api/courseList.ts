interface ResponseData {
    code: number;
    msg: string;
    data: any;
  }
  import request from '@/util/request';
export async function getCourseList(url: string) {
    try {
      const response: ResponseData = await request.get(url)
      if (response.code === 1 && response.msg === "success") {//对象不存在时，返回false
        return response.data
      } else {
        throw new Error(response.msg || '获取数据失败')
      }
    }
    catch (error) {
      console.error(error);
      //直接处理未知错误 error;
      alert(error)
    }
  }