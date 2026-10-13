<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { VueDraggable } from 'vue-draggable-plus'
import { ElMessage, ElMessageBox } from 'element-plus'
import { listProjects } from '../../api/project'
import { getBoardTasks, updateTaskStatus, deleteTask } from '../../api/task'
import TaskDialog from '../../components/TaskDialog.vue'
import TaskCard from '../../components/TaskCard.vue'

const projects = ref([])
const currentProjectId = ref(null)

const columns = reactive({
  TODO: [],
  DOING: [],
  REVIEW: [],
  DONE: []
})

const columnMeta = [
  { key: 'TODO', label: '待办' },
  { key: 'DOING', label: '进行中' },
  { key: 'REVIEW', label: '待审核' },
  { key: 'DONE', label: '已完成' }
]

const dialogVisible = ref(false)
const editingTask = ref(null)

onMounted(async () => {
  projects.value = await listProjects()
  if (projects.value.length > 0) {
    currentProjectId.value = projects.value[0].id
  }
})

watch(currentProjectId, (val) => {
  if (val) {
    loadTasks()
  }
})

async function loadTasks() {
  const list = await getBoardTasks(currentProjectId.value)
  columnMeta.forEach((c) => {
    columns[c.key] = list.filter((t) => t.status === c.key)
  })
}

async function onColumnAdd(status, evt) {
  // vue-draggable-plus在触发onAdd前已经把新元素插入到v-model数组里了，直接按newIndex取就行
  const task = columns[status][evt.newIndex]
  if (!task) return
  try {
    await updateTaskStatus(task.id, status)
    ElMessage.success('任务状态已更新')
  } catch (e) {
    // 更新失败就重新拉一次数据把卡片位置纠正回来
    loadTasks()
  }
}

function openCreateDialog() {
  editingTask.value = null
  dialogVisible.value = true
}

function openEditDialog(task) {
  editingTask.value = task
  dialogVisible.value = true
}

async function handleDelete(task) {
  await ElMessageBox.confirm(`确定要删除任务《${task.title}》吗？`, '提示', { type: 'warning' })
  await deleteTask(task.id)
  ElMessage.success('删除成功')
  loadTasks()
}

function handleDialogSaved() {
  dialogVisible.value = false
  loadTasks()
}
</script>

<template>
  <div class="board-page">
    <div class="toolbar">
      <el-select v-model="currentProjectId" placeholder="选择项目" style="width: 260px">
        <el-option v-for="p in projects" :key="p.id" :label="p.name" :value="p.id" />
      </el-select>
      <el-button type="primary" :disabled="!currentProjectId" @click="openCreateDialog">+ 新建任务</el-button>
    </div>

    <div class="board" v-if="currentProjectId">
      <div class="column" v-for="col in columnMeta" :key="col.key">
        <div class="column-header">
          {{ col.label }}
          <span class="count">{{ columns[col.key].length }}</span>
        </div>

        <VueDraggable v-model="columns[col.key]" :animation="180" group="board-tasks" class="column-body"
          @add="(evt) => onColumnAdd(col.key, evt)">
          <TaskCard v-for="task in columns[col.key]" :key="task.id" :task="task" @edit="openEditDialog(task)"
            @delete="handleDelete(task)" />
        </VueDraggable>
      </div>
    </div>

    <el-empty v-else description="暂无可用项目，请先在「项目管理」中创建项目" />

    <TaskDialog v-model="dialogVisible" :task="editingTask" :project-id="currentProjectId" @saved="handleDialogSaved" />
  </div>
</template>

<style scoped>
.board-page {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.toolbar {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
}

.board {
  flex: 1;
  display: flex;
  gap: 16px;
  overflow-x: auto;
}

.column {
  width: 280px;
  flex-shrink: 0;
  background: #ebecf0;
  border-radius: 6px;
  display: flex;
  flex-direction: column;
  max-height: 100%;
}

.column-header {
  padding: 12px 14px;
  font-weight: 600;
  color: #42526e;
  display: flex;
  align-items: center;
  gap: 6px;
}

.count {
  background: #dfe1e6;
  border-radius: 10px;
  padding: 0 8px;
  font-size: 12px;
  font-weight: normal;
}

.column-body {
  flex: 1;
  padding: 0 8px 8px;
  overflow-y: auto;
  min-height: 60px;
}
</style>
