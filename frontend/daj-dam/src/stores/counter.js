import apiClient from "@/services/api.js";
import { defineStore } from 'pinia'

const baseUserURL = '/users';
const basePostURL = '/posts';
const pageSize = 2;

export const useUserStore = defineStore('user', {
  state: () => ({
    userId: 1,
    profile: null,
    posts: [],
    authorized: false,
  }),
  actions: {
    async getProfile() {
      this.profile = (await apiClient.get(baseUserURL + '/' + this.userId)).data;
    },
    async getPosts(postType, userId, page, size) {
      const params = {};

      if (postType !== undefined && postType !== null) {
        params.type = postType;
      }

      if (page !== undefined && page !== null) {
        params.page = page;
      }
      if (size !== undefined && size !== null) {
        params.size = size;
      }else {
        params.size = pageSize;
      }

      if (userId !== undefined && userId !== null) {
        const config = {
          params: params,
          headers: {
            'Content-Type': 'application/json',
            'X-Likesgiving-Id': userId,
          }
        }
        this.posts = (await apiClient.get(basePostURL, config)).data;
      } else {
        this.posts = (await apiClient.get(basePostURL, {params})).data;
      }


    }
  }
})