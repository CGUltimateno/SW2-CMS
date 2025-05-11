import axios from 'axios';

const API_URL = 'http://localhost:8080/feedback';

export const submitFeedback = async (feedbackData) => {
    try {
        const response = await axios.post(`${API_URL}/submit`, feedbackData);
        return response.data;
    } catch (error) {
        throw new Error('Error submitting feedback: ' + error.message);
    }
};

export const getFeedbackResults = async (courseId) => {
    try {
        const response = await axios.get(`${API_URL}/results/${courseId}`);
        return response.data;
    } catch (error) {
        throw new Error('Error fetching feedback results: ' + error.message);
    }
};

export const exportFeedbackSummary = async (courseId) => {
    try {
        const response = await axios.get(`${API_URL}/export/${courseId}`, {
            responseType: 'blob',
        });
        return response.data;
    } catch (error) {
        throw new Error('Error exporting feedback summary: ' + error.message);
    }
};

// Add the missing getFeedbackForms function
export const getFeedbackForms = async () => {
    try {
        const response = await axios.get(`${API_URL}/forms`);
        return response;
    } catch (error) {
        throw new Error('Error fetching feedback forms: ' + error.message);
    }
};

// Create a default export with all the functions
const feedbackService = {
    submitFeedback,
    getFeedbackResults,
    exportFeedbackSummary,
    getFeedbackForms
};

export default feedbackService;