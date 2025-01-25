import apiClient from "@/services/api.js";

// Secured requests
export const fetchMyRequest = async (id) =>
    await apiClient.get(`/my-requests/${id}`, {
        withCredentials: true,
    });

export const fetchMyRequests = async () =>
    await apiClient.get("/my-requests", {
        withCredentials: true,
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

export const fetchRequests = async (page = 0, pageSize = 6) =>
    await apiClient.get(`/requests`, {
        params: { page, pageSize },
    });

export const fetchRequestsSearch = async (text, page = 0, pageSize = 6) =>
    await apiClient.get(`/requests/search`, {
        params: { page, pageSize, text },
    });
