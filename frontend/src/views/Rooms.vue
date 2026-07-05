<template>
  <div style="padding: 20px">
    <h2>会议室管理</h2>
    <p>当前用户：{{ username }} ({{ role }})</p>
    <el-button type="danger" @click="handleLogout">退出登录</el-button>
    <!-- 这里 Sprint 2 Day 4 会放表格 -->
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';

const router = useRouter();
const username = ref(localStorage.getItem('username') || '');
const role = ref(localStorage.getItem('role') || '');

const handleLogout = () => {
  localStorage.clear();
  ElMessage.info('已退出');
  router.push('/login');
};
</script>