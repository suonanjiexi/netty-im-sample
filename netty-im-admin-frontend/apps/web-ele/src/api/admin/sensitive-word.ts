import { requestClient } from '#/api/request';

export namespace SensitiveWordApi {
  /** 敏感词 */
  export interface SensitiveWord {
    id: number;
    word: string;
    category: number; // 1:政治, 2:色情, 3:暴力, 4:广告, 5:其他
    level: number; // 1:一般, 2:敏感, 3:危险
    action: number; // 1:替换, 2:阻止, 3:标记
    replacement: string; // 替换词
    status: number; // 1:启用, 0:禁用
    creatorId: number;
    createdAt: string;
    updatedAt: string;
  }

  /** 分页查询参数 */
  export interface PageQuery {
    page: number;
    size: number;
    category?: number;
    level?: number;
    status?: number;
    keyword?: string;
  }

  /** 分页结果 */
  export interface PageResult {
    data: SensitiveWord[];
    total: number;
    current: number;
    size: number;
  }

  /** 批量删除参数 */
  export interface BatchDeleteParams {
    ids: string;
  }

  /** 批量操作参数 */
  export interface BatchOperationParams {
    ids: string;
    status?: number;
  }
}

/**
 * 分页查询敏感词列表
 */
export async function pageSensitiveWords(params: SensitiveWordApi.PageQuery) {
  return requestClient.get<SensitiveWordApi.PageResult>('/admin/api/sensitive-words', { params });
}

/**
 * 添加敏感词
 */
export async function addSensitiveWord(data: SensitiveWordApi.SensitiveWord) {
  return requestClient.post('/admin/api/sensitive-words', data);
}

/**
 * 更新敏感词
 */
export async function updateSensitiveWord(id: number, data: SensitiveWordApi.SensitiveWord) {
  return requestClient.put(`/admin/api/sensitive-words/${id}`, data);
}

/**
 * 删除敏感词
 */
export async function deleteSensitiveWord(id: number) {
  return requestClient.delete(`/admin/api/sensitive-words/${id}`);
}

/**
 * 批量删除敏感词
 */
export async function batchDeleteSensitiveWords(params: SensitiveWordApi.BatchDeleteParams) {
  return requestClient.delete('/admin/api/sensitive-words/batch', { params });
}

/**
 * 更新敏感词状态
 */
export async function updateSensitiveWordStatus(id: number, status: number) {
  return requestClient.put(`/admin/api/sensitive-words/${id}/status`, null, { params: { status } });
}

/**
 * 批量更新敏感词状态
 */
export async function batchUpdateSensitiveWordStatus(params: SensitiveWordApi.BatchOperationParams) {
  return requestClient.put('/admin/api/sensitive-words/batch-status', null, { params });
}