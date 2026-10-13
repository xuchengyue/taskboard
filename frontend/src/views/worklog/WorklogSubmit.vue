<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { listProjects } from '../../api/project'
import { getBoardTasks } from '../../api/task'
import { submitWorklog, myWorklogs } from '../../api/worklog'

const projects = ref([])
const tasks = ref([])
const records = ref([])

const formRef = ref()
const submitting = ref(false)

const form = reactive({
  projectId: null,
  taskId: null,
  workDate: '',
  hours: 1,
  description: ''
})

const rules = {
  projectId: [{ required: true, message: '请选择项目', trigger: 'change' }],
  taskId: [{ required: true, message: '请选择任务', trigger: 'change' }],
  workDate: [{ required: true, message: '请选择日期', trigger: 'change' }],
  hours: [{ required: true, message: '请输入工时', trigger: 'blur' }]
}

const statusMap = {
  0: { label: '待审核', type: 'warning' },
  1: { label: '已通过', type: 'success' },
  2: { label: '已驳回', type: 'danger' }
}

onMounted(async () => {
  projects.value = await listProjects()
  await loadMyRecords()
})

watch(
  () => form.projectId,
  async (projectId) => {
    form.taskId = null
    tasks.value = projectId ? await getBoardTasks(projectId) : []
  }
)

async function loadMyRecords() {
  records.value = await myWorklogs()
}

async function handleSubmit() {
  await formRef.value.validate()
  submitting.value = true
  try {
    await submitWorklog({
      taskId: form.taskId,
      workDate: form.workDate,
      hours: form.hours,
      description: form.description
    })
    ElMessage.success('工时提交成功，等待审核')
    form.taskId = null
    form.hours = 1
    form.description = ''
    loadMyRecords()
  } finally {
    submitting.value = false
  }
}
</script>

<template>
  <div class="worklog-page">
    <el-card class="form-card" header="填报工时">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px" inline-message>
        <el-form-item label="项目" prop="projectId">
          <el-select v-model="form.projectId" placeholder="选择项目" style="width: 220px">
            <el-option v-for="p in projects" :key="p.id" :label="p.name" :value="p.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="任务" prop="taskId">
          <el-select v-model="form.taskId" placeholder="选择任务" style="width: 260px">
            <el-option v-for="t in tasks" :key="t.id" :label="t.title" :value="t.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="日期" prop="workDate">
          <el-date-picker v-model="form.workDate" type="date" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item label="工时(h)" prop="hours">
          <el-input-number v-model="form.hours" :min="0.5" :max="24" :step="0.5" />
        </el-form-item>
        <el-form-item label="工作内容">
          <el-input v-model="form.description" placeholder="简单描述今天做了什么" style="width: 320px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="submitting" @click="handleSubmit">提交</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card header="我的工时记录" style="margin-top: 16px">
      <el-table :data="records" border>
        <el-table-column prop="workDate" label="日期" width="110" />
        <el-table-column prop="projectName" label="项目" width="160" />
        <el-table-column prop="taskTitle" label="任务" min-width="180" show-overflow-tooltip />
        <el-table-column prop="hours" label="工时(h)" width="90" />
        <el-table-column prop="description" label="工作内容" min-width="200" show-overflow-tooltip />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="statusMap[row.status]?.type">{{ statusMap[row.status]?.label }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="auditRemark" label="审核意见" min-width="160" show-overflow-tooltip />
      </el-table>
    </el-card>
  </div>
</template>

<style scoped>
.worklog-page {
  display: flex;
  flex-direction: column;
}
</style>
