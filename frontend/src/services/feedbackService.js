import axios from 'axios';

const authHeader = () => {
    const user = JSON.parse(localStorage.getItem('user'));
    console.log("authHeader user:", user);
    if (user && user.token) { 
        return { Authorization: `Bearer ${user.token}` };
    } else {
        return {};
    }
};


const API_URL = 'http://localhost:8080/feedback-forms';

export const getAllFeedbackForms = async () => {
    try {
        const response = await axios.get(API_URL, { headers: authHeader() });
        return response.data;
    } catch (error) {
        console.error("Error in getAllFeedbackForms:", error.response?.status, error.response?.data);
        throw error;
    }
};

export const createFeedbackForm = async (formData) => {
    try {
        const apiPayload = {
            title: formData.title,
            description: formData.description,
            questions: formData.questions.map(q => ({
                questionText: q.questionText,
                isRatingQuestion: q.isRatingQuestion === true, 
                target: q.target
            }))
        };
        
        console.log("API payload:", apiPayload);
        
        const response = await axios.post(
            API_URL, 
            apiPayload,
            { headers: authHeader() }
        );
        
        return response.data;
    } catch (error) {
        console.error("Error creating form:", error);
        throw new Error(error.response?.data?.message || 'Failed to create feedback form');
    }
};


export const assignFormToCourse = async (formId, courseId) => {
    try {
        const response = await axios.post(
            `${API_URL}/${formId}/assign/${courseId}`, 
            {}, 
            { headers: authHeader() }
        );
        return response.data;
    } catch (error) {
        throw new Error('Error assigning form to course: ' + error.message);
    }
};

export const getMyFeedbackForms = async () => {
    try {
        const response = await axios.get(`${API_URL}/my-forms`, { headers: authHeader() });
        return response.data;
    } catch (error) {
        console.error("Error fetching your feedback forms:", error);
        throw new Error('Error fetching your feedback forms: ' + error.message);
    }
};


export const submitFeedback = async (courseId, formId, answers) => {
  if (!courseId || courseId === 'undefined') {
    throw new Error('Course ID is required');
  }
  
  if (!formId || formId === 'undefined') {
    throw new Error('Form ID is required');
  }
  
  if (!answers || !Array.isArray(answers) || answers.length === 0) {
    throw new Error('Answers are required');
  }
  
  try {
    console.log(`Submitting feedback for course ${courseId}, form ${formId}`);
    const response = await axios.post(
      `http://localhost:8080/feedback/submit/${courseId}/${formId}`, 
      answers, 
      { headers: authHeader() }
    );
    console.log("Feedback submission response:", response.data);
    return response.data;
  } catch (error) {
    console.error("Error submitting feedback:", error);
    throw new Error(error.response?.data?.message || 'Failed to submit feedback');
  }
};


export const getFeedbackResults = async (courseId) => {
    try {
        const response = await axios.get(
            `http://localhost:8080/feedback/results/${courseId}`, 
            { headers: authHeader() }
        );
        return response.data;
    } catch (error) {
        console.error("Error fetching feedback results:", error);
        throw new Error(error.response?.data?.message || 'Failed to fetch feedback results');
    }
};


export const exportFeedbackSummary = async (courseId) => {
    try {
        const response = await axios.get(
            `http://localhost:8080/feedback/results/export/${courseId}`, 
            {
                responseType: 'blob',
                headers: authHeader()
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error exporting feedback:", error);
        throw new Error('Failed to export feedback report');
    }
};

// Create a default export with all the functions
const feedbackService = {
    getAllFeedbackForms,
    createFeedbackForm,
    assignFormToCourse,
    getMyFeedbackForms,
    submitFeedback,
    getFeedbackResults,
    exportFeedbackSummary,
};

export default feedbackService;