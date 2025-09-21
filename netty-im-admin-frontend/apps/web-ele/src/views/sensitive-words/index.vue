<template>
  <div class="sensitive-words">
    <h1>敏感词管理</h1>
    <el-card>
      <el-form :model="queryForm" :inline="true" label-width="80px">
        <el-form-item label="分类">
          <el-select v-model="queryForm.category" clearable placeholder="请选择分类">
            <el-option label="政治" :value="1" />
            <el-option label="色情" :value="2" />
            <el-option label="暴力" :value="3" />
            <el-option label="广告" :value="4" />
            <el-option label="其他" :value="5" />
          </el-select>
        </el-form-item>
        <el-form-item label="等级">
          <el-select v-model="queryForm.level" clearable placeholder="请选择等级">
            <el-option label="一般" :value="1" />
            <el-option label="敏感" :value="2" />
            <el-option label="危险" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryForm.status" clearable placeholder="请选择状态">
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="关键词">
          <el-input v-model="queryForm.keyword" placeholder="敏感词" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
          <el-button type="primary" @click="handleAdd">新增</el-button>
          <el-button type="danger" @click="handleBatchDelete">批量删除</el-button>
        </el-form-item>
      </el-form>
      
      <el-table 
        :data="tableData" 
        border 
        style="width: 100%" 
        v-loading="loading"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="ID" />
        <el-table-column prop="word" label="敏感词" />
        <el-table-column prop="category" label="分类">
          <template #default="{ row }">
            <el-tag v-if="row.category === 1">政治</el-tag>
            <el-tag v-else-if="row.category === 2" type="danger">色情</el-tag>
            <el-tag v-else-if="row.category === 3" type="warning">暴力</el-tag>
            <el-tag v-else-if="row.category === 4" type="success">广告</el-tag>
            <el-tag v-else-if="row.category === 5">其他</el-tag>
            <span v-else>未知</span>
          </template>
        </el-table-column>
        <el-table-column prop="level" label="等级">
          <template #default="{ row }">
            <el-tag v-if="row.level === 1">一般</el-tag>
            <el-tag v-else-if="row.level === 2" type="warning">敏感</el-tag>
            <el-tag v-else-if="row.level === 3" type="danger">危险</el-tag>
            <span v-else>未知</span>
          </template>
        </el-table-column>
        <el-table-column prop="action" label="处理方式">
          <template #default="{ row }">
            <el-tag v-if="row.action === 1">替换</el-tag>
            <el-tag v-else-if="row.action === 2" type="warning">阻止</el-tag>
            <el-tag v-else-if="row.action === 3" type="danger">标记</el-tag>
            <span v-else>未知</span>
          </template>
        </el-table-column>
        <el-table-column prop="replacement" label="替换词" />
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag v-if="row.status === 1" type="success">启用</el-tag>
            <el-tag v-else type="danger">禁用</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" />
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
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
    
    <!-- 新增/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="敏感词" prop="word">
          <el-input v-model="form.word" placeholder="请输入敏感词" />
        </el-form-item>
        <el-form-item label="分类" prop="category">
          <el-select v-model="form.category" style="width: 100%" placeholder="请选择分类">
            <el-option label="政治" :value="1" />
            <el-option label="色情" :value="2" />
            <el-option label="暴力" :value="3" />
            <el-option label="广告" :value="4" />
            <el-option label="其他" :value="5" />
          </el-select>
        </el-form-item>
        <el-form-item label="等级" prop="level">
          <el-select v-model="form.level" style="width: 100%" placeholder="请选择等级">
            <el-option label="一般" :value="1" />
            <el-option label="敏感" :value="2" />
            <el-option label="危险" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="处理方式" prop="action">
          <el-select v-model="form.action" style="width: 100%" placeholder="请选择处理方式">
            <el-option label="替换" :value="1" />
            <el-option label="阻止" :value="2" />
            <el-option label="标记" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="替换词" prop="replacement">
          <el-input v-model="form.replacement" placeholder="请输入替换词" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch
            v-model="form.status"
            :active-value="1"
            :inactive-value="0"
            active-text="启用"
            inactive-text="禁用"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance } from 'element-plus'
