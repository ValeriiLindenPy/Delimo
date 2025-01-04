import axios from 'axios';

const apiClient = axios.create({
    baseURL: 'http://localhost:8080', // Backend base URL
    withCredentials: true, // Include cookies for session
});

export default apiClient;
