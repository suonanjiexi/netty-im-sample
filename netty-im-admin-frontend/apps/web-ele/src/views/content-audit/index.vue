<template>
  <div class="content-audit">
    <h1>内容审核</h1>
    <el-card>
      <el-form :model="queryForm" :inline="true" label-width="100px">
        <el-form-item label="内容类型">
          <el-select v-model="queryForm.contentType" clearable placeholder="请选择内容类型">
            <el-option label="动态" :value="1" />
            <el-option label="评论" :value="2" />
            <el-option label="私信" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="审核状态">
          <el-select v-model="queryForm.auditStatus" clearable placeholder="请选择审核状态">
            <el-option label="待审核" :value="0" />
            <el-option label="审核通过" :value="1" />
            <el-option label="审核不通过" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="关键词">
          <el-input v-model="queryForm.keyword" placeholder="敏感词" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
          <el-button type="primary" @click="handleBatchReview">批量审核</el-button>
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
        <el-table-column prop="contentType" label="内容类型">
          <template #default="{ row }">
            <el-tag v-if="row.contentType === 1">动态</el-tag>
            <el-tag v-else-if="row.contentType === 2" type="success">评论</el-tag>
            <el-tag v-else-if="row.contentType === 3" type="warning">私信</el-tag>
            <span v-else>未知</span>
          </template>
        </el-table-column>
        <el-table-column prop="content" label="内容" show-overflow-tooltip />
        <el-table-column prop="auditStatus" label="审核状态">
          <template #default="{ row }">
            <el-tag v-if="row.auditStatus === 0">待审核</el-tag>
            <el-tag v-else-if="row.auditStatus === 1" type="success">审核通过</el-tag>
            <el-tag v-else-if="row.auditStatus === 2" type="danger">审核不通过</el-tag>
            <span v-else>未知</span>
          </template>
        </el-table-column>
        <el-table-column prop="sensitiveWords" label="敏感词" />
        <el-table-column prop="auditTime" label="审核时间" />
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleReview(row)">审核</el-button>
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
    
    <!-- 审核对话框 -->
    <el-dialog v-model="reviewDialogVisible" title="内容审核" width="500px">
      <el-form :model="reviewForm" :rules="reviewRules" ref="reviewFormRef" label-width="100px">
        <el-form-item label="审核结果" prop="auditStatus">
          <el-radio-group v-model="reviewForm.auditStatus">
            <el-radio :label="1">通过</el-radio>
            <el-radio :label="2">不通过</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审核意见" prop="auditResult">
          <el-input 
            v-model="reviewForm.auditResult" 
            type="textarea" 
            :rows="3" 
            placeholder="请输入审核意见"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="reviewDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitReview">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance } from 'element-plus'
import { 
  pageAudits, 
  getAuditById, 
  manualReview, 
  batchReview,
  type ContentAuditApi
} from '#/api/admin/content-audit'

// 查询表单
const queryForm = reactive({
  page: 1,
  size: 10,
  contentType: undefined as number | undefined,
  auditStatus: undefined as number | undefined,
  keyword: ''
})

// 表格数据
const tableData = ref<ContentAuditApi.ContentAudit[]>([])
const total = ref(0)
const loading = ref(false)
const selectedAudits = ref<ContentAuditApi.ContentAudit[]>([])

// 审核对话框
const reviewDialogVisible = ref(false)
const reviewFormRef = ref<FormInstance>()
const currentAuditId = ref(0)

const reviewForm = reactive({
  auditStatus: 1,
  auditResult: ''
})

const reviewRules = {
  auditStatus: [{ required: true, message: '请选择审核结果', trigger: 'change' }],
  auditResult: [{ required: true, message: '请输入审核意见', trigger: 'blur' }]
}

// 获取数据
const fetchData = async () => {
  loading.value = true
  try {
    const res = await pageAudits(queryForm)
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
  queryForm.contentType = undefined
  queryForm.auditStatus = undefined
  queryForm.keyword = ''
  queryForm.page = 1
  fetchData()
}

// 选择变更
const handleSelectionChange = (selection: ContentAuditApi.ContentAudit[]) => {
  selectedAudits.value = selection
}

// 审核
const handleReview = (row: ContentAuditApi.ContentAudit) => {
  currentAuditId.value = row.id
  reviewForm.auditStatus = 1
  reviewForm.auditResult = ''
  reviewDialogVisible.value = true
}

// 提交审核
const submitReview = () => {
  if (!reviewFormRef.value) return
  reviewFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await manualReview(currentAuditId.value, {
          auditStatus: reviewForm.auditStatus,
          auditResult: reviewForm.auditResult,
          auditorId: 1 // 这里应该从当前登录用户获取
        })
        ElMessage.success('审核成功')
        reviewDialogVisible.value = false
        fetchData()
      } catch (error) {
        ElMessage.error('审核失败')
      }
    }
  })
}

// 批量审核
const handleBatchReview = () => {
  if (selectedAudits.value.length === 0) {
    ElMessage.warning('请先选择要审核的记录')
    return
  }
  
  ElMessageBox.prompt('请输入审核意见', '批量审核', {
    confirmButtonText: '通过',
    cancelButtonText: '不通过',
    inputPlaceholder: '请输入审核意见'
  }).then(async ({ value }) => {
    try {
      const auditIds = selectedAudits.value.map(item => item.id).join(',')
      await batchReview({
        auditIds,
        auditStatus: 1,
        auditResult: value || '批量审核通过',
        auditorId: 1 // 这里应该从当前登录用户获取
      })
      ElMessage.success('批量审核成功')
      fetchData()
    } catch (error) {
      ElMessage.error('批量审核失败')
    }
  }).catch(async (action) => {
    // 如果点击了不通过按钮
    if (action === 'cancel') {
      try {
        const auditIds = selectedAudits.value.map(item => item.id).join(',')
        await batchReview({
          auditIds,
          auditStatus: 2,
          auditResult: '批量审核不通过',
          auditorId: 1 // 这里应该从当前登录用户获取
        })
        ElMessage.success('批量审核成功')
        fetchData()
      } catch (error) {
        ElMessage.error('批量审核失败')
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
.content-audit {
  padding: 20px;
}
</style>