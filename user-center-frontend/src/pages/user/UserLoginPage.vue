<template>
  <div id="userLoginPage">
    <h2 class="title">用户登录</h2>
    <a-form
      style="max-width: 480px; margin: 0 auto"
      :model="formState"
      name="loginForm"
      label-align="left"
      :label-col="{ span: 4 }"
      :wrapper-col="{ span: 20 }"
      autocomplete="off"
      @finish="handleSubmit"
    >
      <!-- 账号 -->
      <a-form-item
        label="账号"
        name="userAccount"
        :rules="[{ required: true, message: '请输入账号' }]"
      >
        <a-input
          v-model:value="formState.userAccount"
          placeholder="请输入账号"
        />
      </a-form-item>

      <!-- 密码 -->
      <a-form-item
        label="密码"
        name="userPassword"
        :rules="[
          { required: true, message: '请输入密码' },
          { min: 8, message: '密码不能小于 8 位' }
        ]"
      >
        <a-input-password
          v-model:value="formState.userPassword"
          placeholder="请输入密码"
        />
      </a-form-item>

      <!-- 登录按钮 -->
      <a-form-item :wrapper-col="{ offset: 4, span: 20 }">
        <a-button type="primary" html-type="submit">登录</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script lang="ts" setup>
import { reactive } from "vue";
import { useRouter } from "vue-router";
import { message } from "ant-design-vue";

import { userLogin } from "@/api/user";
import { useLoginUserStore } from "@/store/useLoginUserStore";

// 定义表单状态类型
interface FormState {
  userAccount: string;
  userPassword: string;
}

// 初始化表单状态
const formState = reactive<FormState>({
  userAccount: "",
  userPassword: "",
});

// 获取路由和全局登录用户状态
const router = useRouter();
const loginUserStore = useLoginUserStore();

/**
 * 提交表单
 * @param values 表单值
 */
const handleSubmit = async (values: FormState) => {
  try {
    const res = await userLogin(values);

    if (res.data.code === 0 && res.data.data) {
      // 登录成功，刷新全局用户状态
      await loginUserStore.fetchLoginUser();
      message.success("登录成功");

      // 跳转首页
      router.replace("/");
    } else {
      message.error(res.data.message || "登录失败");
    }
  } catch (error) {
    message.error("网络或服务器错误，请稍后重试");
    console.error(error);
  }
};
</script>

<style scoped>
#userLoginPage .title {
  text-align: center;
  margin-bottom: 16px;
}
</style>
