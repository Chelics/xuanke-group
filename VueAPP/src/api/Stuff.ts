import request from '@/util/request';

interface ResponseData {
  code: number;
  msg: string;
  data: any;
}

// 更新阶段数据的请求函数

function formatToISOString(date: Date): string {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0'); // getMonth returns 0-11
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  const seconds = String(date.getSeconds()).padStart(2, '0');

  return `${year}-${month}-${day}T${hours}:${minutes}:${seconds}`;
}

export async function updateState(stage: any) {
  let formattedStartTime, formattedEndTime;

  if (stage.startTime) {
    if (typeof stage.startTime === 'string') {
      // If startTime is a string, attempt to parse it into a Date before formatting
      const parsedDate = new Date(stage.startTime);
      if (!isNaN(parsedDate.getTime())) {
        formattedStartTime = formatToISOString(parsedDate);
      } else {
        console.error('Invalid startTime string could not be parsed to Date:', stage.startTime);
      }
    } else if (stage.startTime instanceof Date) {
      // If startTime is already a Date, just format it
      formattedStartTime = formatToISOString(stage.startTime);
    }
  }

  if (stage.endTime) {
    if (typeof stage.endTime === 'string') {
      // Similar check and handling for endTime
      const parsedDate = new Date(stage.endTime);
      if (!isNaN(parsedDate.getTime())) {
        formattedEndTime = formatToISOString(parsedDate);
      } else {
        console.error('Invalid endTime string could not be parsed to Date:', stage.endTime);
      }
    } else if (stage.endTime instanceof Date) {
      formattedEndTime = formatToISOString(stage.endTime);
    }
  }

  const updateData = {
    id: stage.id,
    ...(formattedStartTime && { startTime: formattedStartTime }),
    ...(formattedEndTime && { endTime: formattedEndTime }),
  }

  try {
    const response: ResponseData = await request.put("/staff/stages", updateData)
    if (response?.code === 1 && response?.msg === "success") {
      return response.data
    } else {
      throw new Error(response?.msg || '修改阶段数据失败')
    }
  }
  catch (error) {
    console.error(error);
    //直接处理未知错误 error;
    throw(error)
  }
}
export async function updateName(name: string) {
  try {
    const response: ResponseData = await request.put("/staff/stages/term", name)
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