<template>
  <div class="dashboard-page">
    <div class="page-header">
      <div class="container">
        <h1 class="page-title">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#FF6B35" stroke-width="2">
            <path d="M18 20V10"/>
            <path d="M12 20V4"/>
            <path d="M6 20v-6"/>
          </svg>
          数据统计
        </h1>
        <p class="page-subtitle">平台运营数据总览</p>
      </div>
    </div>

    <div class="container">
      <!-- 数据卡片 -->
      <div class="stats-grid">
        <div class="stat-card" v-for="card in cards" :key="card.label">
          <div class="stat-icon" :style="{ background: card.color }">
            <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2">
              <path :d="card.icon"/>
            </svg>
          </div>
          <div class="stat-content">
            <span class="stat-number">{{ card.value }}</span>
            <span class="stat-label">{{ card.label }}</span>
          </div>
          <div class="stat-trend" :class="card.trend">
            <svg v-if="card.trend === 'up'" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <polyline points="23 6 13.5 15.5 8.5 10.5 1 18"/>
              <polyline points="17 6 23 6 23 12"/>
            </svg>
            <svg v-else-if="card.trend === 'down'" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <polyline points="23 18 13.5 8.5 8.5 13.5 1 6"/>
              <polyline points="17 18 23 18 23 12"/>
            </svg>
            <svg v-else width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <line x1="5" y1="12" x2="19" y2="12"/>
            </svg>
            {{ card.trendValue }}
          </div>
        </div>
      </div>

      <!-- 图表区域 -->
      <div class="charts-grid">
        <div class="chart-card">
          <h3 class="chart-title">近7天新增岗位/投递</h3>
          <div ref="lineChartRef" class="chart-container"></div>
        </div>
        <div class="chart-card">
          <h3 class="chart-title">岗位分类分布</h3>
          <div ref="pieChartRef" class="chart-container"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import { getStats } from '../../api/admin'

const cards = ref([
  {
    label: '用户总数',
    value: 0,
    color: 'linear-gradient(135deg, #FF6B35, #FF8E53)',
    icon: 'M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2;M9 7a4 4 0 1 0 0-8 4 4 0 0 0 0 8z;M23 21v-2a4 4 0 0 0-3-3.87;M16 3.13a4 4 0 0 1 0 7.75',
    trend: 'up',
    trendValue: '+12%'
  },
  {
    label: '岗位总数',
    value: 0,
    color: 'linear-gradient(135deg, #3B82F6, #60A5FA)',
    icon: 'M20 7H4a2 2 0 0 0-2 2v10a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2V9a2 2 0 0 0-2-2z;M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16',
    trend: 'up',
    trendValue: '+8%'
  },
  {
    label: '投递总数',
    value: 0,
    color: 'linear-gradient(135deg, #8B5CF6, #A78BFA)',
    icon: 'M22 2L11 13;M22 2L15 22L11 13L2 9L22 2Z',
    trend: 'up',
    trendValue: '+15%'
  },
  {
    label: '评价总数',
    value: 0,
    color: 'linear-gradient(135deg, #059669, #34D399)',
    icon: 'M12 2L15.09 8.26L22 9.27L17 14.14L18.18 21.02L12 17.77L5.82 21.02L7 14.14L2 9.27L8.91 8.26L12 2Z',
    trend: 'neutral',
    trendValue: '0%'
  }
])

const lineChartRef = ref()
const pieChartRef = ref()

onMounted(async () => {
  try {
    const res = await getStats()
    const data = res.data
    cards.value[0].value = data.userCount
    cards.value[1].value = data.jobCount
    cards.value[2].value = data.applicationCount
    cards.value[3].value = data.reviewCount

    await nextTick()
    renderLineChart(data)
    renderPieChart(data)
  } catch {}
})

