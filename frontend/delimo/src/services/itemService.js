import apiClient from "@/services/api.js";

export const fetchItem = (id) => apiClient.get(`/my-items/${id}`);
export const updateItem = (id, data) =>
    apiClient.patch(`/my-items/${id}`, data, {
        withCredentials: true,
        headers: { "Content-Type": "application/json" },
    });
