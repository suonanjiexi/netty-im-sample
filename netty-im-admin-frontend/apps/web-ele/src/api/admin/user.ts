import { requestClient } from '#/api/request';

export namespace AdminUserApi {
  /** 管理员用户 */
  export interface AdminUser {
    id: number;
    username: string;
    nickname: string;
    avatar: string;
    email: string;
    phone: string;
    role: string;
    status: number;
    lastLoginTime: string;
    lastLoginIp: string;
    createdAt: string;
    updatedAt: string;
  }

  /** 管理员用户DTO */
  export interface AdminUserDTO {
    id?: number;
    username: string;
    password?: string;
    nickname: string;
    avatar?: string;
    email?: string;
    phone?: string;
    role: string;
    status: number;
  }

  /** 分页查询参数 */
  export interface PageQuery {
    page: number;
    size: number;
    keyword?: string;
  }

  /** 分页结果 */
  export interface PageResult {
    data: AdminUser[];
    total: number;
    current: number;
    size: number;
  }
}

/**
 * 分页查询管理员列表
 */
export async function pageAdminUsers(params: AdminUserApi.PageQuery) {
  return requestClient.get<AdminUserApi.PageResult>('/admin/api/users', { params });
}

/**
 * 创建管理员
 */
export async function createAdminUser(data: AdminUserApi.AdminUserDTO) {
  return requestClient.post('/admin/api/users', data);
}

/**
 * 更新管理员
 */
export async function updateAdminUser(id: number, data: AdminUserApi.AdminUserDTO) {
  return requestClient.put(`/admin/api/users/${id}`, data);
}

/**
 * 删除管理员
 */
export async function deleteAdminUser(id: number) {
  return requestClient.delete(`/admin/api/users/${id}`);
}

/**
 * 更新管理员状态
 */
export async function updateAdminUserStatus(id: number, status: number) {
  return requestClient.put(`/admin/api/users/${id}/status`, null, { params: { status } });
}