import apiClient from "@/services/api.js";

export const fetchItem = (id) => apiClient.get(`/my-items/${id}`);

export const updateItem = (id, formData) =>
    apiClient.patch(`/my-items/${id}`, formData, {
        withCredentials: true,
        headers: { "Content-Type": "multipart/form-data" },
    });
