import request from '@/util/request';

interface ResponseData {
  code: number;
  msg: string;
  data: any;
}

// 更新阶段数据的请求函数

export async function updateState(stage: any) {
  const updateData = {
    id: stage.id,
    ...(stage.startTime && { startTime: stage.startTime }),
    ...(stage.endTime && { endTime: stage.endTime }),
  }

  try {
    const response: ResponseData = await request.post("/staff/stages", updateData)
    if (response.code === 1 && response.msg === "success") {
      return response.data
    } else {
      throw new Error(response.msg || '修改阶段数据失败')
    }
  }
  catch (error) {
    console.error(error);
    //直接处理未知错误 error;
    alert(error)
  }
}
export async function updateName(name: string) {
  try {
    const response: ResponseData = await request.post("/staff/stages/term", name)
    if (response.code === 1 && response.msg === "success") {
      return response.data
    } else {
      throw new Error(response.msg || '修改阶段数据失败')
    }
  }
  catch (error) {
    console.error(error);
    //直接处理未知错误 error;
    alert(error)
  }
}