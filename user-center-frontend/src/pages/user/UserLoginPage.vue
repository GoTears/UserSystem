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
          // { min: 8, message: '密码不能小于 8 位' }
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
import type { AxiosError } from "axios";


import { userLogin } from "@/api/user";
import { useLoginUserStore } from "@/store/useLoginUserStore";

interface FormState {
  userAccount: string;
  userPassword: string;
}

const formState = reactive<FormState>({
  userAccount: "",
  userPassword: "",
});

const router = useRouter();
const loginUserStore = useLoginUserStore();

/**
 * 判断是否属于登录错误（统一提示"账号或密码错误"）
 */
const isLoginError = (msg: string = ""): boolean => {
  const keywords = [
    "用户不存在",
    "密码错误",
    "请求数据为空",
    "账号或密码为空",
    "用户账号过短",
    "用户密码过短",
    "请求参数错误",
  ];
  return keywords.some((k) => msg.includes(k));
};

const handleSubmit = async (values: FormState) => {
  try {
    const res = await userLogin(values);

    // ---- ① 登录成功 ----
    if (res.data.code === 0 && res.data.data) {
      await loginUserStore.fetchLoginUser();
      message.success("登录成功");
      router.replace("/");
      return;
    }

    // ---- ② 登录失败（后端返回错误码）----
    const backendMsg = res.data.message || res.data.description || "登录失败";

    if (isLoginError(backendMsg)) {
      message.error("账号或密码错误");
    } else {
      message.error(backendMsg);
    }
  }
  catch (error) {
    const err = error as AxiosError<{ message?: string }>;

    const backendMsg =
      err.response?.data?.message || "网络错误，请稍后重试";

    const finalMsg = isLoginError(backendMsg)
      ? "账号或密码错误"
      : backendMsg;

    message.error(finalMsg);
    console.error(err);
  }
  // catch (error: unknown) {
  //   // ---- ③ Axios 错误 ----
  //   const axiosError = error as AxiosError;

  //   let backendMsg =
  //   (axiosError.response?.data as any)?.message ||
  //   "网络错误，请稍后重试";


  //   if (isLoginError(backendMsg)) {
  //     backendMsg = "账号或密码错误";
  //   }

  //   message.error(backendMsg);
  //   console.error(error);
  // }
};
</script>

<style scoped>
#userLoginPage .title {
  text-align: center;
  margin-bottom: 16px;
}
</style>
