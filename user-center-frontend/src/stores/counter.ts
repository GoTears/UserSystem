import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', () => {
  const user = ref(null)
  const isLogin = computed(() => !!user.value)
  function setUser(u: any) {
    user.value = u
  }
  function logout() {
    user.value = null
  }
  return { user, isLogin, setUser, logout }
})
