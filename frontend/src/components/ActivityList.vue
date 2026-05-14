<template>
  <div>
    <h2>活动列表</h2>
    <el-button type="primary" @click="loadActivities" style="margin-bottom: 20px;">刷新</el-button>
    <el-table :data="activities" border style="width: 100%">
      <el-table-column prop="id" label="活动ID" width="200"></el-table-column>
      <el-table-column prop="name" label="活动名称"></el-table-column>
      <el-table-column prop="productName" label="商品名称"></el-table-column>
      <el-table-column prop="totalStock" label="总库存" width="100"></el-table-column>
      <el-table-column label="可用库存" width="100">
        <template slot-scope="scope">{{ scope.row.availableStock }}</template>
      </el-table-column>
      <el-table-column prop="limitPerUser" label="每人限购" width="100"></el-table-column>
      <el-table-column prop="startTime" label="开始时间" width="180"></el-table-column>
      <el-table-column prop="endTime" label="结束时间" width="180"></el-table-column>
      <el-table-column label="活动状态" width="100">
        <template slot-scope="scope">
          <el-tag :type="getActivityStatus(scope.row).type">
            {{ getActivityStatus(scope.row).text }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="isPreheated" label="预热状态" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.isPreheated ? 'success' : 'warning'">
            {{ scope.row.isPreheated ? '已预热' : '未预热' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="250">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="viewDetail(scope.row.id)">详情</el-button>
          <el-button size="mini" type="success" @click="preheat(scope.row.id)" :disabled="scope.row.isPreheated">预热</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  name: 'ActivityList',
  data() {
    return {
      activities: [],
      currentTime: new Date()
    }
  },
  mounted() {
    this.loadActivities()
    this.timer = setInterval(() => {
      this.currentTime = new Date()
    }, 1000)
  },
  beforeDestroy() {
    if (this.timer) {
      clearInterval(this.timer)
    }
  },
  methods: {
    getActivityStatus(activity) {
      if (!activity) return { type: 'info', text: '未知' }
      
      const now = this.currentTime
      const startTime = new Date(activity.startTime.replace(/-/g, '/'))
      const endTime = new Date(activity.endTime.replace(/-/g, '/'))
      
      if (now < startTime) {
        return { type: 'warning', text: '未开始' }
      } else if (now > endTime) {
        return { type: 'info', text: '已结束' }
      } else {
        return { type: 'success', text: '进行中' }
      }
    },
    loadActivities() {
      this.$http.get('/api/activity/list').then(res => {
        if (res.data.code === 200) {
          this.activities = res.data.data
        }
      }).catch(() => {
        this.$message.error('加载活动列表失败')
      })
    },
    viewDetail(id) {
      this.$router.push('/activity/' + id)
    },
    preheat(id) {
      this.$http.post('/api/activity/preheat/' + id).then(res => {
        if (res.data.code === 200) {
          this.$message.success('预热成功')
          this.loadActivities()
        } else {
          this.$message.error(res.data.message)
        }
      }).catch(() => {
        this.$message.error('预热失败')
      })
    }
  }
}
</script>
