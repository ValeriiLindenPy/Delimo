import apiClient from "@/services/api.js";

//secured
export const fetchMyItem = async (id) => await apiClient.get(`/my-items/${id}`,{
    withCredentials: true,
});

export const fetchMyItems = async (page = 0, size = 6) => await apiClient.get("/my-items",{
    withCredentials: true,
    params: {
        page, size,
    }
});

export const updateItem = async (id, formData) =>
    await apiClient.patch(`/my-items/${id}`, formData, {
        withCredentials: true,
        headers: { "Content-Type": "multipart/form-data" },
    });

export const deleteItemById = async (id) =>
    await apiClient.delete(`/my-items/${id}`, {
        withCredentials: true,
    });

export const createItem = async (formData) =>
    await apiClient.post(`/my-items`, formData, {
        withCredentials: true,
        headers: { "Content-Type": "multipart/form-data" },
    });

//public
export const getItem = async (id) => await apiClient.get(`/items/${id}`);
export const getItems = async (page = 0, pageSize = 6, city = "") => {
    const params = { page, pageSize };
    if (city && city.trim() !== "") {
        params.city = city;
    }
    return await apiClient.get("/items", { params });
};

export const getItemsSearch = async (text, page = 0, pageSize = 6) => await apiClient.get("/items/search",
    {
        params: { page, pageSize, text },
    });

export const getItemsTitleSearch = async (q, limit = 3) => await apiClient.get("/items/titles",
    {
        params: { q, limit },
    });