import { defineStore } from "pinia";
import { ref } from "vue";
import { getCurrentUser } from "@/api/user";

// 定义用户类型
export interface LoginUser {
  username: string;
  // 根据实际接口补充其他字段
  [key: string]: any;
}

export const useLoginUserStore = defineStore("loginUser", () => {
  const defaultUser: LoginUser = {
    username: "未登录",
  };
  const loginUser = ref<LoginUser>({ ...defaultUser });

  async function fetchLoginUser() {
    const res = await getCurrentUser();
    if (res.data.code === 0 && res.data.data) {
      loginUser.value = res.data.data;
    }
  }

  function setLoginUser(newLoginUser: LoginUser) {
    loginUser.value = newLoginUser;
  }

  function resetLoginUser() {
    loginUser.value = { ...defaultUser };
  }

  return { loginUser, setLoginUser, fetchLoginUser, resetLoginUser };
},

{
  //持久化
  persist: [
    {
      key: "loginUser",
      storage: localStorage, // 或 sessionStorage
      // paths: ["loginUser"],   // 指定持久化字段
    },
  ],
  },
);
