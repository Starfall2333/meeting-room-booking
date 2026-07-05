<template>
  <div class="register-container">
    <el-card class="register-card" shadow="always">
      <template #header>
        <div class="card-header">
          <h2>注册新账号</h2>
          <span>欢迎加入会议预约系统</span>
        </div>
      </template>

      <el-form
        :model="registerForm"
        :rules="rules"
        ref="registerFormRef"
        label-width="80px"
        status-icon
      >
        <el-form-item label="用户名" prop="username">
          <el-input v-model="registerForm.username" placeholder="请输入用户名（4-16位字母或数字）" />
        </el-form-item>

        <el-form-item label="邮箱" prop="email">
          <el-input v-model="registerForm.email" placeholder="请输入邮箱（选填）" />
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input v-model="registerForm.password" type="password" placeholder="请设置密码（至少6位）" />
        </el-form-item>

        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="registerForm.confirmPassword" type="password" placeholder="请再次输入密码" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" style="width: 100%" @click="handleRegister" :loading="loading">
            注 册
          </el-button>
        </el-form-item>

        <div style="text-align: center; font-size: 14px; color: #909399">
          已有账号？
          <el-link type="primary" @click="$router.push('/login')">去登录</el-link>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage, type FormInstance, type FormRules } from 'element-plus';
import { registerApi } from '@/api/auth';

const router = useRouter();
const registerFormRef = ref<FormInstance>();
const loading = ref(false);

const registerForm = reactive({
  username: '',
  email: '',
  password: '',
  confirmPassword: '',
});

// 自定义校验规则：校验确认密码是否与密码一致
const validateConfirmPassword = (rule: any, value: string, callback: any) => {
  if (value !== registerForm.password) {
    callback(new Error('两次输入的密码不一致'));
  } else {
    callback();
  }
};

const rules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 4, max: 16, message: '用户名长度在 4 到 16 个字符', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9]+$/, message: '用户名只能包含字母和数字', trigger: 'blur' },
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' },
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码至少 6 位', trigger: 'blur' },
  ],
  confirmPassword: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' },
  ],
};

const handleRegister = async () => {
  if (!registerFormRef.value) return;
  await registerFormRef.value.validate();

  loading.value = true;
  try {
    await registerApi({
      username: registerForm.username,
      password: registerForm.password,
      email: registerForm.email,
    });
    ElMessage.success('注册成功，请登录！');
    // 跳转到登录页
    await router.push('/login');
  } catch (error) {
    console.error('注册失败', error);
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}
.register-card {
  width: 460px;
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