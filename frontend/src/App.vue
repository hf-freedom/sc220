<template>
  <div id="app">
    <el-container>
      <el-header style="background: #409EFF; color: white; font-size: 24px; text-align: center; line-height: 60px;">
        电商秒杀活动系统
      </el-header>
      <el-container>
        <el-aside width="200px" style="background: #f5f5f5;">
          <el-menu router default-active="1" style="border: none;">
            <el-menu-item index="1" route="/">活动列表</el-menu-item>
            <el-menu-item index="2" route="/create">创建活动</el-menu-item>
            <el-menu-item index="3" route="/orders">订单列表</el-menu-item>
            <el-menu-item index="4" route="/blacklist">黑名单管理</el-menu-item>
          </el-menu>
        </el-aside>
        <el-main style="padding: 20px;">
          <router-view></router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  height: 100vh;
}
</style>
