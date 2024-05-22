<script lang="ts" setup>

import { ElButton, ElMessage } from 'element-plus';
import { reactive } from 'vue'

const formInline = reactive({
  num: '',
  name: '',
  state: '',
})

const onSubmit = () => {
  console.log('submit!')
}

const tableData = ref([
  {
    courseNum: 'EDS45444',
    courseName: '操作系统',
    term: '2022-2023 秋季学期',
    capacity: '189',
    roomId: 'J1-102',
    state: '待审核',
  },{
    courseNum: 'SEU99017',
    courseName: '数据结构',
    term: '2022-2023 秋季学期',
    capacity: '19',
    roomId: 'J6-102',
    state: '已通过',
  }
])


import { ref } from 'vue'


const pageNum = ref(4)//当前有效页
const pageSize = ref(10)//每页的数据条数
const totalLine = ref(50)//总数据条数
const small = ref(false)
const background = ref(false)
const disabled = ref(false)

const handleSizeChange = (val: number) => {
  console.log(`${val} items per page`)
  ApplyedCourseList()
}
const handleCurrentChange = (val: number) => {
  console.log(`current page: ${val}`)
  ApplyedCourseList()
}


//课程列表
import { ApplyedCourseListService } from '@/api/Teacher';
const ApplyedCourseList = async () => {
  let result = await ApplyedCourseListService();
  tableData.value = result.data;
}

//控制添加课程弹窗
const dialogVisible = ref(false)
//添加课程数据模型
const courseModel = ref({
  courseNum: '',
  courseName: '',
  term: '',
  capacity: ''
})
//添加表单校验
const rules = {
  courseNum: [
    { required: true, message: '请输入课程编号', trigger: 'blur' },
  ],
  courseName: [
    { required: true, message: '请输入课程名称', trigger: 'blur' },
  ],
  term: [
    { required: true, message: '请输入授课学期', trigger: 'blur' },
  ],
  capacity: [
    { required: true, message: '请输入课容量', trigger: 'blur' },
  ]
}


//列表查询
const searchId = ref('')
const searchName = ref('')
const searchState = ref('')

import { ApplyedCourseSearchService } from '@/api/Teacher';
const courseSearchList = async () => {
  let params = {
    pageNum: pageNum.value,
    pageSize: pageSize.value,
    searchId: searchId.value ? searchId.value : null,
    searchName: searchName.value ? searchName : null,
    searchState: searchState.value ? searchState : null
  }

  let result = await ApplyedCourseSearchService(params);

  totalLine.value = result.data.total;
  tableData.value = result.data.items;

}
//courseSearchList();

//调用接口，新增申请课程
import { ApplyedCourseAddService } from '@/api/Teacher';
const addCourse = async () => {
  //调用接口
  let result = await ApplyedCourseAddService(courseModel.value);
  //ElMessage.success(result.msg?result.msg:'添加成功')

  //刷新列表（重新调用获取课表的函数
  ApplyedCourseList();
  dialogVisible.value = false;

}

</script>






<template>
  <el-card>
    <div class="card-header">
      <span>课程管理</span>
      <el-button type="primary" @click='dialogVisible = true'>申请课程</el-button>
    </div>

    <div>
      <hr>
    </div>


    <el-form :inline="true" :model="formInline" class="demo-form-inline">
      <el-form-item label="课程编号">
        <el-input v-model="formInline.num" placeholder="课程编号" clearable />
      </el-form-item>

      <el-form-item label="课程名称">
        <el-input v-model="formInline.name" placeholder="课程名称" clearable />
      </el-form-item>

      <el-form-item label="审核状态">
        <el-select v-model="formInline.state" placeholder="审核状态" style="width: 120px" clearable>
          <el-option label="待审核" value="0" />
          <el-option label="已通过" value="1" />
          <el-option label="已驳回" value="2" />
        </el-select>
      </el-form-item>


      <el-form-item>
        <el-button type="primary" @click="ApplyedCourseSearchService">搜索</el-button>
      </el-form-item>
    </el-form>


    <el-table :data="tableData" style="width: 100%">
      <el-table-column prop="courseNum" label="课程编号" width="180" />
      <el-table-column prop="courseName" label="课程名称" />
      <el-table-column prop="term" label="授课学期" />
      <el-table-column prop="capacity" label="课容量" />
      <el-table-column prop="roomId" label="上课地点" />
      <el-table-column prop="state" label="课程状态" />
      <el-table-column label="操作" />
    </el-table>


    <el-dialog v-model="dialogVisible" title="课程申请" width="30%">
      <el-form :model="courseModel" :rules="rules" label-width="100px" style="padding-right:30px">
        <el-form-item label="课程编号" prop="courseNum">
          <el-input v-model="courseModel.courseNum" minlength="1" maxlength="10"></el-input>
        </el-form-item>
        <el-form-item label="课程名称" prop="courseName">
          <el-input v-model="courseModel.courseName" minlength="1" maxlength="10"></el-input>
        </el-form-item>
        <el-form-item label="授课学期" prop="term">
          <el-input v-model="courseModel.term" minlength="1" maxlength="10"></el-input>
        </el-form-item>
        <el-form-item label="课容量" prop="capacity">
          <el-input v-model="courseModel.capacity" minlength="1" maxlength="10"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="addCourse">确认</el-button>
        </span>
      </template>
    </el-dialog>


    <div class="el-p">
      <div class="demonstration"></div>
      <el-pagination
      v-model:current-page="pageNum"
      v-model:page-size="pageSize"
      :page-sizes="[1, 10, 15, 20]"
      :small="small"
      :disabled="disabled"
      :background="background"
      layout="total, sizes, prev, pager, next, jumper"
      :total="60"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />

    </div>
  </el-card>
</template>


<style scoped>
.el-p {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.card-header {
  display: flex;
  justify-content: space-between;
}
</style>