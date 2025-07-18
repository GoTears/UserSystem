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
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSubmit">登录</el-button>
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

const handleSubmit = async () => {
  await formRef.value.validate(async (valid: boolean) => {
    if (!valid) return;
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
      ElMessage.error(data.message || "登录失败");
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
</style> 