import axios from 'axios';

const API_URL = 'http://localhost:8080/api'; // Adjust the base URL as needed

export const submitFeedback = async (feedbackData) => {
    try {
        const response = await axios.post(`${API_URL}/feedback`, feedbackData);
        return response.data;
    } catch (error) {
        throw new Error('Error submitting feedback: ' + error.message);
    }
};

export const getFeedbackForms = async () => {
    try {
        const response = await axios.get(`${API_URL}/feedback/forms`);
        return response.data;
    } catch (error) {
        throw new Error('Error fetching feedback forms: ' + error.message);
    }
};

export const getFeedbackResults = async (courseId) => {
    try {
        const response = await axios.get(`${API_URL}/feedback/results/${courseId}`);
        return response.data;
    } catch (error) {
        throw new Error('Error fetching feedback results: ' + error.message);
    }
};

export const exportFeedbackSummary = async (courseId) => {
    try {
        const response = await axios.get(`${API_URL}/feedback/export/${courseId}`, {
            responseType: 'blob',
        });
        return response.data;
    } catch (error) {
        throw new Error('Error exporting feedback summary: ' + error.message);
    }
};