<template>
  <div>
    <h2>订单列表</h2>
    <el-alert
      title="系统每10秒自动检测超时订单，超时30秒未支付将自动取消并释放库存"
      type="info"
      :closable="false"
      style="margin-bottom: 20px;"
      show-icon
    ></el-alert>
    <el-row style="margin-bottom: 20px;" :gutter="20">
      <el-col :span="12">
        <el-button type="primary" @click="loadOrders">刷新列表</el-button>
        <el-tag type="success" style="margin-left: 10px;">自动刷新中 (5秒)</el-tag>
      </el-col>
      <el-col :span="12" style="text-align: right;">
        <span style="color: #909399;">当前时间: {{ currentTime }}</span>
      </el-col>
    </el-row>
    <el-table :data="orders" border style="width: 100%">
      <el-table-column prop="id" label="订单ID" width="200"></el-table-column>
      <el-table-column prop="activityId" label="活动ID" width="200"></el-table-column>
      <el-table-column prop="userId" label="用户ID" width="120"></el-table-column>
      <el-table-column prop="quantity" label="数量" width="80"></el-table-column>
      <el-table-column label="状态" width="150">
        <template slot-scope="scope">
          <el-tag :type="getStatusType(scope.row.status)">
            {{ getStatusText(scope.row.status) }}
          </el-tag>
          <span v-if="scope.row.status === 'PENDING_PAYMENT'" style="margin-left: 5px; color: #E6A23C; font-size: 12px;">
            (剩余: {{ getRemainingTime(scope.row.createTime) }}秒)
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
      <el-table-column prop="payTime" label="支付时间" width="180">
        <template slot-scope="scope">
          {{ scope.row.payTime || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template slot-scope="scope">
          <el-button size="mini" type="success" @click="payOrder(scope.row.id)" :disabled="scope.row.status !== 'PENDING_PAYMENT'">支付</el-button>
          <el-button size="mini" type="warning" @click="releaseOrder(scope.row.id)" :disabled="scope.row.status !== 'PENDING_PAYMENT'">取消</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  name: 'OrderList',
  data() {
    return {
      orders: [],
      currentTime: new Date().toLocaleString(),
      timer: null,
      orderTimeout: 30
    }
  },
  mounted() {
    this.loadOrders()
    this.timer = setInterval(() => {
      this.currentTime = new Date().toLocaleString()
      this.loadOrders()
    }, 5000)
  },
  beforeDestroy() {
    if (this.timer) {
      clearInterval(this.timer)
    }
  },
  methods: {
    getRemainingTime(createTime) {
      if (!createTime) return 0
      const created = new Date(createTime.replace(/-/g, '/'))
      const now = new Date()
      const elapsed = Math.floor((now - created) / 1000)
      return Math.max(0, this.orderTimeout - elapsed)
    },
    loadOrders() {
      this.$http.get('/api/order/list').then(res => {
        if (res.data.code === 200) {
          this.orders = res.data.data.sort((a, b) => new Date(b.createTime.replace(/-/g, '/')) - new Date(a.createTime.replace(/-/g, '/')))
        }
      }).catch(() => {
        this.$message.error('加载订单列表失败')
      })
    },
    getStatusType(status) {
      if (status === 'PAID') return 'success'
      if (status === 'PENDING_PAYMENT') return 'warning'
      if (status === 'CANCELLED') return 'info'
      return ''
    },
    getStatusText(status) {
      if (status === 'PAID') return '已支付'
      if (status === 'PENDING_PAYMENT') return '待支付'
      if (status === 'CANCELLED') return '已取消'
      return status
    },
    payOrder(orderId) {
      this.$http.post('/api/order/pay/' + orderId).then(res => {
        if (res.data.code === 200) {
          this.$message.success('支付成功，库存已锁定！')
          this.loadOrders()
        } else {
          this.$message.error(res.data.message)
        }
      }).catch(() => {
        this.$message.error('支付失败')
      })
    },
    releaseOrder(orderId) {
      this.$http.post('/api/order/release/' + orderId).then(res => {
        if (res.data.code === 200) {
          this.$message.success('订单已取消，库存已释放！')
          this.loadOrders()
        } else {
          this.$message.error(res.data.message)
        }
      }).catch(() => {
        this.$message.error('取消失败')
      })
    }
  }
}
</script>
