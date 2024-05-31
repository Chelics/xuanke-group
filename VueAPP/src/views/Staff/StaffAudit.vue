<template>
  <div>
    <h1>课程审核管理</h1>
    <div>
      <section>
        <h2>待审核课程列表</h2>
        <!-- <el-form @submit.prevent="fetchFilteredCourses">
          搜索输入框等其他表单元素...
          <el-input v-model="courseNameToFetch" placeholder="请输入课程名进行查询（留空以查看全部）" style="width: 500px;"
            @keyup.enter="fetchFilteredCourses" />
          <el-button type="primary" @click="fetchFilteredCourses">{{ getButtonLabel }}</el-button>
        </el-form> -->
        <el-table :data="courses.rows" style="margin-top: 20px;" @selection-change="handleSelectionChange" ref="multipleTable">
          <el-table-column type="selection" align="center" />

          <el-table-column prop="courseName" label="课程名" />
          <el-table-column prop="type" label="类别">
            <template #default="scope">{{ getTypeLabel(scope.row.type) || '' }}</template>
          </el-table-column>
          <el-table-column prop="courseNumber" label="课程编号" />
          <el-table-column prop="courseHour" label="课时">
            <template #default="scope">{{ scope.row.courseHour ? `${scope.row.courseHour}课时` : '' }}</template>
          </el-table-column>
          <el-table-column prop="courseStorage" label="课程容量">
            <template #default="scope">{{ scope.row.courseStorage ? `${scope.row.courseStorage}人` : '' }}</template>
          </el-table-column>
          <el-table-column prop="faculty" label="学院" />
          <el-table-column prop="credit" label="学分">
            <template #default="scope">{{ scope.row.credit }}</template>
          </el-table-column>
          <el-table-column prop="teachers" label="授课教师">
            <template #default="scope">{{ (scope.row.teachers||[]).join(', ') }}</template>
          </el-table-column>
          <el-table-column prop="classes" label="班级">
            <template #default="scope">{{ (scope.row.classes||[]).join(', ') }}</template>
          </el-table-column>
          <el-table-column prop="commitTime" label="提交时间" width="250">
            <template #default="scope">{{ scope.row.commitTime }}</template>
          </el-table-column>
          <el-table-column label="操作">
    <template #default="scope">
      <el-button type="text" size="small" @click="showCourseDetails(scope.row)">详情</el-button>
    </template>
  </el-table-column>

        </el-table>
        <el-pagination background layout="prev, pager, next, jumper" :total="courses.total"
          :current-page="searchParams.page" :page-size="searchParams.pageSize" @current-change="handlePageChange" />
      </section>

      <!-- 批量操作区域 -->
      <section>
        <el-button type="primary" @click="batchApprove" :disabled="selectedCourseIds.length === 0">批量通过</el-button>
        <el-button type="danger" @click="batchReject" :disabled="selectedCourseIds.length === 0">批量驳回</el-button>
      </section>
    </div>
  </div>

  <el-dialog v-model="dialogVisible" title="课程详情" width="600px">
    <el-descriptions :column="2" border>
      <el-descriptions-item label="课程名">{{ currentCourse.courseName }}</el-descriptions-item>
      <el-descriptions-item label="类别">{{ getTypeLabel(currentCourse.type) }}</el-descriptions-item>
      <el-descriptions-item label="课程编号">{{ currentCourse.courseNumber }}</el-descriptions-item>
      <el-descriptions-item label="课时">{{ currentCourse.courseHour }}课时</el-descriptions-item>
      <el-descriptions-item label="课程容量">{{ currentCourse.courseStorage }}人</el-descriptions-item>
      <el-descriptions-item label="学院">{{ currentCourse.faculty }}</el-descriptions-item>
      <el-descriptions-item label="学分">{{ currentCourse.credit }}</el-descriptions-item>
      <el-descriptions-item label="授课教师">{{ currentCourse.teachers.join(', ') }}</el-descriptions-item>
      <el-descriptions-item label="班级">{{ currentCourse.classes.join(', ') }}</el-descriptions-item>
      <el-descriptions-item label="提交时间">{{ currentCourse.commitTime }}</el-descriptions-item>
    </el-descriptions>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">关闭</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { defineComponent, onMounted, ref, computed } from 'vue';
