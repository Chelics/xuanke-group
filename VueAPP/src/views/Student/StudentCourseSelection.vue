<template>
    <div class="select-course">
      <div class="category-buttons">
        <el-button
          v-for="category in categories"
          :key="category.value"
          :type="selectedCategory === category.value ? 'primary' : 'default'"
          @click="selectCategory(category.value)" :style="{ width: '250px' }"
        >
          {{ category.text }}
        </el-button>
      
      </div>
      <div v-if="selectedCategory === 3" class="search-container">
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
          :class="{ 'is-selected': isSelected(course.courseNumber) }"
          
        >
          <!-- 添加课程编号显示，并在选中时应用蓝色 -->
          <p :style="{ color: isSelected(course.courseNumber) ? '#409eff' : '' }">[{{ course.courseNumber }}] {{ course.courseName }}</p>
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
            <el-button type="danger" size="small" @click="selectCourse(course.id, course.courseNumber)">退课</el-button>
          </template>
          <template v-else>
            <el-button 
      :type="course.studentNum < course.courseStorage ? 'primary' : 'danger'"
      :disabled="course.studentNum >= course.courseStorage"
      size="small" @click="selectCourse(course.id, course.courseNumber)">
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
import  service from '@/util/request';
  
  interface FullCourse {
    id: number;
    courseName: string;
    type: number;
    courseNumber: string;
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

    { value: 0, text: '专业课' },
    { value: 1, text: '体育课' },
    { value: 2, text: '通选课' },
    {value:3,text:'全部课程'},
  ];
let courses=ref<FullCourse[]>([]);
      const selectedCategory = ref(categories[0].value);
      const expandedPanels = ref<string[]>([]);
      const groupedCourses = computed(() => {
        return groupCoursesByPrefix(courses.value);
      });
      // 修改filteredGroups计算属性以支持"全部"分类下的搜索
const filteredGroups = computed(() => {
  if (selectedCategory.value === 3) {
    // 如果是"全部"分类且有搜索结果，使用搜索结果
    if (searchedCourses.value!==undefined) {
      console.log("搜索结果"+searchedCourses.value)
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
      nonEmptyTimes: [course.time1, course.time2, course.time3].filter(time => time !== null) as number[]
    })),
  }));
});


  //分类显示
      function groupCoursesByPrefix(courses: FullCourse[]): { prefix: string; commonInfo: Omit<FullCourse, 'courses'>; courses: FullCourse[] }[] {
  return courses.reduce((groups: any[], course: FullCourse) => {

    const prefix = course.courseNumber.slice(0, 6);
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
  
      const selected = ref<string[]>([]); // 新增：用于存储已选课程的 courseNumber
      // 新增方法：判断课程是否被选中,需要已选课程列表
function isSelected(courseNumber: string): boolean {
  return selected.value.includes(courseNumber);
}

// 定义分组（Group）的结构，包含一组具有相同前缀的课程
interface GroupInfo {
  prefix: string; // 课程编号的前缀部分
  commonInfo: Pick<FullCourse, 'courseName' | 'courseNumber'>; // 该组课程共有的信息，如课程名和课程编号
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
  return selected.value.includes(course.courseNumber) && !selected.value.includes(course.courseNumber + '_' + course.id);
}
// 新增方法：检查课程组内是否有课程被选中，
const groupHasSelectedCourse = (group :GroupInfo) => {
  return group.courses.some(course => isSelected(course.courseNumber));
};

function findIdByCourNumber(courses: FullCourse[], courseNumber: string): number | undefined {
  for (const course of courses) {
    if (course.courseNumber === courseNumber) {
      return course.id;
    }
  }
  // 如果没有找到匹配的courseNumber，可以返回undefined或者处理错误
  return undefined;
}
// 修改selectCourse函数以处理选课与退课逻辑,需要已选列表
function selectCourse(courseId: number, courseNumber: string) {
  if (isSelected(courseNumber)) {
    // 退课逻辑，这里假设已经实现了退课操作，实际中可能需要调用后端接口
    const courseId = findIdByCourNumber(courses.value,courseNumber);
    //测试
    

      //实际
    const dropCourse=async()=>{
      try{
        const response :ResponseData=await service.post(`/student/course/drop`,courseId)
        if(response.code===1&&response.msg==="success"){
          ElMessage.info("退课成功！")
          const index = selected.value.findIndex(cn => cn === courseNumber);
            if (index > -1) {
              selected.value.splice(index, 1);
            }
            selectedCourses.value=await getSelectedCourse();
        }
        else{
          ElMessage.error(response.msg)
        }
      }
      catch(error){
        ElMessage.error("退课错误！")
      }
    }
    dropCourse();
    
    
  } 
  else {
    // 选课逻辑，这里简单添加到已选数组
    //测试
    
    //实际
    const courseId = findIdByCourNumber(courses.value,courseNumber);
    const selectCourse=async(course_id)=>{
      try{
        const response :ResponseData=await service.post(`/student/course/select`,
          
          course_id
          
        );
        if(response.code===1&&response.msg==="success"){
          ElMessage.info("选课成功！")
          selected.value.push(courseNumber);
          selectedCourses.value=await getSelectedCourse();
        }
        else{
          ElMessage.error(response.msg)
        }
      }
      catch(error){
        ElMessage.error("选课错误！")
      }
    }
    selectCourse(courseId);
  }
  console.log(`操作了课程ID: ${courseId}, 状态: ${isSelected(courseNumber) ? '退课' : '选课'} }`);
}


//以下为数据发送，接收相关
interface ResponseData {
  code: number;
  msg: string;
  data: any;
}
onMounted(async() => {
  //实际使用
  courses.value=await getCourse();
  executeSearch();
  //已选
   selectedCourses.value=await getSelectedCourse();
   console.log(selectedCourses.value)
   selectedCourses.value.forEach(course => {
    selected.value.push(course.courseNumber);
});
});
//获取所有已选课程信息
const getSelectedCourse=async()=>{
 try{
  const response :ResponseData=await service.get(`/student/course/selected`);
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
//获取所有课程信息
const getCourse=async()=>{
 try{
  const response :ResponseData=await service.get(`/student/course/get`);
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
  // 这里应该是调用实际的API逻辑
  const response :ResponseData=await service.get(`/student/course/search`,
  {params:{
    keyWord:searchKeyword.value}});
  if(response.code===1&&response.msg==="success")
    return response.data
  else{
    ElMessage.error(response?.msg );
  }
};

// 新增变量
const searchKeyword = ref<string>(''); // 搜索关键词，类型为字符串
const searchedCourses = ref<FullCourse[]>([]); // 搜索结果，类型为FullCourse数组
const selectedCourses=ref<FullCourse[]>([]);//已选结果
// 执行搜索的函数
async function executeSearch() {
  if (searchKeyword.value) {
    const response=await searchCoursesApi();
    searchedCourses.value = response
;
  } else {
    // 如果搜索框为空，显示全部课程
    console.log("显示全部")
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
  color: #409eff;
  /* font-weight: bold; 可以根据需要调整字体样式 */
  font-size: 1.5em;
}


  </style>
<style>
  .el-collapse-item__header {
  font-size: 1.3em; /* 假设默认字体大小为1em，这里放大2号 */
}

</style>
