import apiClient from "@/services/api.js";

// Secured requests
export const fetchMyRequest = async (id) =>
    await apiClient.get(`/my-requests/${id}`, {
        withCredentials: true,
    });

export const fetchMyRequests = async (page = 0, pageSize = 6) =>
    await apiClient.get("/my-requests", {
        withCredentials: true,
        params: {page, pageSize}
    });

export const createRequest = async (formData) =>
    await apiClient.post("/my-requests", formData, {
        withCredentials: true,
        headers: { "Content-Type": "application/json" },
    });

export const updateRequest = (id, formData) =>
    apiClient.patch(`/my-requests/${id}`, formData, {
        withCredentials: true,
        headers: { "Content-Type": "application/json" },
    });

export const deleteRequestById = (id) =>
    apiClient.delete(`/my-requests/${id}`, {
        withCredentials: true,
    });

// Public requests
export const fetchRequest = async (id) =>
    await apiClient.get(`/requests/${id}`);

export const fetchRequests = async (page = 0, pageSize = 6, city = "") => {
    const params = { page, pageSize };
    if (city && city.trim() !== "") {
        params.city = city;
    }
    return await apiClient.get("/requests", { params });
};

export const fetchRequestsSearch = async (text, page = 0, pageSize = 6, city = "") => {
    const params = { page, pageSize, text };
    if (city && city.trim() !== "") {
        params.city = city;
    }
    return await apiClient.get("/requests/search", { params });
};
