import { requestClient } from '#/api/request';

export namespace ContentAuditApi {
  /** 内容审核记录 */
  export interface ContentAudit {
    id: number;
    contentType: number;
    contentId: number;
    userId: number;
    content: string;
    auditStatus: number;
    auditResult: string;
    auditorId: number;
    auditType: number;
    sensitiveWords: string;
    auditScore: number;
    needManualReview: number;
    auditTime: string;
    createdAt: string;
    updatedAt: string;
  }

  /** 分页查询参数 */
  export interface PageQuery {
    page: number;
    size: number;
    contentType?: number;
    auditStatus?: number;
    keyword?: string;
  }

  /** 分页结果 */
  export interface PageResult {
    data: ContentAudit[];
    total: number;
    current: number;
    size: number;
  }

  /** 人工审核参数 */
  export interface ManualReviewParams {
    auditStatus: number;
    auditResult: string;
    auditorId: number;
  }

  /** 批量审核参数 */
  export interface BatchReviewParams {
    auditIds: string;
    auditStatus: number;
    auditResult: string;
    auditorId: number;
  }
}

/**
 * 分页查询审核记录列表
 */
export async function pageAudits(params: ContentAuditApi.PageQuery) {
  return requestClient.get<ContentAuditApi.PageResult>('/admin/api/content-audits', { params });
}

/**
 * 获取审核记录详情
 */
export async function getAuditById(id: number) {
  return requestClient.get<ContentAuditApi.ContentAudit>(`/admin/api/content-audits/${id}`);
}

/**
 * 人工审核内容
 */
export async function manualReview(id: number, data: ContentAuditApi.ManualReviewParams) {
  return requestClient.post(`/admin/api/content-audits/${id}/review`, data);
}

/**
 * 批量审核内容
 */
export async function batchReview(data: ContentAuditApi.BatchReviewParams) {
  return requestClient.post('/admin/api/content-audits/batch-review', data);
}

/**
 * 获取审核统计数据
 */
export async function getAuditStatistics(params: { startTime?: string; endTime?: string }) {
  return requestClient.get<any>('/admin/api/content-audits/statistics', { params });
}