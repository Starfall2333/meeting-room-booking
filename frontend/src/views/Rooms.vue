<template>
  <div class="room-management">
    <!-- 页面标题和操作栏 -->
    <div class="header">
      <h2>会议室管理</h2>
      <div class="actions">
        <el-input
          v-model="keyword"
          placeholder="按名称搜索"
          clearable
          style="width: 200px; margin-right: 10px"
          @clear="fetchRooms"
          @keyup.enter="fetchRooms"
        />
        <el-button type="primary" @click="openAddDialog">新增会议室</el-button>
        <el-button type="danger" plain @click="handleLogout">退出</el-button>
      </div>
    </div>

    <!-- 表格 -->
    <el-table :data="tableData" border stripe style="width: 100%" v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="会议室名称" min-width="150" />
      <el-table-column prop="location" label="地点" min-width="150" />
      <el-table-column prop="capacity" label="容纳人数" width="100" />
      <el-table-column prop="description" label="描述" min-width="150" show-overflow-tooltip />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 'available' ? 'success' : 'danger'">
            {{ row.status === 'available' ? '可用' : '维护中' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" size="small" @click="openEditDialog(row)">编辑</el-button>
          <el-button type="danger" size="small" @click="handleDelete(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="500px"
      :close-on-click-modal="false"
      @close="resetForm"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-width="100px"
        status-icon
      >
        <el-form-item label="会议室名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item label="地点" prop="location">
          <el-input v-model="formData.location" placeholder="请输入地点" />
        </el-form-item>
        <el-form-item label="容纳人数" prop="capacity">
          <el-input-number v-model="formData.capacity" :min="1" :max="999" style="width: 100%" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="formData.description" type="textarea" :rows="2" placeholder="可选" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="formData.status">
            <el-radio label="available">可用</el-radio>
            <el-radio label="maintenance">维护中</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm" :loading="submitting">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus';
import { getRooms, addRoom, updateRoom, deleteRoom, type Room, type RoomRequest } from '@/api/room';

const router = useRouter();

// ---------- 数据 ----------
const tableData = ref<Room[]>([]);
const loading = ref(false);
const keyword = ref('');

// 对话框相关
const dialogVisible = ref(false);
const dialogTitle = ref('新增会议室');
const isEdit = ref(false);
const editId = ref<number | null>(null);
const submitting = ref(false);

const formRef = ref<FormInstance>();
const formData = reactive<RoomRequest>({
  name: '',
  location: '',
  capacity: 1,
  description: '',
  status: 'available',
});

// 表单校验规则
const rules: FormRules = {
  name: [{ required: true, message: '请输入会议室名称', trigger: 'blur' }],
  location: [{ required: true, message: '请输入地点', trigger: 'blur' }],
  capacity: [{ required: true, message: '请输入容纳人数', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }],
};

// ---------- 方法 ----------
// 加载列表
const fetchRooms = async () => {
  loading.value = true;
  try {
    const res = await getRooms(keyword.value);
    tableData.value = res;
  } catch (error) {
    console.error('获取列表失败', error);
  } finally {
    loading.value = false;
  }
};

// 打开新增对话框
const openAddDialog = () => {
  isEdit.value = false;
  editId.value = null;
  dialogTitle.value = '新增会议室';
  dialogVisible.value = true;
  // 重置表单（后续 resetForm 会在关闭时调用）
};

// 打开编辑对话框
const openEditDialog = (row: Room) => {
  isEdit.value = true;
  editId.value = row.id!;
  dialogTitle.value = '编辑会议室';
  // 回填数据
  formData.name = row.name;
  formData.location = row.location;
  formData.capacity = row.capacity;
  formData.description = row.description || '';
  formData.status = row.status || 'available';
  dialogVisible.value = true;
};

// 重置表单
const resetForm = () => {
  formRef.value?.resetFields();
  formData.name = '';
  formData.location = '';
  formData.capacity = 1;
  formData.description = '';
  formData.status = 'available';
  isEdit.value = false;
  editId.value = null;
};

// 提交表单（新增或更新）
const submitForm = async () => {
  if (!formRef.value) return;
  await formRef.value.validate();

  submitting.value = true;
  try {
    if (isEdit.value && editId.value !== null) {
      // 更新
      await updateRoom(editId.value, formData);
      ElMessage.success('更新成功');
    } else {
      // 新增
      await addRoom(formData);
      ElMessage.success('新增成功');
    }
    dialogVisible.value = false;
    // 刷新列表
    await fetchRooms();
  } catch (error) {
    console.error('提交失败', error);
    // 错误已在拦截器中提示，这里不重复
  } finally {
    submitting.value = false;
  }
};

// 删除
const handleDelete = (id: number) => {
  ElMessageBox.confirm('确定要删除该会议室吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  })
    .then(async () => {
      try {
        await deleteRoom(id);
        ElMessage.success('删除成功');
        await fetchRooms();
      } catch (error) {
        console.error('删除失败', error);
      }
    })
    .catch(() => {});
};

// 退出登录
const handleLogout = () => {
  localStorage.clear();
  ElMessage.info('已退出');
  router.push('/login');
};

// ---------- 生命周期 ----------
onMounted(() => {
  fetchRooms();
});
</script>

<style scoped>
.room-management {
  padding: 20px;
}
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  flex-wrap: wrap;
}
.actions {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
}
</style>