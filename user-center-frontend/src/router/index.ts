import { createRouter, createWebHistory, type RouteRecordRaw } from "vue-router";

// 路由配置
const routes: RouteRecordRaw[] = [
  {
    path: "/",
    name: "home",
    component: () => import("@/pages/HomePage.vue"),
  },
  {
    path: "/user/login",
    name: "userLogin",
    component: () => import("@/pages/user/UserLoginPage.vue"),
  },
  {
    path: "/user/register",
    name: "userRegister",
    component: () => import("@/pages/user/UserRegisterPage.vue"),
  },
  {
    path: "/admin/userManage",
    name: "adminUserManage",
    component: () => import("@/pages/admin/UserManagePage.vue"),
  },
];

// 创建路由
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
});

export default router;