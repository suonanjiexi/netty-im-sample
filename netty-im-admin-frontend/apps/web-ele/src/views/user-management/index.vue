<template>
  <div class="user-management">
    <h1>用户管理</h1>
    <el-card>
      <el-form :model="queryForm" :inline="true" label-width="80px">
        <el-form-item label="关键词">
          <el-input v-model="queryForm.keyword" placeholder="用户名/昵称" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryForm.status" clearable placeholder="请选择状态">
            <el-option label="正常" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
      
      <el-table :data="tableData" border style="width: 100%" v-loading="loading">
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="nickname" label="昵称" />
        <el-table-column prop="email" label="邮箱" />
        <el-table-column prop="phone" label="手机号" />
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag v-if="row.status === 1" type="success">正常</el-tag>
            <el-tag v-else type="danger">禁用</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="onlineStatus" label="在线状态">
          <template #default="{ row }">
            <el-tag v-if="row.onlineStatus === 1" type="success">在线</el-tag>
            <el-tag v-else type="info">离线</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lastLoginTime" label="最后登录时间" />
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleView(row)">查看</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
            <el-button type="warning" link @click="handleStatusChange(row)">
              {{ row.status === 1 ? '禁用' : '启用' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <el-pagination
        v-model:current-page="queryForm.page"
        v-model:page-size="queryForm.size"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        style="margin-top: 20px; text-align: right;"
      />
    </el-card>
    
    <!-- 查看详情对话框 -->
    <el-dialog v-model="detailDialogVisible" title="用户详情" width="600px">
      <el-form :model="detailForm" label-width="100px" disabled>
        <el-form-item label="用户名">
          <el-input v-model="detailForm.username" />
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="detailForm.nickname" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="detailForm.email" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="detailForm.phone" />
        </el-form-item>
        <el-form-item label="真实姓名">
          <el-input v-model="detailForm.realName" />
        </el-form-item>
        <el-form-item label="身份证号">
          <el-input v-model="detailForm.idCardNumber" />
        </el-form-item>
        <el-form-item label="身份认证状态">
          <el-tag v-if="detailForm.identityStatus === 1" type="success">已认证</el-tag>
          <el-tag v-else type="warning">未认证</el-tag>
        </el-form-item>
        <el-form-item label="状态">
          <el-tag v-if="detailForm.status === 1" type="success">正常</el-tag>
          <el-tag v-else type="danger">禁用</el-tag>
        </el-form-item>
        <el-form-item label="在线状态">
          <el-tag v-if="detailForm.onlineStatus === 1" type="success">在线</el-tag>
          <el-tag v-else type="info">离线</el-tag>
        </el-form-item>
        <el-form-item label="最后登录时间">
          <el-input v-model="detailForm.lastLoginTime" />
        </el-form-item>
        <el-form-item label="创建时间">
          <el-input v-model="detailForm.createdAt" />
        </el-form-item>
        <el-form-item label="更新时间">
          <el-input v-model="detailForm.updatedAt" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  pageUsers, 
  getUserById, 
  deleteUser, 
  updateUserStatus,
  type UserManagementApi
} from '#/api/admin/user-management'

// 查询表单
const queryForm = reactive({
  page: 1,
  size: 10,
  keyword: '',
  status: undefined as number | undefined
})

// 表格数据
const tableData = ref<UserManagementApi.User[]>([])
const total = ref(0)
const loading = ref(false)

// 详情对话框
const detailDialogVisible = ref(false)
const detailForm = ref<UserManagementApi.User>({
  id: 0,
  username: '',
  email: '',
  phone: '',
  nickname: '',
  avatar: '',
  status: 0,
  onlineStatus: 0,
  lastLoginTime: '',
  idCardNumber: '',
  realName: '',
  identityStatus: 0,
  createdAt: '',
  updatedAt: ''
})

// 获取数据
const fetchData = async () => {
  loading.value = true
  try {
    const res = await pageUsers(queryForm)
    tableData.value = res.data
    total.value = res.total
  } catch (error) {
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

// 查询
const handleSearch = () => {
  queryForm.page = 1
  fetchData()
}

// 重置
const handleReset = () => {
  queryForm.keyword = ''
  queryForm.status = undefined
  queryForm.page = 1
  fetchData()
}

// 查看详情
const handleView = async (row: UserManagementApi.User) => {
  try {
    const res = await getUserById(row.id)
    detailForm.value = res
    detailDialogVisible.value = true
  } catch (error) {
    ElMessage.error('获取用户详情失败')
  }
}

// 删除
const handleDelete = (row: UserManagementApi.User) => {
  ElMessageBox.confirm('确定要删除该用户吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await deleteUser(row.id)
      ElMessage.success('删除成功')
      fetchData()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

// 状态变更
const handleStatusChange = async (row: UserManagementApi.User) => {
  try {
    await updateUserStatus(row.id, row.status === 1 ? 0 : 1)
    ElMessage.success('状态更新成功')
    fetchData()
  } catch (error) {
    ElMessage.error('状态更新失败')
  }
}

// 分页
const handleSizeChange = (val: number) => {
  queryForm.size = val
  fetchData()
}

const handleCurrentChange = (val: number) => {
  queryForm.page = val
  fetchData()
}

// 初始化
onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.user-management {
  padding: 20px;
}
</style>