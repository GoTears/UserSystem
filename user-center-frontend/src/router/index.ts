import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LoginView from '../views/LoginView.vue'
import RegisterView from '../views/RegisterView.vue'
import UserCenterView from '../views/UserCenterView.vue'
import UserListView from '../views/UserListView.vue'
import UserEditView from '../views/UserEditView.vue'
import NotFound from '../views/NotFound.vue'
import { useUserStore } from '../stores/counter'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/about',
      name: 'about',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../views/AboutView.vue'),
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView,
    },
    {
      path: '/register',
      name: 'register',
      component: RegisterView,
    },
    {
      path: '/user-center',
      name: 'user-center',
      component: UserCenterView,
      meta: { requiresAuth: true }
    },
    {
      path: '/user-list',
      name: 'user-list',
      component: UserListView,
      meta: { requiresAuth: true, requiresAdmin: true }
    },
    {
      path: '/user-edit',
      name: 'user-edit',
      component: UserEditView,
      meta: { requiresAuth: true }
    },
    {
      path: '/:pathMatch(.*)*',
      name: 'notfound',
      component: NotFound,
    },
  ],
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const userStore = useUserStore();
  
  // 需要登录的页面
  if (to.meta.requiresAuth && !userStore.isLogin) {
    next('/login');
    return;
  }
  
  // 需要管理员权限的页面
  if (to.meta.requiresAdmin && userStore.user?.userRole !== 1) {
    next('/');
    return;
  }
  
  // 已登录用户访问登录页，跳转到首页
  if (to.path === '/login' && userStore.isLogin) {
    next('/');
    return;
  }
  
  next();
});

export default router
