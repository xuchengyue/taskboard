<script setup>
import { ref, reactive, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { createTask, updateTask } from '../api/task'
import { listProjectMembers } from '../api/project'

const props = defineProps({
  modelValue: { type: Boolean, default: false },
  task: { type: Object, default: null },
  projectId: { type: [Number, String], default: null }
})

const emit = defineEmits(['update:modelValue', 'saved'])

const formRef = ref()
const members = ref([])
const submitting = ref(false)

const form = reactive({
  id: null,
  title: '',
  description: '',
  priority: 'MEDIUM',
  assigneeId: null,
  estimatedHours: 0,
  dueDate: ''
})

const rules = {
  title: [{ required: true, message: '请输入任务标题', trigger: 'blur' }]
}

watch(
  () => props.modelValue,
  async (visible) => {
    if (!visible) return

    if (props.projectId) {
      members.value = await listProjectMembers(props.projectId)
    }

    if (props.task) {
      form.id = props.task.id
      form.title = props.task.title
      form.description = props.task.description
      form.priority = props.task.priority
      form.assigneeId = props.task.assigneeId
      form.estimatedHours = props.task.estimatedHours
      form.dueDate = props.task.dueDate
    } else {
      form.id = null
      form.title = ''
      form.description = ''
      form.priority = 'MEDIUM'
      form.assigneeId = null
      form.estimatedHours = 0
      form.dueDate = ''
    }
  }
)

function handleClose() {
  emit('update:modelValue', false)
}

async function handleSubmit() {
  await formRef.value.validate()
  submitting.value = true
  try {
    const payload = { ...form, projectId: props.projectId }
    if (form.id) {
      await updateTask(payload)
    } else {
      await createTask(payload)
    }
    ElMessage.success('保存成功')
    emit('saved')
  } finally {
    submitting.value = false
  }
}
</script>

<template>
  <el-dialog :model-value="modelValue" :title="form.id ? '编辑任务' : '新建任务'" width="480px" @close="handleClose"
    @update:model-value="(v) => emit('update:modelValue', v)">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
      <el-form-item label="标题" prop="title">
        <el-input v-model="form.title" placeholder="请输入任务标题" />
      </el-form-item>
      <el-form-item label="描述">
        <el-input v-model="form.description" type="textarea" :rows="3" placeholder="任务详细描述（选填）" />
      </el-form-item>
      <el-form-item label="优先级">
        <el-radio-group v-model="form.priority">
          <el-radio-button value="LOW">低</el-radio-button>
          <el-radio-button value="MEDIUM">中</el-radio-button>
          <el-radio-button value="HIGH">高</el-radio-button>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="负责人">
        <el-select v-model="form.assigneeId" placeholder="选择负责人" clearable style="width: 100%">
          <el-option
            v-for="m in members"
            :key="m.id"
            :label="m.real_name || m.realName || m.username"
            :value="m.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="预估工时">
        <el-input-number v-model="form.estimatedHours" :min="0" :step="0.5" />
      </el-form-item>
      <el-form-item label="截止日期">
        <el-date-picker v-model="form.dueDate" type="date" value-format="YYYY-MM-DD" placeholder="选择日期" style="width: 100%" />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" :loading="submitting" @click="handleSubmit">保存</el-button>
    </template>
  </el-dialog>
</template>
