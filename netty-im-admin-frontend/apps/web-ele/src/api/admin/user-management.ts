import { requestClient } from '#/api/request';

export namespace UserManagementApi {
  /** 用户 */
  export interface User {
    id: number;
    username: string;
    email: string;
    phone: string;
    nickname: string;
    avatar: string;
    status: number;
    onlineStatus: number;
    lastLoginTime: string;
    idCardNumber: string;
    realName: string;
    identityStatus: number;
    createdAt: string;
    updatedAt: string;
  }

  /** 分页查询参数 */
  export interface PageQuery {
    page: number;
    size: number;
    keyword?: string;
    status?: number;
  }

  /** 分页结果 */
  export interface PageResult {
    data: User[];
    total: number;
    current: number;
    size: number;
  }
}

/**
 * 分页查询用户列表
 */
export async function pageUsers(params: UserManagementApi.PageQuery) {
  return requestClient.get<UserManagementApi.PageResult>('/admin/api/users', { params });
}

/**
 * 获取用户详情
 */
export async function getUserById(id: number) {
  return requestClient.get<UserManagementApi.User>(`/admin/api/users/${id}`);
}

/**
 * 更新用户状态
 */
export async function updateUserStatus(id: number, status: number) {
  return requestClient.put(`/admin/api/users/${id}/status`, null, { params: { status } });
}

/**
 * 删除用户
 */
export async function deleteUser(id: number) {
  return requestClient.delete(`/admin/api/users/${id}`);
}