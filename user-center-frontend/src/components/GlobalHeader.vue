<template>
  <div id="globalHeader">
    <a-row :wrap="false">
      <a-col flex="200px">
        <div class="title-bar">
          <!-- <img class="logo" src="../assets/logo.png" alt="logo" /> -->
          <div class="title">泪痕用户中心</div>
        </div>
      </a-col>
      <a-col flex="auto">
        <a-menu
          v-model:selectedKeys="current"
          mode="horizontal"
          :items="items"
          @click="doMenuClick"
        />
      </a-col>
      <a-col flex="80px">
        <div class="user-login-status">
          <div v-if="loginUserStore.loginUser.id" class="user-info">
            <span class="username">{{ displayName }}</span>
            <a-button type="link" class="logout-btn" @click="logout">退出登录</a-button>
          </div>
          <div v-else>
            <a-button type="primary" href="/user/login">登录</a-button>
          </div>
        </div>
      </a-col>
    </a-row>
  </div>
</template>

<script lang="ts" setup>
import { h, ref, computed } from "vue";
import { CrownOutlined, HomeOutlined } from "@ant-design/icons-vue";
import type { MenuProps } from "ant-design-vue";
import { message } from "ant-design-vue";
import { useRouter } from "vue-router";
import { useLoginUserStore } from "@/store/useLoginUserStore";
import { userLogout } from "@/api/user";

const loginUserStore = useLoginUserStore();

const router = useRouter();
// 点击菜单后的路由跳转事件
const doMenuClick = ({ key }: { key: string }) => {
  router.push({
    path: key,
  });
};

const current = ref<string[]>(["mail"]);
// 监听路由变化，更新当前菜单选中状态
router.afterEach((to, from, failure) => {
  current.value = [to.path];
});

const displayName = computed(
  () =>
    loginUserStore.loginUser.username ||
    loginUserStore.loginUser.userAccount ||
    "无名"
);

const logout = async () => {
  try {
    await userLogout();
    message.success("已退出登录");
  } catch (error) {
    message.error("退出失败，请重试");
    return;
  }
  loginUserStore.resetLoginUser();
  router.push("/user/login");
};

const items = ref<MenuProps["items"]>([
  {
    key: "/",
    icon: () => h(HomeOutlined),
    label: "主页",
    title: "主页",
  },
  {
    key: "/user/login",
    label: "用户登录",
    title: "用户登录",
  },
  {
    key: "/user/register",
    label: "用户注册",
    title: "用户注册",
  },
  {
    key: "/admin/userManage",
    icon: () => h(CrownOutlined),
    label: "用户管理",
    title: "用户管理",
  },
//   {
//     key: "others",
//     label: h(
//       "a",
//       { href: "https://www.codefather.cn", target: "_blank" },
//       "编程导航"
//     ),
//     title: "编程导航",
//   },
]);
</script>

<style scoped>
.title-bar {
  display: flex;
  align-items: center;
}

.title {
  color: black;
  font-size: 18px;
  margin-left: 16px;
}

.logo {
  height: 48px;
}

.user-info {
  /* display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 2px; */
  display: flex;
  flex-direction: row;     /* 从 column 改为 row */
  align-items: center;
  gap: 8px;   
}

.username {
  font-weight: 600;
  color: #333;
}

.logout-btn {
 font-weight: 600;
  padding: 0;
  margin-top: -4px;   /* ✨ 往上移 */
  line-height: 1;     /* 减少按钮高度 */
}
</style>
