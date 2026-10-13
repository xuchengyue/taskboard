<script setup>
defineProps({
  task: { type: Object, required: true }
})

defineEmits(['edit', 'delete'])

const priorityMap = {
  HIGH: { label: '高', type: 'danger' },
  MEDIUM: { label: '中', type: 'warning' },
  LOW: { label: '低', type: 'info' }
}
</script>

<template>
  <div class="task-card">
    <div class="card-top">
      <span class="title">{{ task.title }}</span>
      <el-tag :type="priorityMap[task.priority]?.type" size="small" effect="plain">
        {{ priorityMap[task.priority]?.label }}
      </el-tag>
    </div>

    <p class="desc" v-if="task.description">{{ task.description }}</p>

    <div class="card-bottom">
      <span class="hours">{{ task.actualHours }} / {{ task.estimatedHours }}h</span>
      <span class="assignee" v-if="task.assigneeName">{{ task.assigneeName }}</span>
    </div>

    <div class="due" v-if="task.dueDate">截止：{{ task.dueDate }}</div>

    <div class="actions">
      <el-button link size="small" @click.stop="$emit('edit')">编辑</el-button>
      <el-button link size="small" type="danger" @click.stop="$emit('delete')">删除</el-button>
    </div>
  </div>
</template>

<style scoped>
.task-card {
  background: #fff;
  border-radius: 4px;
  padding: 10px 12px;
  margin: 8px 0;
  box-shadow: 0 1px 2px rgba(9, 30, 66, 0.25);
  cursor: grab;
}

.card-top {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 6px;
}

.title {
  font-size: 14px;
  color: #172b4d;
  font-weight: 500;
  word-break: break-all;
}

.desc {
  font-size: 12px;
  color: #6b778c;
  margin: 6px 0;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.card-bottom {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #6b778c;
  margin-top: 6px;
}

.due {
  font-size: 12px;
  color: #ff8b00;
  margin-top: 4px;
}

.actions {
  margin-top: 4px;
  text-align: right;
}
</style>
