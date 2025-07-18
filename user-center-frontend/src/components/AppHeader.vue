<template>
  <el-header class="app-header">
    <div class="header-left" @click="$router.push('/')">
      <img src="/favicon.ico" class="logo" alt="logo" />
      <span class="title">用户中心系统</span>
    </div>
    <div class="header-right">
      <el-menu mode="horizontal" :default-active="$route.path" class="nav-menu" background-color="transparent" text-color="#333" active-text-color="#409EFF">
        <el-menu-item index="/">首页</el-menu-item>
        <el-menu-item index="/about">关于</el-menu-item>
        <el-menu-item v-if="userStore.isLogin" index="/user-center">个人中心</el-menu-item>
        <el-menu-item v-if="userStore.isLogin && userStore.user?.userRole === 1" index="/user-list">用户管理</el-menu-item>
      </el-menu>
      <div v-if="userStore.isLogin" class="user-info">
        <el-dropdown @command="handleCommand">
          <span class="el-dropdown-link">
            <el-avatar :size="32" icon="UserFilled" style="margin-right:8px;" />
            {{ userStore.user?.username || userStore.user?.userAccount }}
            <el-icon><arrow-down /></el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="profile">个人信息</el-dropdown-item>
              <el-dropdown-item command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
      <div v-else>
        <el-button type="primary" @click="$router.push('/login')">登录</el-button>
        <el-button @click="$router.push('/register')">注册</el-button>
      </div>
    </div>
  </el-header>
</template>

<script setup lang="ts">
import { useRouter, useRoute } from 'vue-router';
import { useUserStore } from '../stores/counter';
import { ArrowDown, UserFilled } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';

const router = useRouter();
const userStore = useUserStore();
const $route = useRoute();

const handleCommand = async (command: string) => {
  if (command === 'profile') {
    router.push('/user-center');
  } else if (command === 'logout') {
    try {
      await fetch('/api/user/logout', { method: 'POST' });
      userStore.logout();
      ElMessage.success('已退出登录');
      router.push('/login');
    } catch (e) {
      ElMessage.error('退出登录失败');
    }
  }
};
</script>

<style scoped>
.app-header {
  background: #fff;
  box-shadow: 0 2px 8px #f0f1f2;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 64px;
  padding: 0 40px;
}
.header-left {
  display: flex;
  align-items: center;
  cursor: pointer;
}
.logo {
  width: 36px;
  height: 36px;
  margin-right: 12px;
}
.title {
  font-size: 22px;
  font-weight: bold;
  color: #409EFF;
  letter-spacing: 2px;
}
.header-right {
  display: flex;
  align-items: center;
  gap: 18px;
}
.user-info {
  display: flex;
  align-items: center;
  margin-left: 16px;
}
.nav-menu {
  border-bottom: none;
  background: transparent;
}
</style> 