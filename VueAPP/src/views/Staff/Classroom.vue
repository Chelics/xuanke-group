<template>
  <div class="classroom-search">
    <div class="search-form">
      <el-input v-model="searchParams.building" placeholder="教学楼" />
      <el-input v-model="searchParams.roomName" placeholder="教室名" />
      <el-input v-model="searchParams.storageBegin" placeholder="容量从" />
      <el-input v-model="searchParams.storageEnd" placeholder="到" />
      <el-button @click="fetchData">搜索</el-button>
    </div>
    
    <el-table
      v-loading="loading"
      :data="classrooms"
      stripe
      style="width: 100%">
      
      <el-table-column prop="building" label="教学楼" />
      <el-table-column prop="roomName" label="教室名" />
      <el-table-column prop="roomStorage" label="教室容量" />
      <el-table-column label="详细信息">
        <template #default="{ row }">
          <el-button link type="primary" @click="showCourseDetails(row.courseList)">
            详细信息
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-model:currentPage="searchParams.page"
      v-model:page-size="searchParams.pageSize"
      :page-sizes="[10, 20, 50, 100]"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
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
</template>

<script setup lang="ts">
import { ref, onMounted, watchEffect,reactive } from 'vue';
import { ElMessage } from 'element-plus';
//import { ClassroomSearchParams } from '@/types'; // 假定 ClassroomSearchParams 已定义
import service from '../../util/request';
import { ElInput } from 'element-plus';
import CourseDetails from './CourseDetails.vue';
interface ResponseData {
  code: number;
  msg: string;
  data: any;
}
interface ClassroomSearchParams {
  building?: string; // 教学楼，可选
  roomName?: string; // 教室名，可选
  storageBegin?: number | null; // 范围匹配的开始人数(教室容量)，可选，支持null以表示不限制
  storageEnd?: number | null; // 范围匹配的结束人数(教室容量)，可选，支持null以表示不限制
  page: number; // 分页查询的页码，默认为1
  pageSize: number; // 分页查询的每页记录数，默认为10
}
const searchParams = ref<ClassroomSearchParams>({
  building: '',
  roomName: '',
  storageBegin: 1,
  storageEnd: 300,
  page: 1,
  pageSize: 10,
});

const classrooms = ref<any[]>([]); // 存储教室列表数据
const total = ref<number>(0); // 存储总记录数
const loading = ref(false); // 加载状态

// 分页处理函数
const handleSizeChange = (size: number) => {
  searchParams.value.pageSize = size;
  fetchData();
};

const handleCurrentChange = (page: number) => {
  searchParams.value.page = page;
  fetchData();
};

// 获取教室列表数据
async function fetchData() {
  loading.value = true;
  try {
    const response :ResponseData = await service.get('/staff/rooms', { params: searchParams.value });

    if (response.code === 1) {
      classrooms.value = response.data.rows;
      total.value = response.data.total;
    } else {
      ElMessage.error(response.msg || '获取教室列表失败');
    }
    for (const classroom of classrooms.value) {
      const courseList = await fetchCourseListForRoom(classroom.id);
      // 将获取到的课程列表赋值给当前教室的courseList属性
      classroom.courseList = courseList;
    }
  } catch (error) {
    console.error('获取教室列表失败:', error);
    ElMessage.error('网络错误，请稍后再试。');
  } finally {
    loading.value = false;
  }
}
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
//详细信息展示
const dialogVisible = ref(false);
  let selectedCourses = ref<Course[]>([]);
  const showCourseDetails = (courses: Course[]) => {
    selectedCourses.value = [];
  selectedCourses.value = reactive(courses);
  console.log(courses)
  dialogVisible.value = true;
};
// 模拟根据教室ID获取课程列表的异步函数
async function fetchCourseListForRoom(roomId: number) {
  // 同样，这里应替换为真实请求，例如:
   const response = await service.get(`/staff/rooms/${roomId}`,{params:{id:roomId}});
  // 模拟数据返回
   return response.data;
}
onMounted(() => {
  fetchData();
  
});

// 监听searchParams变化，自动触发数据加载
//watchEffect(fetchData);
</script>

<style scoped>
.classroom-search {
  /* 样式自定义 */
}
.search-form {
  display: flex;
  align-items: center;
  gap: 10px;
}
</style>