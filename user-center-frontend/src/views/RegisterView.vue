<template>
  <div class="register-container">
    <el-card class="register-card">
      <h2 style="text-align:center;">用户注册</h2>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="账号" prop="userAccount">
          <el-input v-model="form.userAccount" autocomplete="username" />
        </el-form-item>
        <el-form-item label="密码" prop="userPassword">
          <el-input v-model="form.userPassword" type="password" autocomplete="new-password" />
        </el-form-item>
        <el-form-item label="确认密码" prop="checkPassword">
          <el-input v-model="form.checkPassword" type="password" autocomplete="new-password" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleRegister" style="width:100%;">注册</el-button>
        </el-form-item>
        <el-form-item>
          <el-button @click="$router.push('/login')" style="width:100%;">已有账号？去登录</el-button>
        </el-form-item>
        <el-alert v-if="errorMsg" :title="errorMsg" type="error" show-icon style="margin-bottom:0;" />
        <el-alert v-if="successMsg" :title="successMsg" type="success" show-icon style="margin-bottom:0;" />
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import type { FormInstance, FormRules } from 'element-plus';

const form = ref({ userAccount: '', userPassword: '', checkPassword: '' });
const loading = ref(false);
const errorMsg = ref('');
const successMsg = ref('');
const router = useRouter();
const formRef = ref<FormInstance>();

const validatePass = (rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('请输入密码'));
  } else {
    if (form.value.checkPassword !== '') {
      formRef.value?.validateField('checkPassword');
    }
    callback();
  }
};

const validatePass2 = (rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('请再次输入密码'));
  } else if (value !== form.value.userPassword) {
    callback(new Error('两次输入密码不一致!'));
  } else {
    callback();
  }
};

const rules: FormRules = {
  userAccount: [
    { required: true, message: '请输入账号', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  userPassword: [
    { required: true, validator: validatePass, trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  checkPassword: [
    { required: true, validator: validatePass2, trigger: 'blur' }
  ]
};

const handleRegister = async () => {
  if (!formRef.value) return;
  
  const valid = await formRef.value.validate();
  if (!valid) return;

  errorMsg.value = '';
  successMsg.value = '';
  loading.value = true;
  
  try {
    const res = await fetch('/api/user/register', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ 
        userAccount: form.value.userAccount, 
        userPassword: form.value.userPassword, 
        checkPassword: form.value.checkPassword 
      })
    });
    const data = await res.json();
    if (data.code === 0 && data.data) {
      successMsg.value = '注册成功！正在跳转到登录页...';
      setTimeout(() => {
        router.push('/login');
      }, 1500);
    } else {
      errorMsg.value = data.message || '注册失败';
    }
  } catch (e) {
    errorMsg.value = '网络错误';
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
.register-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f6fa;
}
.register-card {
  width: 400px;
  padding: 32px 24px 18px 24px;
}
</style> 