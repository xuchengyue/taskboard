<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '../../store/auth'
import { listProjects, createProject } from '../../api/project'
import { listUsers } from '../../api/user'

const auth = useAuthStore()
const canCreate = auth.role === 'PM' || auth.role === 'ADMIN'

const projects = ref([])
const users = ref([])
const dialogVisible = ref(false)
const formRef = ref()
const submitting = ref(false)

const form = reactive({
  name: '',
  description: '',
  startDate: '',
  endDate: '',
  memberIds: []
})

const rules = {
  name: [{ required: true, message: '请输入项目名称', trigger: 'blur' }]
}

onMounted(async () => {
  await loadProjects()
  if (canCreate) {
    users.value = await listUsers()
  }
})

async function loadProjects() {
  projects.value = await listProjects()
}

function openCreateDialog() {
  form.name = ''
  form.description = ''
  form.startDate = ''
  form.endDate = ''
  form.memberIds = []
  dialogVisible.value = true
}

async function handleSubmit() {
  await formRef.value.validate()
  submitting.value = true
  try {
    await createProject(form)
    ElMessage.success('项目创建成功')
    dialogVisible.value = false
    loadProjects()
  } finally {
    submitting.value = false
  }
}
</script>

<template>
  <div>
    <div class="toolbar" v-if="canCreate">
      <el-button type="primary" @click="openCreateDialog">+ 新建项目</el-button>
    </div>

    <el-table :data="projects" border style="width: 100%">
      <el-table-column prop="name" label="项目名称" min-width="180" />
      <el-table-column prop="description" label="描述" min-width="240" show-overflow-tooltip />
      <el-table-column prop="startDate" label="开始日期" width="120" />
      <el-table-column prop="endDate" label="结束日期" width="120" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 'ACTIVE' ? 'success' : 'info'">
            {{ row.status === 'ACTIVE' ? '进行中' : '已归档' }}
          </el-tag>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" title="新建项目" width="480px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入项目名称" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="开始日期">
          <el-date-picker v-model="form.startDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
        <el-form-item label="结束日期">
          <el-date-picker v-model="form.endDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
        <el-form-item label="项目成员">
          <el-select v-model="form.memberIds" multiple placeholder="选择团队成员" style="width: 100%">
            <el-option v-for="u in users" :key="u.id" :label="u.realName" :value="u.id" />
          </el-select>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="handleSubmit">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.toolbar {
  margin-bottom: 16px;
}
</style>
