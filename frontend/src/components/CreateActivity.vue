<template>
  <div>
    <h2>创建秒杀活动</h2>
    <el-form :model="form" label-width="120px" style="max-width: 600px; margin-top: 20px;">
      <el-form-item label="活动名称">
        <el-input v-model="form.name" placeholder="请输入活动名称"></el-input>
      </el-form-item>
      <el-form-item label="商品名称">
        <el-input v-model="form.productName" placeholder="请输入商品名称"></el-input>
      </el-form-item>
      <el-form-item label="总库存">
        <el-input-number v-model="form.totalStock" :min="1" :max="10000"></el-input-number>
      </el-form-item>
      <el-form-item label="每人限购">
        <el-input-number v-model="form.limitPerUser" :min="1" :max="100"></el-input-number>
      </el-form-item>
      <el-form-item label="开始时间">
        <el-date-picker v-model="form.startTime" type="datetime" placeholder="选择日期时间" format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss"></el-date-picker>
      </el-form-item>
      <el-form-item label="结束时间">
        <el-date-picker v-model="form.endTime" type="datetime" placeholder="选择日期时间" format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss"></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="createActivity">创建活动</el-button>
        <el-button @click="resetForm">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  name: 'CreateActivity',
  data() {
    return {
      form: {
        name: '',
        productName: '',
        totalStock: 100,
        limitPerUser: 2,
        startTime: '',
        endTime: ''
      }
    }
  },
  methods: {
    createActivity() {
      if (!this.form.name || !this.form.productName || !this.form.startTime || !this.form.endTime) {
        this.$message.warning('请填写完整信息')
        return
      }
      this.$http.post('/api/activity/create', this.form).then(res => {
        if (res.data.code === 200) {
          this.$message.success('创建成功')
          this.$router.push('/')
        } else {
          this.$message.error(res.data.message)
        }
      }).catch(() => {
        this.$message.error('创建失败')
      })
    },
    resetForm() {
      this.form = {
        name: '',
        productName: '',
        totalStock: 100,
        limitPerUser: 2,
        startTime: '',
        endTime: ''
      }
    }
  }
}
</script>
