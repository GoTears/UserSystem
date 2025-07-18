<script setup lang="ts">
import { computed } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '../stores/counter';
import { ArrowDown } from '@element-plus/icons-vue';

const router = useRouter();
const userStore = useUserStore();

const isAdmin = computed(() => {
  return userStore.user?.userRole === 1;
});

const handleCommand = async (command: string) => {
  if (command === 'profile') {
    router.push('/user-center');
  } else if (command === 'logout') {
    try {
      await fetch('/api/user/logout', { method: 'POST' });
      userStore.logout();
      router.push('/login');
    } catch (e) {
      console.error('退出登录失败:', e);
    }
  }
};
</script>

<template>
  <div class="home-container">
    <el-container>
      <el-header>
        <div class="header-content">
          <h1>用户中心系统</h1>
          <div class="user-info" v-if="userStore.isLogin">
            <span>欢迎，{{ userStore.user?.username || userStore.user?.userAccount }}</span>
            <el-dropdown @command="handleCommand">
              <el-button type="text">
                个人中心 <el-icon><ArrowDown /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="profile">个人信息</el-dropdown-item>
                  <el-dropdown-item command="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
          <div v-else>
            <el-button @click="$router.push('/login')">登录</el-button>
            <el-button @click="$router.push('/register')">注册</el-button>
          </div>
        </div>
      </el-header>
      
      <el-main>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-card>
              <template #header>
                <div class="card-header">
                  <span>用户管理</span>
                </div>
              </template>
              <div class="card-content">
                <el-button @click="$router.push('/user-center')" style="width:100%; margin-bottom:10px;">
                  用户中心
                </el-button>
                <el-button @click="$router.push('/user-list')" style="width:100%;" v-if="isAdmin">
                  用户列表
                </el-button>
              </div>
            </el-card>
          </el-col>
          
          <el-col :span="16">
            <el-card>
              <template #header>
                <div class="card-header">
                  <span>系统信息</span>
                </div>
              </template>
              <div class="card-content">
                <p v-if="userStore.isLogin">
                  当前用户：{{ userStore.user?.username || userStore.user?.userAccount }}
                </p>
                <p v-else>请先登录</p>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </el-main>
    </el-container>
  </div>
</template>

<style scoped>
.home-container {
  min-height: 100vh;
}
.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
}
.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.card-content {
  text-align: center;
}
</style>
