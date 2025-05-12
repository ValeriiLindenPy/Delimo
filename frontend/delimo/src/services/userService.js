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

export const deleteUser = async (userId) => {
    try {
        return await apiClient.delete(
            `/users/${userId}`,
            {
                withCredentials: true,
                headers: {
                    "Content-Type": "application/json",
                },
            }
        );
    } catch (error) {
        console.error("Error updating user details:", error);
        throw error;
    }
};

export const authenticateUser = async (credentials) => {
    try {
        return  await apiClient.post("/users/auth/authenticate", credentials);
    } catch (error) {
        console.error("auth error:", error);
        throw error;
    }
};

export const registerUser = async (userData) => {
    try {
        return await apiClient.post("/users/auth/register", userData, {
            withCredentials: true,
            headers: {
                "Content-Type": "application/json",
            },
        });
    } catch (error) {
        console.error("Error with registration:", error);
        throw error;
    }
};

export const forgotPasswordUser = async (email) => {
    try {
        return await apiClient.post("/users/auth/forgot-password", {
            email: email,
        });
    }catch (error) {
        throw error;
    }
};

export const passwordResetUser = async (token , password) => {
    try {
        return await apiClient.post("/users/auth/reset-password", {
            token: token,
            newPassword: password,
        });
    }catch (error) {
        throw error;
    }
};

export const verifyUser = async (token) => {
    try {
        return await apiClient.get("/users/auth/verify", {params: {token}});
    }catch (error) {
        throw error;
    }
};

export const fetchUserData = async () => {
    try {
        return  await apiClient.get("/users/data");
    } catch (error) {
        console.error("error fetching data:", error);
    }
};