import axios from 'axios';
import { ElMessage } from 'element-plus';
import service from '../../util/request';
interface ResponseData {
  code: number;
  msg: string;
  data: any;
}
interface Course {
  // 定义Course接口以匹配实际数据结构
  id: number,
      courseName: string,
      type: number, // 类别：理论课
      courseNumber: string,
      courseHour: number, // 课时
      courseStorage: number, // 课程容量
      faculty: string,
      credit: number, // 学分
      teacherIds: string,
      classIds: string,
      commitTime: string, // 提交时间
      teachers: string[], // 授课教师
      classes: string[] // 班级
}

const selectedCourseIds = ref<number[]>([]);
const courseNameToFetch = ref('');
const courses = ref<{ total: number; rows: Course[] }>({ total: 0, rows: [] });
const searchParams = ref({ courseName: '', page: 1, pageSize: 10 });

//按钮值
const getButtonLabel = computed(() => {
  return courseNameToFetch.value ? '查询' : '刷新';
});
//刷新（查询）
const  fetchFilteredCourses = async () => {
  try {
    if (courseNameToFetch.value) {
      const response :ResponseData= await service.get(`/staff/checking`,{params:courseNameToFetch.value});
      if (response?.code !== 1) {
        ElMessage.error('查询课程失败，请检查ID是否正确。');
      }
      courses.value = { total: 1, rows: [response.data] };
    } else {
      // 当查询框为空时，使用searchParams进行查询
      // 注意：这里直接使用testData作为示例数据返回，实际应用中应替换为真实API调用

      const response = await service.get('/staff/checking', {
            params: searchParams.value,
          }); 
          
         

      courses.value = response.data;
    }
  } catch (error) {
    console.error('查询失败:', error);
    ElMessage.error('查询时发生错误，请重试。');
  }
};

const handlePageChange = (currentPage: number) => {
  searchParams.value.page = currentPage;
  fetchFilteredCourses();
};

const handleSelectionChange = (selection: any[]) => {
  selectedCourseIds.value = selection.map((item: any) => item.id);
};

const batchApprove = async () => {
  try {
    await service.put('/staff/checking', {
      status: 2,
      ids: selectedCourseIds.value,
    });
    ElMessage.success('所选课程已成功通过审核！');
    selectedCourseIds.value = []; // 清空已选择的课程ID
    fetchFilteredCourses(); // 刷新列表
  } catch (error) {
    ElMessage.error('批量通过操作失败，请稍后再试。');
  }
};

const batchReject = async () => {
  try {
    await service.put('/staff/checking', {
      status: 3,
      ids: selectedCourseIds.value,
    });
    ElMessage.success('所选课程已成功驳回！');
    selectedCourseIds.value = []; // 清空已选择的课程ID
    fetchFilteredCourses(); // 刷新列表
  } catch (error) {
    ElMessage.error('批量驳回操作失败，请稍后再试。');
  }
};
const getTypeLabel = (type: number): string => {
  switch (type) {
    case 0:
      return "专业课";
    case 1:
      return "体育课";
    case 2:
      return "通选课";
    default: return"";
  }
};


onMounted(() => {
  fetchFilteredCourses();
});

// 添加用于控制对话框显示的变量和当前选中的课程信息
const dialogVisible = ref(false);
const currentCourse = ref<Course>({
    
  // 定义Course接口以匹配实际数据结构
  id: -1,
      courseName: '这是一个防止类型推断的占位对象',
      type: 0, // 类别：理论课
      courseNumber: 'string',
      courseHour: 0, // 课时
      courseStorage: 0, // 课程容量
      faculty: 'string',
      credit: 0, // 学分
      teacherIds: 'string',
      classIds: 'string',
      commitTime: 'string', // 提交时间
      teachers: [], // 授课教师
      classes: [] // 班级

});

// 显示课程详情的函数
const showCourseDetails = (course: Course) => {
  currentCourse.value = course as Course;
  dialogVisible.value = true;
};
</script>