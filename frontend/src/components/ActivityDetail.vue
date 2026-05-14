<template>
  <div>
    <h2>活动详情</h2>
    <el-card v-if="activity" style="margin-bottom: 20px;">
      <div slot="header" class="clearfix">
        <span>{{ activity.name }}</span>
        <el-tag :type="activityStatus.type" size="medium" style="float: right;">
          {{ activityStatus.text }}
        </el-tag>
      </div>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="活动ID">{{ activity.id }}</el-descriptions-item>
        <el-descriptions-item label="商品名称">{{ activity.productName }}</el-descriptions-item>
        <el-descriptions-item label="总库存">{{ activity.totalStock }}</el-descriptions-item>
        <el-descriptions-item label="可用库存">{{ activity.availableStock }}</el-descriptions-item>
        <el-descriptions-item label="每人限购">
          <el-tag type="info">{{ activity.limitPerUser }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="您已购买">
          <el-tag :type="userPurchaseCount >= activity.limitPerUser ? 'danger' : 'success'">
            {{ userPurchaseCount }} / {{ activity.limitPerUser }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="预热状态">
          <el-tag :type="activity.isPreheated ? 'success' : 'warning'">
            {{ activity.isPreheated ? '已预热' : '未预热' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="开始时间">{{ activity.startTime }}</el-descriptions-item>
        <el-descriptions-item label="结束时间">{{ activity.endTime }}</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <el-card style="margin-bottom: 20px;">
      <div slot="header" class="clearfix">
        <span>秒杀操作</span>
      </div>
      <el-alert
        v-if="activityStatus.type !== 'success'"
        :title="activityStatus.tip"
        :type="activityStatus.type"
        style="margin-bottom: 20px;"
        show-icon
      ></el-alert>
      <el-form :model="seckillForm" label-width="100px" inline>
        <el-form-item label="用户ID">
          <el-input v-model="seckillForm.userId" placeholder="输入用户ID" :disabled="activityStatus.type !== 'success' || !activity.isPreheated"></el-input>
        </el-form-item>
        <el-form-item label="购买数量">
          <el-input-number v-model="seckillForm.quantity" :min="1" :max="10" :disabled="activityStatus.type !== 'success' || !activity.isPreheated"></el-input-number>
        </el-form-item>
        <el-form-item>
          <el-button 
            type="danger" 
            @click="doSeckill" 
            :disabled="activityStatus.type !== 'success' || !activity.isPreheated"
          >
            {{ activityStatus.buttonText }}
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card>
      <div slot="header" class="clearfix">
        <span>活动统计</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="loadStatistics">刷新统计</el-button>
      </div>
      <el-row :gutter="20" v-if="statistics">
        <el-col :span="6">
          <el-card shadow="hover">
            <div class="text">秒杀成功人数</div>
            <div class="num" style="color: #67C23A; font-size: 30px;">{{ statistics.successCount }}</div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover">
            <div class="text">秒杀失败人数</div>
            <div class="num" style="color: #F56C6C; font-size: 30px;">{{ statistics.failCount }}</div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover">
            <div class="text">剩余库存</div>
            <div class="num" style="color: #409EFF; font-size: 30px;">{{ statistics.availableStock }}</div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover">
            <div class="text">订单支付率</div>
            <div class="num" style="color: #E6A23C; font-size: 30px;">{{ statistics.paymentRate }}</div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'ActivityDetail',
  data() {
    return {
      activity: null,
      statistics: null,
      seckillForm: {
        userId: 'user001',
        quantity: 1
      },
      currentTime: new Date(),
      userPurchaseCount: 0
    }
  },
  computed: {
    activityStatus() {
      if (!this.activity) {
        return { type: 'info', text: '加载中...', tip: '', buttonText: '加载中...' }
      }
      
      const now = this.currentTime
      const startTime = new Date(this.activity.startTime.replace(/-/g, '/'))
      const endTime = new Date(this.activity.endTime.replace(/-/g, '/'))
      
      if (now < startTime) {
        return { 
          type: 'warning', 
          text: '未开始', 
          tip: '活动尚未开始，请耐心等待！',
          buttonText: '活动未开始'
        }
      } else if (now > endTime) {
        return { 
          type: 'info', 
          text: '已结束', 
          tip: '活动已结束，感谢参与！',
          buttonText: '活动已结束'
        }
      } else {
        if (!this.activity.isPreheated) {
          return { 
            type: 'warning', 
            text: '待预热', 
            tip: '活动库存尚未预热，请先预热！',
            buttonText: '未预热'
          }
        }
        if (this.userPurchaseCount >= this.activity.limitPerUser) {
          return { 
            type: 'warning', 
            text: '已达上限', 
            tip: `您已购买${this.userPurchaseCount}件，达到该活动每人限购${this.activity.limitPerUser}件的上限！`,
            buttonText: '已达上限'
          }
        }
        return { 
          type: 'success', 
          text: '进行中', 
          tip: '',
          buttonText: '立即秒杀'
        }
      }
    }
  },
  mounted() {
    this.loadActivity()
    this.loadStatistics()
    this.timer = setInterval(() => {
      this.currentTime = new Date()
    }, 1000)
  },
  beforeDestroy() {
    if (this.timer) {
      clearInterval(this.timer)
    }
  },
  watch: {
    'seckillForm.userId': {
      handler() {
        if (this.activity) {
          this.loadUserPurchaseCount()
        }
      },
      immediate: false
    }
  },
  methods: {
    loadActivity() {
      const activityId = this.$route.params.id
      this.$http.get('/api/activity/' + activityId).then(res => {
        if (res.data.code === 200) {
          this.activity = res.data.data
          this.loadUserPurchaseCount()
        }
      })
    },
    loadUserPurchaseCount() {
      const activityId = this.$route.params.id
      this.$http.get(`/api/activity/user-purchase/${activityId}/${this.seckillForm.userId}`).then(res => {
        if (res.data.code === 200) {
          this.userPurchaseCount = res.data.data
        }
      })
    },
    loadStatistics() {
      const activityId = this.$route.params.id
      this.$http.get('/api/activity/statistics/' + activityId).then(res => {
        if (res.data.code === 200) {
          this.statistics = res.data.data
        }
      })
    },
    doSeckill() {
      if (this.activityStatus.type !== 'success') {
        this.$message.warning(this.activityStatus.tip || '当前无法秒杀')
        return
      }
      const activityId = this.$route.params.id
      const data = {
        activityId: activityId,
        userId: this.seckillForm.userId,
        quantity: this.seckillForm.quantity
      }
      this.$http.post('/api/seckill/do', data).then(res => {
        if (res.data.code === 200) {
          this.$message.success('秒杀成功！订单号：' + res.data.data.id)
          this.loadActivity()
          this.loadStatistics()
          this.loadUserPurchaseCount()
        } else {
          this.$message.error(res.data.message)
          this.loadStatistics()
        }
      }).catch(() => {
        this.$message.error('秒杀失败')
      })
    }
  }
}
</script>

<style scoped>
.text {
  font-size: 14px;
  color: #909399;
  margin-bottom: 10px;
}
.num {
  font-weight: bold;
  text-align: center;
}
</style>
