import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import feedbackService from '../../services/feedbackService';
import './FeedbackForm.css';

const FeedbackForm = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [availableForms, setAvailableForms] = useState([]);
    const [selectedForm, setSelectedForm] = useState(null);
    const [questions, setQuestions] = useState([]);
    const [responses, setResponses] = useState({});
    
    // Get courseId and formId from location if available
    const initialCourseId = location.state?.courseId;
    const initialFormId = location.state?.formId;

    // Fetch all available forms for the user
    useEffect(() => {
        const fetchAvailableForms = async () => {
            try {
                setLoading(true);
                setError(null);
                
                // Get all forms available to the student
                const forms = await feedbackService.getMyFeedbackForms();
                console.log("Available forms for student:", forms);
                
                if (forms && forms.length > 0) {
                    setAvailableForms(forms);
                    
                    // If we have an initial formId, select that form
                    if (initialFormId) {
                        const matchingForm = forms.find(form => form.id === parseInt(initialFormId));
                        if (matchingForm) {
                            selectForm(matchingForm, initialCourseId || matchingForm.assignedCourses[0]?.id);
                        }
                    }
                } else {
                    setError("No feedback forms are available for your courses");
                }
            } catch (err) {
                console.error("Error fetching available forms:", err);
                setError("Failed to load feedback forms. Please try again later.");
            } finally {
                setLoading(false);
            }
        };
        
        fetchAvailableForms();
    }, [initialFormId, initialCourseId]);
    
    // Function to select a form and prepare its questions
    const selectForm = (form, courseId) => {
        console.log("Selected form:", form, "for course:", courseId);
        
        // Set the selected form
        setSelectedForm({
            ...form,
            courseId: courseId || form.assignedCourses[0]?.id
        });
        
        // Process questions
        const processedQuestions = form.questions?.map(q => ({
            ...q,
            // Ensure both property variants exist for compatibility
            isRatingQuestion: q.ratingQuestion === true || q.isRatingQuestion === true,
            ratingQuestion: q.ratingQuestion === true || q.isRatingQuestion === true
        })) || [];
        
        setQuestions(processedQuestions);
        
        // Initialize responses
        const initialResponses = {};
        processedQuestions.forEach(q => {
            initialResponses[q.id] = q.isRatingQuestion || q.ratingQuestion ? 3 : '';
        });
        
        setResponses(initialResponses);
    };
    
    // Handle response changes
    const handleResponseChange = (questionId, value) => {
        setResponses(prev => ({
            ...prev,
            [questionId]: value
        }));
    };
    
    // Handle form submission
const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!selectedForm) {
        setError("Please select a feedback form first");
        return;
    }
    
    try {
        setLoading(true);
        // Modified to properly handle rating vs text responses
        const responsesArray = Object.keys(responses).map(questionId => {
            const questionId_num = parseInt(questionId);
            // Find the corresponding question to check if it's a rating question
            const question = questions.find(q => q.id === questionId_num);
            const isRating = question?.isRatingQuestion || question?.ratingQuestion;
            
            if (isRating) {
                return {
                    questionId: questionId_num,
                    rating: parseInt(responses[questionId]), // Send as rating field
                    textAnswer: "" // Empty string for text field
                };
            } else {
                return {
                    questionId: questionId_num,
                    rating: 0, // Zero for non-rating questions
                    textAnswer: responses[questionId].toString()
                };
            }
        });
        
        console.log("Submitting feedback:", selectedForm.courseId, selectedForm.id, responsesArray);
        
        await feedbackService.submitFeedback(selectedForm.courseId, selectedForm.id, responsesArray);
        navigate('/thank-you');
    } catch (err) {
        console.error("Error submitting feedback:", err);
        setError("Failed to submit your feedback. Please try again.");
        setLoading(false);
    }
};
    
    // Star rating component
    const StarRating = ({ questionId, value, onChange }) => {
        return (
            <div className="star-rating">
                {[1, 2, 3, 4, 5].map(star => (
                    <span 
                        key={star}
                        className={`star ${star <= value ? 'selected' : ''}`}
                        onClick={() => onChange(questionId, star)}
                    >
                        ★
                    </span>
                ))}
                <span className="rating-value">({value}/5)</span>
            </div>
        );
    };

    if (loading && availableForms.length === 0) {
        return <div className="loading">Loading feedback forms...</div>;
    }
    
    if (error && availableForms.length === 0) {
        return (
            <div className="error-container">
                <div className="error-message">{error}</div>
                <button 
                    className="btn btn-primary" 
                    onClick={() => navigate('/student-dashboard')}
                >
                    Return to Dashboard
                </button>
            </div>
        );
    }
    
    // Render the form list if no form is selected
    if (!selectedForm) {
        return (
            <div className="feedback-list-container">
                <h2>Available Feedback Forms</h2>
                
                {availableForms.length === 0 ? (
                    <div className="no-feedback-message">
                        <p>You don't have any courses with open feedback forms at this time.</p>
                        <button 
                            className="btn btn-primary"
                            onClick={() => navigate('/student-dashboard')}
                        >
                            Return to Dashboard
                        </button>
                    </div>
                ) : (
                    <div className="feedback-forms-list">
                        {availableForms.map(form => (
                            <div key={form.id} className="feedback-form-item">
                                <div className="form-info">
                                    <h3>{form.title || 'Untitled Form'}</h3>
                                    <p>{form.description || 'No description provided'}</p>
                                    {form.assignedCourses && form.assignedCourses.length > 0 && (
                                        <p className="course-name">
                                            <strong>Course:</strong> {form.assignedCourses[0].name || "Not specified"}
                                        </p>
                                    )}
                                    <p className="created-date">
                                        <small>Created: {new Date(form.createdAt).toLocaleDateString()}</small>
                                    </p>
                                </div>
                                <button 
                                    className="btn btn-primary"
                                    onClick={() => selectForm(form, form.assignedCourses[0]?.id)}
                                >
                                    Complete Feedback
                                </button>
                            </div>
                        ))}
                    </div>
                )}
            </div>
        );
    }
    
    // Render the selected form with questions
    return (
        <div className="feedback-form-container">
            <h2>{selectedForm.title || 'Course Feedback'}</h2>
            {selectedForm.description && <p className="form-description">{selectedForm.description}</p>}
            
            {error && <div className="error-message">{error}</div>}
            
            <form onSubmit={handleSubmit}>
                {questions.map((question) => (
                    <div key={question.id} className="form-group">
                        <label className="question-text">
                            {question.questionText || question.text}
                            {question.target && (
                                <span className="question-target">
                                    ({question.target.toLowerCase().replace('_', ' ')})
                                </span>
                            )}
                        </label>
                        
                        {question.ratingQuestion || question.isRatingQuestion ? (
                            <StarRating 
                                questionId={question.id}
                                value={responses[question.id] || 3}
                                onChange={handleResponseChange}
                            />
                        ) : (
                            <textarea
                                value={responses[question.id] || ''}
                                onChange={(e) => handleResponseChange(question.id, e.target.value)}
                                placeholder="Type your response here..."
                                rows={3}
                                required
                            />
                        )}
                    </div>
                ))}
                
                <div className="form-actions">
                    <button 
                        type="button" 
                        className="btn btn-secondary"
                        onClick={() => setSelectedForm(null)}
                    >
                        Back to Forms
                    </button>
                    <button 
                        type="submit" 
                        className="btn btn-primary"
                        disabled={loading}
                    >
                        {loading ? 'Submitting...' : 'Submit Feedback'}
                    </button>
                </div>
            </form>
        </div>
    );
};

export default FeedbackForm;