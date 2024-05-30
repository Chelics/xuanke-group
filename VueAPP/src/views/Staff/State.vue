<template>
  <div>
    <h1>Editable Table with Manual Save</h1>
    <el-table
      v-if="stages.length"
      :data="stages"
      style="width: 100%"
    >
      <el-table-column prop="stageName" label="Stage Name">
        <template #default="scope">
          <el-input v-model="scope.row.stageName" size="small" @change="markRowAsDirty(scope.row)" />
        </template>
      </el-table-column>
      <el-table-column prop="startTime" label="Start Time">
        <template #default="scope">
          <el-date-picker
            v-model="scope.row.startTime"
            type="datetime"
            size="small"
            @change="markRowAsDirty(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column prop="endTime" label="End Time">
        <template #default="scope">
          <el-date-picker
            v-model="scope.row.endTime"
            type="datetime"
            size="small"
            @change="markRowAsDirty(scope.row)"
          />
        </template>
      </el-table-column>
    </el-table>
    <div v-else>Loading...</div>
    <el-button type="primary" @click="saveAllChanges" style="margin-top: 16px;">Save All Changes</el-button>
  </div>
</template>
<script setup lang="ts">
import { onMounted, ref } from 'vue';
import request from '@/util/request';
import { ElMessage } from 'element-plus';
import service from '@/util/request';
import { updateState } from '@/api/Stuff';
interface ResponseData {
  code: number;
  msg: string;
  data: any;
}
interface Stage {
  id: number;
  stageName: string;
  startTime: Date; // Consider using Date type in real implementation
  endTime: Date; // Consider using Date type in real implementation
  isDirty: boolean; // Track if this row has been edited
}
const stages = ref<any[]>([])
//import { Ref } from 'vue';
async function getState(){
    try {
      const response :ResponseData= await service.get("/staff/stages")
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
  //stages.value = response.map(stage => ({ ...stage, isEditing: false }));
})
function markRowAsDirty(row: Stage) {
  row.isDirty = true;
}

async function saveAllChanges() {
  const dirtyRows = stages.value.filter(row => row.isDirty);
  for (const row of dirtyRows) {
    try {
      await updateState(row);
      ElMessage.success(`Changes for stage ${row.stageName} saved successfully!`);
      row.isDirty = false; // Reset dirty flag after successful save
    } catch (error) {
      ElMessage.error(`Failed to save changes for stage ${row.stageName}.`);
      console.error(`Error saving stage ${row.stageName}:`, error);
    }
  }
}


</script>