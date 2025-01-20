import apiClient from "@/services/api.js";

//secured
export const fetchMyItem = async (id) => await apiClient.get(`/my-items/${id}`,{
    withCredentials: true,
});

export const fetchMyItems = async () => await apiClient.get("/my-items",{
    withCredentials: true,
});

export const updateItem = (id, formData) =>
    apiClient.patch(`/my-items/${id}`, formData, {
        withCredentials: true,
        headers: { "Content-Type": "multipart/form-data" },
    });

export const deleteItemById = (id) =>
    apiClient.delete(`/my-items/${id}`, {
        withCredentials: true,
    });

export const createItem = (formData) =>
    apiClient.patch(`/my-items`, formData, {
        withCredentials: true,
        headers: { "Content-Type": "multipart/form-data" },
    });

//public
export const getItem = async (id) => await apiClient.get(`/items/${id}`);
export const getItems = async (page = 0, pageSize = 6) => await apiClient.get("/items",
    {
        params: { page, pageSize },
    });