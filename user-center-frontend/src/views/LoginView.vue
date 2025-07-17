<template>
  <div class="login-container">
    <el-card class="login-card">
      <h2 style="text-align:center;">用户登录</h2>
      <el-form :model="form" @submit.prevent="handleLogin" label-width="60px">
        <el-form-item label="账号">
          <el-input v-model="form.userAccount" autocomplete="username" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.userPassword" type="password" autocomplete="current-password" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleLogin" style="width:100%;">登录</el-button>
        </el-form-item>
        <el-alert v-if="errorMsg" :title="errorMsg" type="error" show-icon style="margin-bottom:0;" />
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '../stores/counter';

const form = ref({ userAccount: '', userPassword: '' });
const loading = ref(false);
const errorMsg = ref('');
const router = useRouter();
const userStore = useUserStore();

const handleLogin = async () => {
  errorMsg.value = '';
  loading.value = true;
  try {
    const res = await fetch('/api/user/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ userAccount: form.value.userAccount, userPassword: form.value.userPassword })
    });
    const data = await res.json();
    if (data.code === 0 && data.data) {
      userStore.setUser(data.data);
      router.push('/');
    } else {
      errorMsg.value = data.message || '登录失败';
    }
  } catch (e) {
    errorMsg.value = '网络错误';
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f6fa;
}
.login-card {
  width: 360px;
  padding: 32px 24px 18px 24px;
}
</style> 