import { 
  pageSensitiveWords, 
  addSensitiveWord, 
  updateSensitiveWord, 
  deleteSensitiveWord, 
  batchDeleteSensitiveWords,
  updateSensitiveWordStatus,
  batchUpdateSensitiveWordStatus,
  type SensitiveWordApi
} from '#/api/admin/sensitive-word'

// 查询表单
const queryForm = reactive({
  page: 1,
  size: 10,
  category: undefined as number | undefined,
  level: undefined as number | undefined,
  status: undefined as number | undefined,
  keyword: ''
})

// 表格数据
const tableData = ref<SensitiveWordApi.SensitiveWord[]>([])
const total = ref(0)
const loading = ref(false)
const selectedWords = ref<SensitiveWordApi.SensitiveWord[]>([])

// 对话框
const dialogVisible = ref(false)
const dialogTitle = ref('')
const isAdd = ref(true)
const formRef = ref<FormInstance>()

// 表单数据
const form = reactive<SensitiveWordApi.SensitiveWord>({
  id: 0,
  word: '',
  category: 1,
  level: 1,
  action: 1,
  replacement: '',
  status: 1,
  creatorId: 1,
  createdAt: '',
  updatedAt: ''
})

// 表单验证规则
const rules = {
  word: [{ required: true, message: '请输入敏感词', trigger: 'blur' }],
  category: [{ required: true, message: '请选择分类', trigger: 'change' }],
  level: [{ required: true, message: '请选择等级', trigger: 'change' }],
  action: [{ required: true, message: '请选择处理方式', trigger: 'change' }]
}

// 获取数据
const fetchData = async () => {
  loading.value = true
  try {
    const res = await pageSensitiveWords(queryForm)
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
  queryForm.category = undefined
  queryForm.level = undefined
  queryForm.status = undefined
  queryForm.keyword = ''
  queryForm.page = 1
  fetchData()
}

// 选择变更
const handleSelectionChange = (selection: SensitiveWordApi.SensitiveWord[]) => {
  selectedWords.value = selection
}

// 添加
const handleAdd = () => {
  dialogTitle.value = '新增敏感词'
  isAdd.value = true
  // 重置表单
  Object.assign(form, {
    id: 0,
    word: '',
    category: 1,
    level: 1,
    action: 1,
    replacement: '',
    status: 1,
    creatorId: 1,
    createdAt: '',
    updatedAt: ''
  })
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row: SensitiveWordApi.SensitiveWord) => {
  dialogTitle.value = '编辑敏感词'
  isAdd.value = false
  // 填充表单数据
  Object.assign(form, row)
  dialogVisible.value = true
}

// 删除
const handleDelete = (row: SensitiveWordApi.SensitiveWord) => {
  ElMessageBox.confirm('确定要删除该敏感词吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await deleteSensitiveWord(row.id)
      ElMessage.success('删除成功')
      fetchData()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

// 批量删除
const handleBatchDelete = () => {
  if (selectedWords.value.length === 0) {
    ElMessage.warning('请先选择要删除的敏感词')
    return
  }
  
  ElMessageBox.confirm(`确定要删除选中的${selectedWords.value.length}个敏感词吗？`, '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      const ids = selectedWords.value.map(item => item.id).join(',')
      await batchDeleteSensitiveWords({ ids })
      ElMessage.success('批量删除成功')
      fetchData()
    } catch (error) {
      ElMessage.error('批量删除失败')
    }
  }).catch(() => {})
}

// 状态变更
const handleStatusChange = async (row: SensitiveWordApi.SensitiveWord) => {
  try {
    await updateSensitiveWordStatus(row.id, row.status === 1 ? 0 : 1)
    ElMessage.success('状态更新成功')
    fetchData()
  } catch (error) {
    ElMessage.error('状态更新失败')
  }
}

// 提交表单
const handleSubmit = () => {
  if (!formRef.value) return
  formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (isAdd.value) {
          await addSensitiveWord(form)
          ElMessage.success('新增成功')
        } else {
          await updateSensitiveWord(form.id, form)
          ElMessage.success('更新成功')
        }
        dialogVisible.value = false
        fetchData()
      } catch (error) {
        ElMessage.error(isAdd.value ? '新增失败' : '更新失败')
      }
    }
  })
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
.sensitive-words {
  padding: 20px;
}
</style>