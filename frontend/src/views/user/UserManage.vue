<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { listUsers, createUser, updateUserStatus, resetPassword } from '../../api/user'

const users = ref([])
const dialogVisible = ref(false)
const formRef = ref()
const submitting = ref(false)

const form = reactive({
  username: '',
  realName: '',
  email: '',
  role: 'MEMBER'
})

const rules = {
  username: [{ required: true, message: '请输入登录账号', trigger: 'blur' }],
  realName: [{ required: true, message: '请输入姓名', trigger: 'blur' }]
}

const roleLabel = {
  ADMIN: '系统管理员',
  PM: '项目经理',
  MEMBER: '团队成员'
}

onMounted(loadUsers)

async function loadUsers() {
  users.value = await listUsers()
}

function openCreateDialog() {
  form.username = ''
  form.realName = ''
  form.email = ''
  form.role = 'MEMBER'
  dialogVisible.value = true
}

async function handleSubmit() {
  await formRef.value.validate()
  submitting.value = true
  try {
    await createUser(form)
    ElMessage.success('用户创建成功，初始密码为 123456')
    dialogVisible.value = false
    loadUsers()
  } finally {
    submitting.value = false
  }
}

async function toggleStatus(row) {
  const nextStatus = row.status === 1 ? 0 : 1
  await updateUserStatus(row.id, nextStatus)
  ElMessage.success(nextStatus === 1 ? '账号已启用' : '账号已禁用')
  loadUsers()
}

async function handleResetPassword(row) {
  await ElMessageBox.confirm(`确定将 ${row.realName} 的密码重置为 123456 吗？`, '提示', { type: 'warning' })
  await resetPassword(row.id)
  ElMessage.success('密码已重置')
}
</script>

<template>
  <div>
    <div class="toolbar">
      <el-button type="primary" @click="openCreateDialog">+ 新建用户</el-button>
    </div>

    <el-table :data="users" border>
      <el-table-column prop="username" label="账号" width="140" />
      <el-table-column prop="realName" label="姓名" width="140" />
      <el-table-column prop="email" label="邮箱" min-width="180" />
      <el-table-column label="角色" width="120">
        <template #default="{ row }">{{ roleLabel[row.role] }}</template>
      </el-table-column>
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'">{{ row.status === 1 ? '正常' : '禁用' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button link size="small" @click="toggleStatus(row)">
            {{ row.status === 1 ? '禁用' : '启用' }}
          </el-button>
          <el-button link size="small" @click="handleResetPassword(row)">重置密码</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" title="新建用户" width="420px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="账号" prop="username">
          <el-input v-model="form.username" placeholder="登录账号" />
        </el-form-item>
        <el-form-item label="姓名" prop="realName">
          <el-input v-model="form.realName" placeholder="真实姓名" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" placeholder="选填" />
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="form.role" style="width: 100%">
            <el-option label="团队成员" value="MEMBER" />
            <el-option label="项目经理" value="PM" />
            <el-option label="系统管理员" value="ADMIN" />
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
