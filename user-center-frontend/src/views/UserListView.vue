<template>
  <div class="user-list-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>用户管理</span>
          <el-input
            v-model="searchUsername"
            placeholder="搜索用户名"
            style="width: 200px;"
            @input="handleSearch"
            clearable
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
        </div>
      </template>
      
      <el-table :data="userList" v-loading="loading" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="userAccount" label="账号" width="120" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="email" label="邮箱" width="180" />
        <el-table-column prop="phone" label="手机号" width="120" />
        <el-table-column prop="userRole" label="角色" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.userRole === 1 ? 'danger' : 'info'">
              {{ scope.row.userRole === 1 ? '管理员' : '普通用户' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180">
          <template #default="scope">
            {{ formatDate(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120">
          <template #default="scope">
            <el-button 
              type="danger" 
              size="small" 
              @click="handleDelete(scope.row)"
              :disabled="scope.row.userRole === 1"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '../stores/counter';
import { Search } from '@element-plus/icons-vue';
import { ElMessage, ElMessageBox } from 'element-plus';

const router = useRouter();
const userStore = useUserStore();
const userList = ref([]);
const loading = ref(false);
const searchUsername = ref('');

const formatDate = (dateStr: string) => {
  if (!dateStr) return '未知';
  return new Date(dateStr).toLocaleString();
};

const fetchUserList = async () => {
  loading.value = true;
  try {
    const params = searchUsername.value ? `?username=${searchUsername.value}` : '';
    const res = await fetch(`/api/user/search${params}`);
    const data = await res.json();
    if (data.code === 0 && data.data) {
      userList.value = data.data;
    } else {
      ElMessage.error(data.message || '获取用户列表失败');
    }
  } catch (e) {
    ElMessage.error('网络错误');
  } finally {
    loading.value = false;
  }
};

const handleSearch = () => {
  fetchUserList();
};

const handleDelete = async (user: any) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除用户 "${user.username || user.userAccount}" 吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
    );
    
    const res = await fetch('/api/user/delete', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(user.id)
    });
    const data = await res.json();
    if (data.code === 0 && data.data) {
      ElMessage.success('删除成功');
      fetchUserList();
    } else {
      ElMessage.error(data.message || '删除失败');
    }
  } catch (e) {
    if (e !== 'cancel') {
      ElMessage.error('删除失败');
    }
  }
};

onMounted(() => {
  if (!userStore.isLogin) {
    router.push('/login');
    return;
  }
  if (userStore.user?.userRole !== 1) {
    ElMessage.error('权限不足');
    router.push('/');
    return;
  }
  fetchUserList();
});
</script>

<style scoped>
.user-list-container {
  padding: 20px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style> 