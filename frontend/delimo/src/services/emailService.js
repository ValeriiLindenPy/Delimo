import apiClient from "@/services/api.js";

export const sendFeedback = async (formData) =>
    await apiClient.post("/emails/feedback", formData, {
        headers: { "Content-Type": "application/json" },
    });