<template>
    <div class="select-course">
      <div class="category-buttons">
        <el-button
          v-for="category in categories"
          :key="category.value"
          :type="selectedCategory === category.value ? 'primary' : 'default'"
          @click="selectCategory(category.value)"
        >
          {{ category.text }}
        </el-button>
      
      </div>
      <div v-if="selectedCategory === 4" class="search-container">
      <el-input
        placeholder="搜索课程"
        v-model="searchKeyword"
        style="width: 600px;"
      />
      <el-button @click="executeSearch">搜索</el-button>
    </div>
      <el-collapse v-model="expandedPanels" accordion >
    <el-collapse-item
      v-for="(group) in groupWithNonEmptyTimes"
      :key="group.prefix"
      
      :name="`${group.prefix}_panel`"
      :class="{ 'is-selected-title': groupHasSelectedCourse(group) }"

    >
    <template #title>
      <span :class="{ 'selected-tag': groupHasSelectedCourse(group) }">
        {{ getFormattedTitle(group) }}
        <span v-if="groupHasSelectedCourse(group)">已选</span>
      </span>
    </template>
      <div class="course-details">
        <div
          v-for="course in group.courses"
          :key="course.id"
          class="course-box"
          :class="{ 'is-selected': isSelected(course.courNumber) }"
          
        >
          <!-- 添加课程编号显示，并在选中时应用红色 -->
          <p :style="{ color: isSelected(course.courNumber) ? 'red' : '' }">[{{ course.courNumber }}] {{ course.courseName }}</p>
          <p>教师: {{ course.teachers?.join(', ') }}</p>
          <p>教室: {{ course.roomName }}</p>
          <p>已选人数：{{ course.studentNum }}</p>
          <p>课程容量: {{ course.courseStorage }}</p>
          <p>时间:</p>
          <template v-if="course.nonEmptyTimes.length">
            <p v-for="time in course.nonEmptyTimes" :key="time">
              {{ formatTime(time, course.startWeek, course.endWeek) }}
            </p>
          </template>
          <!-- 其他你想展示的详细信息 -->

          <slot name="courseExtra" :course="course"></slot>

          <!-- 修改按钮显示逻辑 -->
          <template v-if="isSelectedForUnselection(course)">
            <el-button type="danger" size="small" @click="selectCourse(course.id, course.courNumber)">退课</el-button>
          </template>
          <template v-else>
            <el-button 
      :type="course.studentNum < course.courseStorage ? 'primary' : 'danger'"
      :disabled="course.studentNum >= course.courseStorage"
      size="small" @click="selectCourse(course.id, course.courNumber)">
      {{ course.studentNum < course.courseStorage ? '选课' : '已满' }}
    </el-button>
          </template>
        </div>
      </div>
    </el-collapse-item>
  </el-collapse>
    </div>
  </template>
  
  <script setup lang="ts">
  import { defineComponent, ref, computed } from 'vue';
  import { ElCollapse, ElCollapseItem, ElButton } from 'element-plus';
  import { watch, onMounted } from 'vue';
import axios from 'axios';
import { ElMessage } from 'element-plus';
import service from '@/util/request';
//import { request } from 'node_modules/axios/index.cjs';
  
  interface FullCourse {
    id: number;
    courseName: string;
    type: number;
    courNumber: string;
    roomId?: string;
    courseHour: number;
    courseStorage: number;
    startWeek: number;
    endWeek: number;
    time1: number;
    time2?: number;
    time3?: number;
    faculty: string;
    credit: number;
    teachers?: string[];
    classes?: string[];
    roomName?: string;
    studentNum: number;
  }
  
  const categories = [

    { value: 1, text: '专业课' },
    { value: 2, text: '体育课' },
    { value: 3, text: '通选课' },
    {value:4,text:'全部课程'},
  ];
let courses=ref<FullCourse[]>([]);
      const selectedCategory = ref(categories[0].value);
      const expandedPanels = ref<string[]>([]);
      const groupedCourses = computed(() => {
        return groupCoursesByPrefix(courses.value);
      });
      // 修改filteredGroups计算属性以支持"全部"分类下的搜索
