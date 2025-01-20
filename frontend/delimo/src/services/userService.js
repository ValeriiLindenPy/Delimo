import apiClient from "@/services/api.js";
import {useUserStore} from "@/stores/counter.js";


export const getUserData = async () => {
    const store = useUserStore();
    try {
        const response = await apiClient.get("/users/user-data", {
            withCredentials: true,
        });
        store.authorized = true;
        store.setUserInfo(response.data);
    } catch (error) {
        store.authorized = false;
        console.error("Error fetching user data:", error);
    }
};

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