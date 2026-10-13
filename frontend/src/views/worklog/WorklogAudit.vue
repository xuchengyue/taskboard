<script setup>
import { ref, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { listProjects } from '../../api/project'
import { pendingWorklogs, auditWorklog } from '../../api/worklog'

const projects = ref([])
const currentProjectId = ref(null)
const records = ref([])

onMounted(async () => {
  projects.value = await listProjects()
  if (projects.value.length > 0) {
    currentProjectId.value = projects.value[0].id
  }
})

watch(currentProjectId, (val) => {
  if (val) loadRecords()
})

async function loadRecords() {
  records.value = await pendingWorklogs(currentProjectId.value)
}

async function handleApprove(row) {
  await auditWorklog({ worklogId: row.id, status: 1, remark: '通过' })
  ElMessage.success('已通过')
  loadRecords()
}

async function handleReject(row) {
  const { value } = await ElMessageBox.prompt('请输入驳回原因', '驳回工时', {
    inputPlaceholder: '例如：描述过于简单，请补充细节',
    inputPattern: /.+/,
    inputErrorMessage: '驳回原因不能为空'
  })
  await auditWorklog({ worklogId: row.id, status: 2, remark: value })
  ElMessage.success('已驳回')
  loadRecords()
}
</script>

<template>
  <div>
    <div class="toolbar">
      <el-select v-model="currentProjectId" placeholder="选择项目" style="width: 260px">
        <el-option v-for="p in projects" :key="p.id" :label="p.name" :value="p.id" />
      </el-select>
    </div>

    <el-table :data="records" border>
      <el-table-column prop="workDate" label="日期" width="110" />
      <el-table-column prop="userName" label="填报人" width="110" />
      <el-table-column prop="taskTitle" label="任务" min-width="180" show-overflow-tooltip />
      <el-table-column prop="hours" label="工时(h)" width="90" />
      <el-table-column prop="description" label="工作内容" min-width="220" show-overflow-tooltip />
      <el-table-column label="操作" width="160" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" size="small" @click="handleApprove(row)">通过</el-button>
          <el-button link type="danger" size="small" @click="handleReject(row)">驳回</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-empty v-if="records.length === 0" description="暂无待审核的工时记录" />
  </div>
</template>

<style scoped>
.toolbar {
  margin-bottom: 16px;
}
</style>