function renderLineChart(data) {
  if (!lineChartRef.value) return
  const chart = echarts.init(lineChartRef.value)
  chart.setOption({
    tooltip: { trigger: 'axis' },
    legend: { data: ['新增岗位', '新增投递'] },
    grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
    xAxis: {
      type: 'category',
      data: data.dailyJobs.map(d => d.date),
      axisLine: { lineStyle: { color: '#E2E8F0' } },
      axisLabel: { color: '#64748B' }
    },
    yAxis: {
      type: 'value',
      minInterval: 1,
      axisLine: { show: false },
      axisTick: { show: false },
      axisLabel: { color: '#64748B' },
      splitLine: { lineStyle: { color: '#F1F5F9' } }
    },
    series: [
      {
        name: '新增岗位',
        type: 'line',
        smooth: true,
        data: data.dailyJobs.map(d => d.count),
        itemStyle: { color: '#FF6B35' },
        areaStyle: {
          color: {
            type: 'linear',
            x: 0, y: 0, x2: 0, y2: 1,
            colorStops: [
              { offset: 0, color: 'rgba(255, 107, 53, 0.3)' },
              { offset: 1, color: 'rgba(255, 107, 53, 0)' }
            ]
          }
        }
      },
      {
        name: '新增投递',
        type: 'line',
        smooth: true,
        data: data.dailyApplications.map(d => d.count),
        itemStyle: { color: '#3B82F6' },
        areaStyle: {
          color: {
            type: 'linear',
            x: 0, y: 0, x2: 0, y2: 1,
            colorStops: [
              { offset: 0, color: 'rgba(59, 130, 246, 0.3)' },
              { offset: 1, color: 'rgba(59, 130, 246, 0)' }
            ]
          }
        }
      }
    ]
  })
  window.addEventListener('resize', () => chart.resize())
}

function renderPieChart(data) {
  if (!pieChartRef.value) return
  const chart = echarts.init(pieChartRef.value)
  const colors = ['#FF6B35', '#3B82F6', '#8B5CF6', '#059669', '#D97706', '#EF4444', '#6366F1', '#EC4899']
  chart.setOption({
    tooltip: { trigger: 'item' },
    series: [{
      type: 'pie',
      radius: ['40%', '70%'],
      center: ['50%', '50%'],
      label: {
        formatter: '{b}\n{d}%',
        color: '#64748B'
      },
      itemStyle: {
        borderRadius: 10,
        borderColor: '#fff',
        borderWidth: 2
      },
      data: data.categoryStats.map((d, i) => ({
        name: d.category,
        value: d.count,
        itemStyle: { color: colors[i % colors.length] }
      }))
    }]
  })
  window.addEventListener('resize', () => chart.resize())
}
</script>

<style scoped>
.dashboard-page {
  min-height: 100vh;
  background: #F8FAFC;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.page-header {
  background: white;
  padding: 32px 0;
  border-bottom: 1px solid #E2E8F0;
  margin-bottom: 32px;
}

.page-title {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 24px;
  font-weight: 700;
  color: #1E293B;
  margin-bottom: 8px;
}

.page-subtitle {
  font-size: 15px;
  color: #64748B;
}

/* 数据卡片 */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
  margin-bottom: 32px;
}

.stat-card {
  background: white;
  border-radius: 16px;
  padding: 24px;
  display: flex;
  align-items: center;
  gap: 16px;
  border: 1px solid #E2E8F0;
  transition: all 0.3s;
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.1);
}

.stat-icon {
  width: 56px;
  height: 56px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.stat-content {
  flex: 1;
}

.stat-number {
  display: block;
  font-size: 28px;
  font-weight: 700;
  color: #1E293B;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 14px;
  color: #64748B;
}

.stat-trend {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  font-weight: 600;
  padding: 4px 8px;
  border-radius: 6px;
}

.stat-trend.up {
  color: #059669;
  background: #D1FAE5;
}

.stat-trend.down {
  color: #DC2626;
  background: #FEE2E2;
}

.stat-trend.neutral {
  color: #6B7280;
  background: #F3F4F6;
}

/* 图表区域 */
.charts-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 24px;
  margin-bottom: 40px;
}

.chart-card {
  background: white;
  border-radius: 16px;
  padding: 24px;
  border: 1px solid #E2E8F0;
}

.chart-title {
  font-size: 18px;
  font-weight: 600;
  color: #1E293B;
  margin-bottom: 20px;
}

.chart-container {
  height: 300px;
}

@media (max-width: 1024px) {
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .charts-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }
}
</style>
