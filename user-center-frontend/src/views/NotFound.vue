<template>
  <div class="page-full-center">
    <el-result icon="warning" title="404 页面不存在" sub-title="您访问的页面不存在或已被删除">
      <template #extra>
        <el-button type="primary" @click="$router.push('/')">返回首页</el-button>
      </template>
    </el-result>
  </div>
</template>

<script setup lang="ts">
// 无需额外逻辑
</script>

<style scoped>
.page-full-center {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style> 