const filteredGroups = computed(() => {
  if (selectedCategory.value === 4) {
    // 如果是"全部"分类且有搜索结果，使用搜索结果
    if (searchedCourses.value.length) {
      return groupCoursesByPrefix(searchedCourses.value);
    } else {
      // 没有搜索结果时显示全部课程
      return groupCoursesByPrefix(courses.value);
    }
  } else {
    // 其他分类逻辑保持不变
    // ...
    return groupedCourses.value.filter(group => group.commonInfo.type === Number(selectedCategory.value));
  }
});
      /* const filteredGroups = computed(() => {
        return groupedCourses.value.filter(group => group.commonInfo.type === Number(selectedCategory.value));
      }); */

      //格式化时间
      function formatTime(time: number, startWeek: number, endWeek: number): string {
  const dayIndex = calculateDay(time); // 使用已提供的逻辑计算星期几
  const [startPeriod, endPeriod] = calculatePeriods(time); // 计算上课节次
  const weekRange = `${startWeek}-${endWeek}周`;
  const weekType = getWeekType(time); // 新增：获取周类型（单周、双周、空）
  const daysInChinese = ['一', '二', '三', '四', '五', '六','日', ];
  const dayInChinese = daysInChinese[dayIndex];
  function calculateDay(time: number): number {
  // 重复已给出的dayAndType计算逻辑
  const dayAndType = time / 8;
  if (dayAndType < 7) return Math.floor(dayAndType);
  if (dayAndType < 14) return Math.floor(dayAndType - 7);
  return Math.floor(dayAndType - 14);
}
function getWeekType(time: number): string {
  const dayAndType = time / 8;
  if (dayAndType < 7) return '单周';
  if (dayAndType < 14) return '双周';
  return ''; // 当time/8 >= 14时，表示没有特定的周类型指示，故留空
}

function calculatePeriods(time: number): [number, number] {
  return periodMapping[Math.ceil(time % 8)];
}

  return `${weekRange} ${weekType} 周${dayInChinese} 第${startPeriod+1}节-第${endPeriod + 1}节`;
}
// 处理时间映射关系
const periodMapping: { [key: number]: [number, number] } = {
    0: [0, 1], 1: [2, 3], 2: [2, 4],  // 上午时间段
    3: [5, 6], 4: [7, 8], 5: [7, 9], // 下午时间段
    6: [10, 11], 7: [10, 12],        // 晚上时间段
  };

// 添加计算属性以过滤出含有非空时间的课程列表
const groupWithNonEmptyTimes = computed(() => {
  return filteredGroups.value.map(group => ({
    ...group,
    courses: group.courses.map(course => ({
      ...course,
      nonEmptyTimes: [course.time1, course.time2, course.time3].filter(time => time !== undefined) as number[]
    })),
  }));
});


  //分类显示
      function groupCoursesByPrefix(courses: FullCourse[]): { prefix: string; commonInfo: Omit<FullCourse, 'courses'>; courses: FullCourse[] }[] {
  return courses.reduce((groups: any[], course: FullCourse) => {
    const prefix = course.courNumber.slice(0, 4);
    const existingGroupIndex = groups.findIndex(g => g.prefix === prefix && g.commonInfo.type === course.type);

    if (existingGroupIndex !== -1) {
      groups[existingGroupIndex].courses.push(course);
    } else {
      const commonInfo = { ...course, courses: [] }; // 注意：这里我们不删除id，而是保留它不在courses数组中显示
      //delete commonInfo.id; 
      groups.push({ prefix, commonInfo, courses: [course] });
    }

    return groups;
  }, []);
}
  
      // 修改selectCategory函数签名，将category参数类型从string改为number
function selectCategory(category: number) {
  selectedCategory.value = category;
  // 如果需要，可以在这里重置expandedPanels，以便每次切换类别时折叠面板都关闭
}
  
      const selected = ref<string[]>([]); // 新增：用于存储已选课程的 courNumber
      // 新增方法：判断课程是否被选中
