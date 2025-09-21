import type { RouteRecordRaw } from 'vue-router';

import { $t } from '#/locales';

const routes: RouteRecordRaw[] = [
  {
    meta: {
      icon: 'mdi:account-cog',
      order: 1,
      title: $t('page.admin.title'),
    },
    name: 'AdminManagement',
    path: '/admin-management',
    children: [
      {
        name: 'AdminList',
        path: '/admin-management/admins',
        component: () => import('#/views/admin-management/index.vue'),
        meta: {
          icon: 'mdi:account-multiple',
          title: $t('page.admin.list'),
        },
      },
      {
        name: 'UserManagement',
        path: '/admin-management/users',
        component: () => import('#/views/user-management/index.vue'),
        meta: {
          icon: 'mdi:account-group',
          title: $t('page.admin.users'),
        },
      },
      {
        name: 'ContentAudit',
        path: '/admin-management/content-audit',
        component: () => import('#/views/content-audit/index.vue'),
        meta: {
          icon: 'mdi:file-document-edit',
          title: $t('page.admin.contentAudit'),
        },
      },
      {
        name: 'SensitiveWords',
        path: '/admin-management/sensitive-words',
        component: () => import('#/views/sensitive-words/index.vue'),
        meta: {
          icon: 'mdi:alert-octagon',
          title: $t('page.admin.sensitiveWords'),
        },
      },
      {
        name: 'MembershipManagement',
        path: '/admin-management/memberships',
        component: () => import('#/views/membership-management/index.vue'),
        meta: {
          icon: 'mdi:star',
          title: $t('page.admin.memberships'),
        },
      },
      {
        name: 'PaymentManagement',
        path: '/admin-management/payments',
        component: () => import('#/views/payment-management/index.vue'),
        meta: {
          icon: 'mdi:currency-usd',
          title: $t('page.admin.payments'),
        },
      },
      {
        name: 'DataStatistics',
        path: '/admin-management/statistics',
        component: () => import('#/views/data-statistics/index.vue'),
        meta: {
          icon: 'mdi:chart-bar',
          title: $t('page.admin.statistics'),
        },
      },
      {
        name: 'SystemLogs',
        path: '/admin-management/logs',
        component: () => import('#/views/system-logs/index.vue'),
        meta: {
          icon: 'mdi:format-list-bulleted',
          title: $t('page.admin.logs'),
        },
      },
    ],
  },
];

export default routes;