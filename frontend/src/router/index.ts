import { createRouter, createWebHistory } from 'vue-router';

// 注意：Layout 或 Rooms 页面下一次才写，这里先占位
const routes = [
  {
    path: '/',
    redirect: '/rooms', // 默认跳转到会议室列表
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { requiresAuth: false }, // 无需登录
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register.vue'),
    meta: { requiresAuth: false },
  },
  {
    path: '/rooms',
    name: 'Rooms',
    component: () => import('@/views/Rooms.vue'),
    meta: { requiresAuth: true }, // 必须登录才能访问
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// ----- 全局前置守卫（核心：拦截未登录访问） -----
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token');
  
  // 如果目标页面需要登录，且没有 token
  if (to.meta.requiresAuth && !token) {
    next('/login'); // 跳转到登录页
  } else {
    // 如果已登录，但不能访问登录/注册页（防止重复登录）
    if (token && (to.path === '/login' || to.path === '/register')) {
      next('/rooms'); // 跳转到首页
    } else {
      next(); // 正常放行
    }
  }
});

export default router;