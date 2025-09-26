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
  const loginUser = ref<LoginUser>({
    username: "未登录",
  });

  async function fetchLoginUser() {
    const res = await getCurrentUser();
    if (res.data.code === 0 && res.data.data) {
      loginUser.value = res.data.data;
    }
  }

  function setLoginUser(newLoginUser: LoginUser) {
    loginUser.value = newLoginUser;
  }

  return { loginUser, setLoginUser, fetchLoginUser };
});
