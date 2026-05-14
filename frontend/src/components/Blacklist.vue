<template>
  <div>
    <h2>黑名单管理</h2>
    <el-form :inline="true" :model="form" class="demo-form-inline" style="margin-bottom: 20px;">
      <el-form-item label="用户ID">
        <el-input v-model="form.userId" placeholder="输入用户ID"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="addToBlacklist">加入黑名单</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="blacklist" border style="width: 100%">
      <el-table-column prop="userId" label="用户ID"></el-table-column>
      <el-table-column label="操作" width="150">
        <template slot-scope="scope">
          <el-button size="mini" type="danger" @click="removeFromBlacklist(scope.row)">移除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  name: 'Blacklist',
  data() {
    return {
      form: {
        userId: ''
      },
      blacklist: []
    }
  },
  mounted() {
    this.loadBlacklist()
  },
  methods: {
    loadBlacklist() {
      this.$http.get('/api/blacklist/list').then(res => {
        if (res.data.code === 200) {
          this.blacklist = res.data.data.map(id => ({ userId: id }))
        }
      }).catch(() => {
        this.$message.error('加载黑名单失败')
      })
    },
    addToBlacklist() {
      if (!this.form.userId) {
        this.$message.warning('请输入用户ID')
        return
      }
      this.$http.post('/api/blacklist/add/' + this.form.userId).then(res => {
        if (res.data.code === 200) {
          this.$message.success('已加入黑名单')
          this.form.userId = ''
          this.loadBlacklist()
        } else {
          this.$message.error(res.data.message)
        }
      }).catch(() => {
        this.$message.error('操作失败')
      })
    },
    removeFromBlacklist(row) {
      this.$http.post('/api/blacklist/remove/' + row.userId).then(res => {
        if (res.data.code === 200) {
          this.$message.success('已从黑名单移除')
          this.loadBlacklist()
        } else {
          this.$message.error(res.data.message)
        }
      }).catch(() => {
        this.$message.error('操作失败')
      })
    }
  }
}
</script>
