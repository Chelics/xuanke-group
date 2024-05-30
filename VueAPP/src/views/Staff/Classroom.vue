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
      <el-table-column prop="id" label="ID" width="50" />
      <el-table-column prop="building" label="教学楼" />
      <el-table-column prop="roomName" label="教室名" />
      <el-table-column prop="roomStorage" label="教室容量" />
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
</template>

<script setup lang="ts">
import { ref, onMounted, watchEffect } from 'vue';
import { ElMessage } from 'element-plus';
//import { ClassroomSearchParams } from '@/types'; // 假定 ClassroomSearchParams 已定义
import service from '@/util/request';
import { ElInput,ElInputNumber } from 'element-plus';
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
  storageEnd: 100,
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
  } catch (error) {
    console.error('获取教室列表失败:', error);
    ElMessage.error('网络错误，请稍后再试。');
  } finally {
    loading.value = false;
  }
}

onMounted(() => {
  fetchData();
});

// 监听searchParams变化，自动触发数据加载
watchEffect(fetchData);
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