import apiClient from "@/services/api.js";


export const updateUserDetails = async (userId, userDetails) => {
    try {
        const response = await apiClient.patch(
            `/users/${userId}`,
            userDetails,
            {
                withCredentials: true,
                headers: {
                    "Content-Type": "application/json",
                },
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error updating user details:", error);
        throw error;
    }
};