function isSelected(courNumber: string): boolean {
  return selected.value.includes(courNumber);
}

// 定义分组（Group）的结构，包含一组具有相同前缀的课程
interface GroupInfo {
  prefix: string; // 课程编号的前缀部分
  commonInfo: Pick<FullCourse, 'courseName' | 'courNumber'>; // 该组课程共有的信息，如课程名和课程编号
  courses: FullCourse[]; // 属于该分组的具体课程列表
}
// 新增方法：根据课程号是否在已选列表中调整标题显示
function getFormattedTitle(group :GroupInfo) {
  const credit = group.courses[0]?.credit ?? 0;
  const spaces = '\u00a0'.repeat(30);
  return `[${group.prefix}] ${group.commonInfo.courseName}${spaces}学分：${credit}`;
}

// 新增方法：判断是否应显示退课按钮
function isSelectedForUnselection(course: FullCourse): boolean {
  return selected.value.includes(course.courNumber) && !selected.value.includes(course.courNumber + '_' + course.id);
}
// 新增方法：检查课程组内是否有课程被选中
const groupHasSelectedCourse = (group :GroupInfo) => {
  return group.courses.some(course => isSelected(course.courNumber));
};
// 修改selectCourse函数以处理选课与退课逻辑
function selectCourse(courseId: number, courNumber: string) {
  if (isSelected(courNumber)) {
    // 退课逻辑，这里假设已经实现了退课操作，实际中可能需要调用后端接口
    //测试
    const index = selected.value.findIndex(cn => cn === courNumber);
    if (index > -1) {
      selected.value.splice(index, 1);
    }
    //实际
    /* const dropCourse=async()=>{
      try{
        const response :ResponseData=await axios.put(`/student/course/drop/${courseId}`)
        if(response.code===1&&response.msg==="success"){
          ElMessage.info("退课成功！")
          const index = selected.value.findIndex(cn => cn === courNumber);
            if (index > -1) {
              selected.value.splice(index, 1);
            }
        }
        else{
          ElMessage.error(response.msg)
        }
      }
      catch(error){
        ElMessage.error("退课错误！")
      }
    }
    dropCourse(); */
  } 
  else {
    // 选课逻辑，这里简单添加到已选数组
    //测试
    selected.value.push(courNumber);
    //实际
   /*  const selectCourse=async()=>{
      try{
        const response :ResponseData=await axios.put(`/student/course/select/${courseId}`)
        if(response.code===1&&response.msg==="success"){
          ElMessage.info("选课成功！")
          selected.value.push(courNumber);
        }
        else{
          ElMessage.error(response.msg)
        }
      }
      catch(error){
        ElMessage.error("选课错误！")
      }
    }
    selectCourse(); */
  }
  console.log(`操作了课程ID: ${courseId}, 状态: ${isSelected(courNumber) ? '退课' : '选课'} }`);
}


