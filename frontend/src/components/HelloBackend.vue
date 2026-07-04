<template>
  <div class="hello-backend">
    <h2>后端连通性测试</h2>
    <button @click="fetchHello">发送请求</button>
    <p v-if="responseData">后端返回：{{ responseData }}</p>
    <p v-if="errorMsg" style="color:red">错误：{{ errorMsg }}</p>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import axios from 'axios'

// 定义响应数据的状态
const responseData = ref<string>('')
const errorMsg = ref<string>('')

// 请求函数
const fetchHello = async () => {
  try {
    // 清空之前的错误信息
    errorMsg.value = ''
    // 发送 GET 请求，注意 URL 要完整（包含协议、主机、端口）
    // 如果你的后端运行在 localhost:8080，则写 'http://localhost:8080/api/v1/hello'
    // 由于已配置跨域，可以直接写完整地址
    const response = await axios.get('http://localhost:8080/api/v1/hello')
    // 假设后端返回的是一个字符串或对象，这里直接取 data
    // 如果返回的是对象，可以 JSON.stringify(response.data)
    responseData.value = typeof response.data === 'string' 
      ? response.data 
      : JSON.stringify(response.data)
  } catch (error: any) {
    // 处理错误：网络错误或后端返回的非2xx状态码
    if (error.response) {
      // 服务器响应了但状态码不在2xx
      errorMsg.value = `请求失败：状态码 ${error.response.status}`
    } else if (error.request) {
      // 请求已发出但没有收到响应
      errorMsg.value = '请求超时或后端服务未启动'
    } else {
      // 其他错误
      errorMsg.value = `发生错误：${error.message}`
    }
    // 清空之前的成功数据
    responseData.value = ''
  }
}
</script>

<style scoped>
.hello-backend {
  margin: 20px;
}
button {
  padding: 8px 16px;
  font-size: 16px;
  cursor: pointer;
}
</style>