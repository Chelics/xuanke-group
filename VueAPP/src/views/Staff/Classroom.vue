<template>
  <div>
    <!-- 筛选部分 -->
    <el-select
      v-model="selectedBuildings"
      multiple
      placeholder="请选择教学楼"
      @change="applyBuildingFilter"
      style="width: 300px;" 
    >
      <el-option
        v-for="filter in buildingFilters"
        :key="filter.value"
        :label="filter.text"
        :value="filter.value"
      ></el-option>
    </el-select>  

    <!-- 表格部分 -->
    <el-table
      :data="filteredClassrooms"
      stripe
      style="width: 100%"
      v-if="filteredClassrooms.length > 0"
    >
      <el-table-column
        prop="building"
        label="教学楼"
      ></el-table-column>
      <el-table-column
        prop="roomName"
        label="教室名"
      ></el-table-column>
      <el-table-column
        prop="roomStorage"
        label="教室容量"
      ></el-table-column>
      <el-table-column label="详细信息">
        <template #default="{ row }">
          <el-button link type="primary" @click="showCourseDetails(row.courseList)">
            详细信息
          </el-button>
        </template>
      </el-table-column>
    </el-table>
      <p v-else>暂无数据</p>
      <!-- 分页组件可以根据实际情况添加，这里省略以简化示例 -->

      <el-dialog
      v-model="dialogVisible"
      title="课程详情"
      width="50%"
    >
      <CourseDetails :courses="selectedCourses" />
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>
    </div>
  </template>
  
  <script setup lang="ts">
  //import { Classroom } from '@/models/Classroom';
  import service from '@/util/request';
import CourseDetails from './CourseDetails.vue'
  import { ref ,computed,onMounted} from 'vue';
   interface Course {
  id: number; // 课程ID
  type: number; // 课程类型
  courseNumber: string; // 课程编号
  roomId: number; // 教室ID
  courseHour: number; // 课时
  courseStorage: number; // 课容量
  courseName: string; // 课程名
  startWeek: number ; // 开始周
  endWeek: number ; // 结束周
  time1: number ; // 周内时间1
  time2?: number ; // 周内时间2
  time3?: number ; // 周内时间3
  faculty: string; // 学院
  credit: number; // 学分
  teachers: string[]; // 授课教师
  classes: string[]; // 授课班级
  roomName: string;
}

 interface Classroom {
  id: number; // 教室ID
  building: string; // 教学楼
  roomName: string; // 教室名
  roomStorage: number; // 教室容量
  courseList?: Course[]; // 课程列表
}
  // 假设的教室数据模型
  const mockClassrooms: Classroom[] = [
  {
    id: 1,
    building: "教1",
    roomName: "1-100",
    roomStorage: 40,
    courseList: [
      {
        id: 1,
        courseName: "线性代数",
        type: 0,
        courseNumber: "SEUCSE01",
        roomId: 1,
        courseHour: 16,
        courseStorage: 40,
        startWeek: 1,
        endWeek: 8,
        time1: 0,
        faculty: "数学学院",
        credit: 2,
        teachers: ["张老三"],
        classes: ["1班"],
        roomName:"教1-1-100"
      }
    ]
  },
  {
    id: 2,
    building: "教2",
    roomName: "2-303",
    roomStorage: 120,
    courseList: [
      {
        id: 2,
        courseName: "工科数学分析",
        type: 0,
        courseNumber: "SEUCSE02",
        roomId: 2,
        courseHour: 96,
        courseStorage: 120,
        startWeek: 1,
        endWeek: 16,
        time1: 33,
        faculty: "数学学院",
        credit: 6,
        teachers: ["王老四"],
        classes: ["2班"],
        roomName:"教2-2-303"
      }
    ]
  }
]

// 用户选择的楼栋筛选值
const selectedBuildings = ref<string[]>([]);

// 固定的筛选选项
const buildingFilters = [
  { text: '教1', value: '教1' },
  { text: '教2', value: '教2' },
  { text: '教3', value: '教3' },
  { text: '教4', value: '教4' },
  { text: '教5', value: '教5' },
  { text: '教6', value: '教6' },
  { text: '教7', value: '教7' },
  { text: '教8', value: '教8' },
];

// 过滤后的教室数据
const filteredClassrooms = computed(() => {
  return classrooms.value.filter(classroom => 
    !selectedBuildings.value.length || selectedBuildings.value.includes(classroom.building)
  );
});

// 应用筛选逻辑
const applyBuildingFilter = (value: string[]) => {
  selectedBuildings.value = value;
};

//详细信息展示
  const dialogVisible = ref(false);
  let selectedCourses = ref<Course[]>([]);
  const showCourseDetails = (courses: Course[]) => {
  selectedCourses.value = courses;
  dialogVisible.value = true;
};


// 假设的教室数据初始化
let classrooms = ref<Classroom[]>([]);

// 模拟获取教室信息的异步函数
async function fetchRooms(): Promise<Classroom[]> {
  // 这里应使用axios等库发出实际请求，例如:
  // const response = await service.get('/staff/rooms');
  // 但为了演示，我们模拟数据返回
  const mockResponse = [
    { id: 1, building: "教1", roomName: "1-100", roomStorage: 40 },
    { id: 2, building: "教2", roomName: "2-303", roomStorage: 120 },
    // 更多教室数据...
  ];
  return mockResponse;
}

// 模拟根据教室ID获取课程列表的异步函数
async function fetchCourseListForRoom(roomId: number): Promise<Course[]> {
  // 同样，这里应替换为真实请求，例如:
   //const response = await service.get(`/staff/rooms/${roomId}`);
  // 模拟数据返回
  const mockCourseList = [
  {
        id: 1,
        courseName: "线性代数",
        type: 0,
        courseNumber: "SEUCSE01",
        roomId: 1,
        courseHour: 16,
        courseStorage: 40,
        startWeek: 1,
        endWeek: 8,
        time1: 0,
        faculty: "数学学院",
        credit: 2,
        teachers: ["张老三"],
        classes: ["1班"],
        roomName:"教1-1-100"
      }
    // 更多课程数据...
  ];
  return mockCourseList;
}

// 在onMounted钩子中执行数据获取逻辑
onMounted(async () => {
  try {
    // 获取所有教室信息
    classrooms.value = await fetchRooms();
    
    // 遍历教室，并为每个教室获取课程列表
    for (const classroom of classrooms.value) {
      const courseList = await fetchCourseListForRoom(classroom.id);
      // 将获取到的课程列表赋值给当前教室的courseList属性
      classroom.courseList = courseList;
    }

    // 将获取到的教室信息赋值给响应式数据classrooms
    //classrooms.value = allRooms;
  } catch (error) {
    console.error('获取教室或课程列表时出错:', error);
  }
});
  </script>