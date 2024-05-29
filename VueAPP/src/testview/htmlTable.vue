<template>
  <h1>第{{ currentWeekIndex +1}}周</h1>
  <el-select v-model="currentWeekIndex" placeholder="Select" style="width: 240px">
      <el-option
        v-for="item in options"
        :key="item.value"
        :label="item.label"
        :value="item.value"
      />
    </el-select>
    <el-table :data="flattenedData" class="course-table" border style="width: 100%" :span-method="mergeCells">
      <el-table-column prop="time" label="时间">
        <template #default="{ row }">
          <!-- 添加节次信息 -->
          {{ getPeriod(row.time) }}<br>
          {{ row.time }}
        </template>
      </el-table-column>
      <el-table-column v-for="(day, index) in courseSchedule.days" :key="index" :label="day">
  <template #default="{ row }">
    <div v-if="row[index]?.shouldRender"
    :style="{ backgroundColor: randomLightColor() }"
    :class="'course-cell'" >
      {{ row[index].course?.course }}<br>
      {{ row[index].course?.startPeriod+1}}-{{ row[index].course?.endPeriod+1}}节<br>
      {{ row[index].course?.location }}<br>
      {{ row[index].course?.teacher }}
    </div>
  </template>
</el-table-column>
    </el-table>
  </template>
  
  <script lang="ts" setup>
    import { computed } from 'vue';
    import { ElTable, ElTableColumn ,ElSelect} from 'element-plus';
    import { getCourseList } from '@/api/courseList';
    import request from '@/util/request';
    import { ref,onMounted } from 'vue';
import axios from 'axios';
import service from '@/util/request';
import { defineProps } from 'vue';
    const options = [
  {
    value: 0,
    label: 1,
  },
  {
    value: 1,
    label: 2,
  },
  {
    value: 2,
    label: 3,
  },
  {
    value: 3,
    label: 4,
  },
  {
    value: 4,
    label: 5,
  },
  {
    value: 5,
    label: 6,
  },
  {
    value: 6,
    label: 7,
  },
  {
    value: 7,
    label: 8,
  },
  {
    value: 8,
    label: 9,
  },
  {
    value: 9,
    label: 10,
  },
  {
    value: 10,
    label: 11,
  },
  {
    value: 11,
    label: 12,
  },
  {
    value: 12,
    label: 13,
  },
  {
    value: 13,
    label: 14,
  },
  {
    value: 14,
    label: 15,
  },
  {
    value: 15,
    label: 16,
  },
]
    interface ResponseData {
  code: number;
  msg: string;
  data: any;
}
    //定义参数，接收一个字符串，作为查询课程数据的api数据
    interface Props {
  myurl: string;
}
//对外界暴露的参数接收，按照这个格式传参数
const props =defineProps<Props>();
// 定义 Course 接口
interface rawCourse {
  id: number;
  courseName: string;
  type: number;
  courseNumber: string;
  roomId: number;
  courseHour: number;
  courseStorage: number;
  startWeek: number;
  endWeek: number;
  time1: number;
  time2?: number;
  time3?: number;
  faculty: string;
  credit: number;
  teachers: string[];
  classes: string[];
  roomName: string;
  //studentNum: number
}

// 定义 Data 接口，包含 courseList 数组
/* interface rawData {
  courseList: rawCourse[];
} */
//rawData:rawData
// 使用ref创建响应式数据，这里就是多周测试数据
let rawCourseData = ref<rawCourse[]>([
]);

