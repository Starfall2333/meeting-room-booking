<template>
  <div class="login-container">
    <el-card class="login-card" shadow="always">
      <template #header>
        <div class="card-header">
          <h2>校园会议室预约系统</h2>
          <span>用户登录</span>
        </div>
      </template>

      <!-- 登录表单 -->
      <el-form
        :model="loginForm"
        :rules="rules"
        ref="loginFormRef"
        label-width="80px"
        status-icon
      >
        <el-form-item label="用户名" prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入用户名" />
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            @keyup.enter="handleLogin"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" style="width: 100%" @click="handleLogin" :loading="loading">
            登 录
          </el-button>
        </el-form-item>

        <div style="text-align: center; font-size: 14px; color: #909399">
          还没有账号？
          <el-link type="primary" @click="$router.push('/register')">立即注册</el-link>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage, type FormInstance, type FormRules } from 'element-plus';
import { loginApi } from '@/api/auth';

const router = useRouter();
const loginFormRef = ref<FormInstance>();
const loading = ref(false);

// 表单数据
const loginForm = reactive({
  username: '',
  password: '',
});

// 表单校验规则
const rules: FormRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
};

// 登录逻辑
const handleLogin = async () => {
  if (!loginFormRef.value) return;
  
  // 1. 执行前端表单校验
  await loginFormRef.value.validate();

  loading.value = true;
  try {
    // 2. 调用后端登录接口
    const res = await loginApi({
      username: loginForm.username,
      password: loginForm.password,
    });

    // 3. 存储 Token 和用户信息（假设后端返回 { token, username, role }）
    localStorage.setItem('token', res.token);
    localStorage.setItem('username', res.username);
    localStorage.setItem('role', res.role);

    ElMessage.success('登录成功！');
    
    // 4. 跳转到首页（会议室列表）
    await router.push('/rooms');
  } catch (error) {
    // 错误已在 request 拦截器中处理，这里只需要做额外补充
    console.error('登录失败', error);
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}
.login-card {
  width: 420px;
  border-radius: 12px;
}
.card-header {
  text-align: center;
}
.card-header h2 {
  margin: 0;
  color: #333;
}
.card-header span {
  color: #888;
  font-size: 14px;
}
</style>