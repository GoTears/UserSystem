<template>
  <div class="page-full-center">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>个人信息</span>
          <el-button type="primary" @click="handleEdit">编辑信息</el-button>
        </div>
      </template>
      
      <el-descriptions :column="2" border v-loading="loading">
        <el-descriptions-item label="用户ID">{{ userInfo?.id }}</el-descriptions-item>
        <el-descriptions-item label="账号">{{ userInfo?.userAccount }}</el-descriptions-item>
        <el-descriptions-item label="用户名">{{ userInfo?.username || '未设置' }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ userInfo?.email || '未设置' }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ userInfo?.phone || '未设置' }}</el-descriptions-item>
        <el-descriptions-item label="用户角色">{{ userInfo?.userRole === 1 ? '管理员' : '普通用户' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ formatDate(userInfo?.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ formatDate(userInfo?.updateTime) }}</el-descriptions-item>
      </el-descriptions>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '../stores/counter';

const router = useRouter();
const userStore = useUserStore();
const userInfo = ref(null);
const loading = ref(false);

const formatDate = (dateStr: string) => {
  if (!dateStr) return '未知';
  return new Date(dateStr).toLocaleString();
};

const fetchUserInfo = async () => {
  loading.value = true;
  try {
    const res = await fetch('/api/user/getuser');
    const data = await res.json();
    if (data.code === 0 && data.data) {
      userInfo.value = data.data;
      userStore.setUser(data.data);
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

const handleEdit = () => {
  router.push('/user-edit');
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
.page-full-center {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: flex-start;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
el-card {
  width: 100%;
  max-width: 800px;
  margin: 40px auto;
}
</style> 