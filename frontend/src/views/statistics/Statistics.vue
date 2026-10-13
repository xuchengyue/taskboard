<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import { listProjects } from '../../api/project'
import { memberStats, dailyTrend } from '../../api/stat'

const projects = ref([])
const filter = reactive({
  projectId: null,
  range: []
})

const memberChartRef = ref()
const trendChartRef = ref()
let memberChart = null
let trendChart = null

onMounted(async () => {
  projects.value = await listProjects()
  await nextTick()
  memberChart = echarts.init(memberChartRef.value)
  trendChart = echarts.init(trendChartRef.value)
  loadCharts()

  window.addEventListener('resize', () => {
    memberChart.resize()
    trendChart.resize()
  })
})

async function loadCharts() {
  const params = {
    projectId: filter.projectId || undefined,
    startDate: filter.range?.[0] || undefined,
    endDate: filter.range?.[1] || undefined
  }

  const [members, trend] = await Promise.all([memberStats(params), dailyTrend(params)])

  memberChart.setOption({
    title: { text: '成员工时投入统计', left: 'center', textStyle: { fontSize: 14 } },
    tooltip: {},
    xAxis: { type: 'category', data: members.map((m) => m.memberName) },
    yAxis: { type: 'value', name: '小时' },
    series: [
      {
        name: '实际工时',
        type: 'bar',
        data: members.map((m) => m.totalHours),
        itemStyle: { color: '#409eff' },
        barWidth: '40%'
      }
    ]
  })

  trendChart.setOption({
    title: { text: '每日工时趋势', left: 'center', textStyle: { fontSize: 14 } },
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: trend.map((t) => t.workDate) },
    yAxis: { type: 'value', name: '小时' },
    series: [
      {
        name: '工时',
        type: 'line',
        smooth: true,
        data: trend.map((t) => t.totalHours),
        areaStyle: { opacity: 0.15 },
        itemStyle: { color: '#67c23a' }
      }
    ]
  })
}
</script>

<template>
  <div>
    <div class="toolbar">
      <el-select v-model="filter.projectId" placeholder="全部项目" clearable style="width: 200px" @change="loadCharts">
        <el-option v-for="p in projects" :key="p.id" :label="p.name" :value="p.id" />
      </el-select>
      <el-date-picker v-model="filter.range" type="daterange" value-format="YYYY-MM-DD" start-placeholder="开始日期"
        end-placeholder="结束日期" @change="loadCharts" />
    </div>

    <el-row :gutter="16">
      <el-col :span="12">
        <el-card>
          <div ref="memberChartRef" style="height: 360px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div ref="trendChartRef" style="height: 360px"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.toolbar {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
}
</style>
