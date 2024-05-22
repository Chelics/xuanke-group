<template>
    <h1>state</h1>
    <div>
    <!-- 使用Element Plus的表格展示数据，此处仅为示例 -->
    <el-table v-if="stages.length" :data="stages">
      <el-table-column prop="stageName" label="阶段名称"></el-table-column>
      <el-table-column prop="startTime" label="开始时间"></el-table-column>
      <el-table-column prop="endTime" label="结束时间"></el-table-column>
    </el-table>
    <div v-else>加载中...</div>
    <el-button type="primary" @click="updateState(stages[0]),updateState(stages[1]),updateState(stages[2]),updateName(stages[2].stageName)" v-if="stages.length > 0">
    保存修改
  </el-button>
  </div>
</template>
<script setup lang="ts">
import { onMounted, ref } from 'vue';
import request from '@/util/request';
import { updateName, updateState } from '@/api/Stuff';
interface ResponseData {
  code: number;
  msg: string;
  data: any;
}
const stages = ref<any[]>([])
//import { Ref } from 'vue';
async function getState(){
    try {
      const response :ResponseData= await request.get("/staff/stages")
      if (response.code === 1 && response.msg === 'success'){
        stages.value = response.data
      }
    }
    catch (error) {
        console.error(error);
        //直接处理未知错误 error;
        alert( error)
      }
}
onMounted(() => {
  getState()
})
</script>