// 在组件挂载后调用异步函数获取数据
async function getRawCourseData() {
  try {
    console.log("开始获取")
    console.log(`/${props.myurl}`)
    //测试 
    const response :ResponseData= await service.get("https://mock.apifox.com/m1/4461960-4108146-default/student/course/selected")
      //const response :ResponseData= await service.get(`${props.myurl}`)
      //console.log(response)
      if (response.code === 1 && response.msg === 'success'){
        rawCourseData.value = response.data as rawCourse[]
      }
      else {
        alert(response)
      }
    }
    catch (error) {
        console.error(error);
        //直接处理未知错误 error;
        alert( error)
      }
}
//处理数据,把后端的数据转化为前端方便的形式
function processRawData(rawCourseData:rawCourse[]) {
  let courseList  = rawCourseData;

  // 初始化 courseSchedule.data，确保其大小足够
  const dataSize = courseSchedule.weeks.length;
  for (let i = 0; i < dataSize; i++) {
    if (!courseSchedule.data[i]) courseSchedule.data[i] = new Array(courseSchedule.days.length);
  }

  // 处理时间映射关系
  const periodMapping: { [key: number]: [number, number] } = {
    0: [0, 1], 1: [2, 3], 2: [2, 4],  // 上午时间段
    3: [5, 6], 4: [7, 8], 5: [7, 9], // 下午时间段
    6: [10, 11], 7: [10, 12],        // 晚上时间段
  };
  console.log(rawCourseData)
  console.log("--------------")
  console.log(courseList)
  // 遍历rawCourseData，处理每一门课程的每一个时间点
  courseList.forEach((rawCourse:rawCourse) => {
    [rawCourse.time1, rawCourse.time2, rawCourse.time3].forEach((time) => {
      if (!time) return; // 如果没有time，表示无课程安排，直接跳过

      // 解析时间信息
      //const weekIndex = Math.floor(time / 8) - 1; // 周数从0开始

      const dayAndType = time / 8;//单双周 + 周几
      console.log(dayAndType)
      let dayIndex: number;
      if (dayAndType < 7) {
        dayIndex = Math.floor( dayAndType); // 单周的星期几
      } else if (dayAndType < 14) {
        dayIndex = Math.floor(dayAndType - 7) ; // 双周的星期几
      } else {
        dayIndex = Math.floor(dayAndType - 14) ; // 单双周都有的课
      }
      console.log(dayIndex)
      console.log(time % 8+"time % 8")
      //weekIndex 0-15 dayIndex0-6
      // 根据时间映射关系找到起始和结束时间段
      const [startPeriod, endPeriod] = periodMapping[Math.ceil(time % 8)];
      // 构建Course对象并插入到课程表中
      const courseToAdd: Course = {
        course: rawCourse.courseName,
        teacher: rawCourse.teachers.join(','), // 假设教师以逗号分隔
        location: rawCourse.roomName,
        startPeriod,
        endPeriod,
      };
      console.log(courseToAdd)
      if(dayAndType<7||dayAndType>=14){
        let weekIndex=rawCourse.startWeek-1
        if(rawCourse.startWeek%2-1===1)weekIndex++
        for(;weekIndex<=rawCourse.endWeek-1;weekIndex+=2){
          if (!courseSchedule.data[weekIndex][dayIndex]) courseSchedule.data[weekIndex][dayIndex] = [];
      courseSchedule.data[weekIndex][dayIndex].push(courseToAdd);
        }
      }

      if(dayAndType>=7){
        let weekIndex=rawCourse.startWeek-1
        if(rawCourse.startWeek%2-1===0)weekIndex++
        for(;weekIndex<=rawCourse.endWeek-1;weekIndex+=2){
          if (!courseSchedule.data[weekIndex][dayIndex]) courseSchedule.data[weekIndex][dayIndex] = [];
      courseSchedule.data[weekIndex][dayIndex].push(courseToAdd);
        }
      }
      
      
    });
  });
}
//钩子函数，加载数据
onMounted(async () => {
  try {
    console.log("检测到token不为空")
    // 获取原始课程数据,取消注释，调用上一个函数并传入参数url
    await getRawCourseData();
    //暂时写测试数据
    
    // 处理数据并更新课程表
    processRawData(rawCourseData.value);
    currentWeekIndex.value=2;
    currentWeekIndex.value=0;
  } catch (error) {
    console.error('Error fetching or processing course data:', error);
  }
});
  // 新增方法：根据时间获取节次数
const getPeriod = (time: string): string => {
  const periods = courseSchedule.periods;
  const index = periods.findIndex(period => period === time);
  return (index + 1).toString(); // 返回节次，从1开始计数
};
    // 你的课程数据和接口定义
    interface Course {
      course: string;
      teacher: string;
      location: string;
      startPeriod: number; // 起始时间段索引
      endPeriod: number; // 结束时间段索引
    }
