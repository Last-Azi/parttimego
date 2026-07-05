import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../stores/user'

const routes = [
  { path: '/login', name: 'Login', component: () => import('../views/auth/Login.vue'), meta: { public: true } },
  { path: '/register', name: 'Register', component: () => import('../views/auth/Register.vue'), meta: { public: true } },

  {
    path: '/',
    component: () => import('../views/layout/MainLayout.vue'),
    children: [
      { path: '', name: 'Home', component: () => import('../views/portal/HomePageNew.vue'), meta: { public: true } },
      { path: 'search', name: 'Search', component: () => import('../views/student/JobList.vue'), meta: { public: true } },
      { path: 'job/:id', name: 'JobDetail', component: () => import('../views/student/JobDetail.vue'), meta: { public: true } },

      { path: 'student/resume', name: 'MyResume', component: () => import('../views/student/MyResume.vue'), meta: { role: 'STUDENT' } },
      { path: 'student/applications', name: 'MyApplications', component: () => import('../views/student/MyApplications.vue'), meta: { role: 'STUDENT' } },
      { path: 'student/favorites', name: 'MyFavorites', component: () => import('../views/student/MyFavorites.vue'), meta: { role: 'STUDENT' } },
      { path: 'student/interviews', name: 'MyInterviews', component: () => import('../views/student/MyInterviews.vue'), meta: { role: 'STUDENT' } },
      { path: 'messages', name: 'Messages', component: () => import('../views/user/MessageCenter.vue'), meta: { role: 'STUDENT' } },
      { path: 'chat', name: 'Chat', component: () => import('../views/chat/ChatRoom.vue') },
      { path: 'employer/messages', name: 'EmployerMessages', component: () => import('../views/user/MessageCenter.vue'), meta: { role: 'EMPLOYER' } },

      { path: 'employer/jobs', name: 'EmployerJobs', component: () => import('../views/employer/MyJobs.vue'), meta: { role: 'EMPLOYER' } },
      { path: 'employer/job/publish', name: 'PublishJob', component: () => import('../views/employer/PublishJob.vue'), meta: { role: 'EMPLOYER' } },
      { path: 'employer/job/:id/applications', name: 'JobApplications', component: () => import('../views/employer/JobApplications.vue'), meta: { role: 'EMPLOYER' } },
      { path: 'employer/interviews', name: 'InterviewManage', component: () => import('../views/employer/InterviewManage.vue'), meta: { role: 'EMPLOYER' } },
      { path: 'employer/dashboard', name: 'EmployerDashboard', component: () => import('../views/employer/EmployerDashboard.vue'), meta: { role: 'EMPLOYER' } },

      { path: 'admin/dashboard', name: 'Dashboard', component: () => import('../views/admin/Dashboard.vue'), meta: { role: 'ADMIN' } },
      { path: 'admin/jobs', name: 'AdminJobs', component: () => import('../views/admin/JobAudit.vue'), meta: { role: 'ADMIN' } },
      { path: 'admin/users', name: 'AdminUsers', component: () => import('../views/admin/UserManage.vue'), meta: { role: 'ADMIN' } },
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  if (to.meta.public) return next()
  const store = useUserStore()
  if (!store.isLoggedIn) return next('/login')
  if (to.meta.role && store.role !== to.meta.role) return next('/')
  next()
})

export default router
