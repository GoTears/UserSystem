<template>
  <div class="user-edit-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>编辑个人信息</span>
        </div>
      </template>
      
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px" v-loading="loading">
        <el-form-item label="用户ID">
          <el-input v-model="form.id" disabled />
        </el-form-item>
        <el-form-item label="账号">
          <el-input v-model="form.userAccount" disabled />
        </el-form-item>
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSubmit" :loading="submitting">保存</el-button>
          <el-button @click="$router.push('/user-center')">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '../stores/counter';
import type { FormInstance, FormRules } from 'element-plus';
import { ElMessage } from 'element-plus';

const router = useRouter();
const userStore = useUserStore();
const formRef = ref<FormInstance>();
const loading = ref(false);
const submitting = ref(false);

const form = ref({
  id: '',
  userAccount: '',
  username: '',
  email: '',
  phone: ''
});

const rules: FormRules = {
  username: [
    { min: 2, max: 20, message: '用户名长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ]
};

const fetchUserInfo = async () => {
  loading.value = true;
  try {
    const res = await fetch('/api/user/getuser');
    const data = await res.json();
    if (data.code === 0 && data.data) {
      const user = data.data;
      form.value = {
        id: user.id,
        userAccount: user.userAccount,
        username: user.username || '',
        email: user.email || '',
        phone: user.phone || ''
      };
    } else {
      ElMessage.error(data.message || '获取用户信息失败');
      router.push('/login');
    }
  } catch (e) {
    ElMessage.error('网络错误');
    router.push('/login');
  } finally {
    loading.value = false;
  }
};

const handleSubmit = async () => {
  if (!formRef.value) return;
  
  const valid = await formRef.value.validate();
  if (!valid) return;

  submitting.value = true;
  try {
    const params = new URLSearchParams();
    params.append('id', form.value.id);
    if (form.value.username) params.append('username', form.value.username);
    if (form.value.email) params.append('newEmail', form.value.email);
    if (form.value.phone) params.append('newPhone', form.value.phone);

    const res = await fetch('/api/user/update', {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body: params
    });
    const data = await res.json();
    if (data.code === 0 && data.data) {
      ElMessage.success('更新成功');
      userStore.setUser(data.data);
      router.push('/user-center');
    } else {
      ElMessage.error(data.message || '更新失败');
    }
  } catch (e) {
    ElMessage.error('网络错误');
  } finally {
    submitting.value = false;
  }
};

onMounted(() => {
  if (!userStore.isLogin) {
    router.push('/login');
    return;
  }
  fetchUserInfo();
});
</script>

<style scoped>
.user-edit-container {
  padding: 20px;
  max-width: 600px;
  margin: 0 auto;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style> 