const courseSchedule: {
  weeks: number[];
  days: string[];
  periods: string[];
  data: Course[][][];
} = {
  weeks: [1, 2, 3, 4, 5,6,7,8,9,10,11,12,13,14,15,16], // 列出所有周数
  days: ['星期一', '星期二', '星期三', '星期四', '星期五', '星期六', '星期日'],
  periods: ['08:00-08:45', '08:50-09:35', '09:50-10:35', '10:40-11:25', '11:30-12:15', '14:00-14:45', '14:50-15:35', '15:50-16:35','16:40-17:25','17:30-18:15','18:30-19:15','19:20-20:05','20:10-20:55'],
  data: [
    // 每个内部数组代表一周的课程，索引与weeks中的索引对应
    [
      // 第1周的课程
      // 每天的课程数组,一天几节课不确定
      [//测试，想删删了吧
      ], // 星期一
      [], // 星期二
      
    ],
    // 第2周的课程，以此类推
    [],
    // 直至第16周，这些括号不能改
    [],[],[],[],[],[],[],[],[],[],[],[],[],[],
  ],
};

    interface SpanMethodParams {
  rowIndex: number;
  columnIndex: number;
}
// 假设 currentWeekIndex 是一个有效的 ref 或响应式变量，用于存储当前选中的周索引

const mergeCells = ({ rowIndex, columnIndex }: SpanMethodParams): { rowspan: number, colspan: number } => {
  const periodIndex = rowIndex;
  const dayIndex = columnIndex - 1; // 考虑到列索引从1开始

  // 确保 dayIndex 有效且当前周的课程数据存在
  if (dayIndex >= 0 && dayIndex < courseSchedule.days.length && courseSchedule.data[currentWeekIndex.value]) {
    const weekCourses = courseSchedule.data[currentWeekIndex.value]; // 获取当前周的课程数组
    
    // 确保所查的天对应的课程数组存在
    if (weekCourses[dayIndex]) {
      // 查找对应时间段内开始的课程
      const matchingCourse = weekCourses[dayIndex].find(course => course.startPeriod === periodIndex);

      if (matchingCourse) {
        // 计算跨行数：结束时间段 - 开始时间段 + 1
        const rowspan = matchingCourse.endPeriod - matchingCourse.startPeriod + 1;
        // 跨列数通常为1，除非你有特殊需求
        const colspan = 1;

        // 如果有特殊逻辑处理“结束时间段”的显示，请在这里添加

        return { rowspan, colspan };
      }
    }
  }

  // 如果没有匹配到课程，则默认不跨行也不跨列
  return { rowspan: 1, colspan: 1 };
};


    // 将数据扁平化以适应表格
    let currentWeekIndex = ref(0); // 示例：默认展示第一周
//处理数据，不用管
const flattenedData = computed(() => {
  const selectedWeekCourses = courseSchedule.data[currentWeekIndex.value];
  return courseSchedule.periods.map((period, periodIndex) => {
    const row: Record<number, { course?: Course, shouldRender: boolean }> & { time: string } = { time: period };
    courseSchedule.days.forEach((_, dayIndex) => {
      row[dayIndex] = { shouldRender: false };
    });

    selectedWeekCourses.forEach((dayCourses, dayIndex) => {
      const course = dayCourses.find(course => course.startPeriod === periodIndex);
      if (course) {
        row[dayIndex] = { course, shouldRender: true };
      }
    });

    return row;
  });
});
const randomLightColor = () => {
  const colors = [
    'rgba(255, 127, 127, 0.6)', // Soft Pink
    'rgba(147, 255, 147, 0.6)', // Pale Green
    'rgba(127, 255, 212, 0.6)', // Light Sky Blue
    'rgba(240, 190, 130, 0.2)',   // Pale Orange
    'rgba(191, 255, 191, 0.6)', // Light Lavender
    'rgba(255, 69, 0, 0.6)'    // Light Coral
  ];
  const randomIndex = Math.floor(Math.random() * colors.length);
  return colors[randomIndex];
};
  </script>
  
<style scoped>
    .course-table {
      width: 100%;
    }
    
    .course-table .el-table__row {
      height: 50px;
    }
    
    .course-table .el-table__cell {
      border: 1px solid #000;
      padding: 10px;
      text-align: center;
    }
    .course-cell {
  border-radius: 10px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 5px;
  /* 注意：这里颜色是通过内联样式动态设置的，所以不需要在CSS中定义背景颜色 */
}

/* 如果需要全局设置文字居中，请保留这部分，否则可忽略 */
.el-table td,
.el-table th {
  text-align: center;
}
  </style>
  