//以下为数据发送，接收相关
interface ResponseData {
  code: number;
  msg: string;
  data: any;
}
onMounted(async() => {
  //实际使用
  //courses=await getCourse();
  courses.value=[
  {
    id: 1,
    courseName: '高等数学',
    type: 1, // 专业课
    courNumber: 'SX0101',
    roomId: 'R0101',
    courseHour: 48,
    courseStorage: 50,
    startWeek: 1,
    endWeek: 16,
    time1: 0,
    teachers: ['张三', '李四'],
    faculty: '数学系',
    credit: 4,
    roomName: '教学楼A101',
    studentNum: 50,
  },
  {
    id: 2,
    courseName: '篮球基础',
    type: 2, // 体育课
    courNumber: 'TY0101',
    roomId: 'G001',
    courseHour: 32,
    courseStorage: 30,
    startWeek: 3,
    endWeek: 15,
    time1:3,
    teachers: ['王五'],
    faculty: '体育学院',
    credit: 2,
    roomName: '体育馆B场',
    studentNum: 20
  },
  {
    id: 3,
    courseName: '西方哲学史',
    type: 3, // 通选课
    courNumber: 'TX0101',
    roomId: 'L0203',
    courseHour: 32,
    courseStorage: 70,
    startWeek: 2,
    endWeek: 16,
    time1:4,
    teachers: ['赵六'],
    faculty: '文学院',
    credit: 2,
    roomName: '图书馆报告厅',
    studentNum:20
  },
  // 同一课程编号，不同详细信息的示例
  {
    id: 4,
    courseName: '高等数学',
    type: 1,
    courNumber: 'SX0102',
    roomId: 'R0102',
    courseHour: 48,
    courseStorage: 90,
    startWeek: 1,
    endWeek: 16,
    time1:8,
    teachers: ['钱七', '孙八'],
    faculty: '数学系',
    credit: 4,
    roomName: '教学楼A102',
    studentNum :20
  },
  {
    id: 5,
    courseName: '线性代数',
    type: 1,
    courNumber: 'SX0202',
    roomId: 'R0102',
    courseHour: 48,
    courseStorage: 90,
    startWeek: 1,
    endWeek: 16,
    time1:8,
    time2:120,
    teachers: ['钱七', '孙八'],
    faculty: '数学系',
    credit: 4,
    roomName: '教学楼A102',
    studentNum :20
  },
  // 更多课程...
];

});
//获取所有课程信息
const getCourse=async()=>{
 try{
  const response :ResponseData=await service.get(`/student/course/get/`);
  if(response.code===1&&response.msg==="success")
    return response.data
  else{
    ElMessage.error(response.msg)
  }
 }
 catch(error){
    ElMessage.error('获取失败，请稍后再试。');
  }
}
//接收已选课程
  // 已经实现的搜索API函数
const searchCoursesApi = async () => {
  // 这里应该是调用实际的API逻辑，这里仅为示例
  // 假设返回的是过滤后的课程数组
  // return await fetchSearchResults(keyword);
  const response :ResponseData=
  {
    code:1,
    msg:'',
    data:[{
    id: 4,
    courseName: '高等数学',
    type: 1,
    courNumber: 'SX0102',
    roomId: 'R0102',
    courseHour: 48,
    courseStorage: 90,
    startWeek: 1,
    endWeek: 16,
    time1:8,
    teachers: ['钱七', '孙八'],
    faculty: '数学系',
    credit: 4,
    roomName: '教学楼A102',
    studentNum: 50,
  },]
  }
  return response.data
  /* const response :ResponseData=await axios.get(`/student/course/search/${searchKeyword.value}`);
  if(response.code===1&&response.msg==="success")
    return response.data
  else{
    ElMessage.error(response?.msg || response);
  } */
};

// 新增变量
const searchKeyword = ref<string>(''); // 搜索关键词，类型为字符串
const searchedCourses = ref<FullCourse[]>([]); // 搜索结果，类型为FullCourse数组

// 执行搜索的函数
async function executeSearch() {
  if (searchKeyword.value) {
    searchedCourses.value = await searchCoursesApi();
  } else {
    // 如果搜索框为空，显示全部课程
    searchedCourses.value = courses.value;
  }
}

</Script>

  <style scoped>
  .select-course {
    display: flex;
    flex-direction: column;
  }
  .category-buttons {
    display: flex;
    justify-content: space-around;
    margin-bottom: 1rem;
  }
  .course-details {
    display: flex;
    flex-wrap: wrap;
  }
  .course-box {
    border: 1px solid #ccc;
    padding: 1rem;
    margin: 1rem;
    width: calc(33% - 2rem); /* 调整以适应一行显示的方块数量 */
    cursor: pointer;
  }
  .el-collapse-item__header {
  font-size: 1.4em; /* 假设默认字体大小为1em，这里放大2号 */
}
  
.selected-tag {
  color: red;
  /* font-weight: bold; 可以根据需要调整字体样式 */
  font-size: 1.5em;
}


  </style>
<style>
  .el-collapse-item__header {
  font-size: 1.3em; /* 假设默认字体大小为1em，这里放大2号 */
}

</style>
