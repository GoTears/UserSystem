<template>
  <div id="userLoginPage">
    <h2 class="title">用户登录</h2>
    <el-form
      :model="formState"
      ref="formRef"
      label-width="60px"
      style="max-width: 480px; margin: 0 auto"
      @submit.prevent="handleSubmit"
    >
      <el-form-item
        label="账号"
        prop="userAccount"
        :rules="[{ required: true, message: '请输入账号', trigger: 'blur' }]"
      >
        <el-input
          v-model="formState.userAccount"
          placeholder="请输入账号"
          :class="{ 'error-input': accountError }"
          @input="clearAccountError"
        />
      </el-form-item>
      <el-form-item
        label="密码"
        prop="userPassword"
        :rules="[
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 8, message: '密码不能小于 8 位', trigger: 'blur' }
        ]"
      >
        <el-input
          v-model="formState.userPassword"
          type="password"
          placeholder="请输入密码"
          :class="{ 'error-input': passwordError }"
          @input="clearPasswordError"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSubmit" :loading="loading">登录</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from "vue";
import { ElMessage } from "element-plus";
import { useRouter } from "vue-router";
import { useUserStore } from "../stores/counter";

const formState = reactive({
  userAccount: "",
  userPassword: "",
});
const formRef = ref();
const router = useRouter();
const userStore = useUserStore();
const loading = ref(false);
const accountError = ref(false);
const passwordError = ref(false);

const clearAccountError = () => {
  accountError.value = false;
};

const clearPasswordError = () => {
  passwordError.value = false;
};

const handleSubmit = async () => {
  await formRef.value.validate(async (valid: boolean) => {
    if (!valid) return;
    
    loading.value = true;
    try {
      const res = await fetch("/api/user/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(formState),
      });
      const data = await res.json();
      if (data.code === 0 && data.data) {
        userStore.setUser(data.data);
        ElMessage.success("登录成功");
        router.push({ path: "/", replace: true });
      } else {
        // 根据安全性原则处理错误信息
        const errorMessage = data.message || "登录失败";
        
        // 分类处理不同类型的错误
        if (errorMessage.includes("账号或密码为空")) {
          // 参数缺失：显示具体提示
          ElMessage.warning("请填写完整的登录信息");
        } else if (errorMessage.includes("用户账号过短") || errorMessage.includes("账户不能包含特殊字符")) {
          // 账号格式错误：高亮账号框，显示格式提示
          accountError.value = true;
          ElMessage.warning("账号格式不正确，请检查");
        } else if (errorMessage.includes("用户密码过短")) {
          // 密码格式错误：高亮密码框，清空密码
          passwordError.value = true;
          formState.userPassword = "";
          ElMessage.warning("密码格式不正确，请重新输入");
        } else {
          // 登录失败（包括用户不存在、密码错误等）：统一显示，不泄露具体原因
          ElMessage.error("账号或密码错误");
          
          // 清空密码框，提供更好的用户体验
          formState.userPassword = "";
        }
      }
    } catch (error) {
      ElMessage.error("网络错误，请稍后重试");
    } finally {
      loading.value = false;
    }
  });
};
</script>

<style scoped>
#userLoginPage {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
#userLoginPage .title {
  text-align: center;
  margin-bottom: 16px;
}

.error-input {
  border-color: #f56c6c !important;
}

.error-input:focus {
  border-color: #f56c6c !important;
  box-shadow: 0 0 0 2px rgba(245, 108, 108, 0.2) !important;
}
</style> 