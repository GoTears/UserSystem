<script setup lang="ts">
import AppHeader from './components/AppHeader.vue'
// 可添加全局逻辑
</script>

<template>
  <div class="app-bg">
    <AppHeader />
    <div class="app-content">
      <router-view />
    </div>
  </div>
</template>

<style>
html, body, #app, .app-bg {
  height: 100%;
  margin: 0;
  padding: 0;
}
.app-bg {
  min-height: 100vh;
  background: linear-gradient(120deg, #f5f6fa 60%, #e3eafc 100%);
  display: flex;
  flex-direction: column;
  align-items: stretch;
  height: 100vh;
}
.app-content {
  flex: 1 1 0;
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: stretch;
  justify-content: flex-start;
  min-height: 0;
}
</style>
