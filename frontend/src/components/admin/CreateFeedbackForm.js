import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { createFeedbackForm } from "../../services/feedbackService"; // Import the service
import "./CreateFeedbackForm.css";

const CreateFeedbackForm = () => {
    // Form state
    const [formData, setFormData] = useState({
        title: "",
        description: "",
        questions: [
            {
                id: 1,
                questionText: "How would you rate this course?",
                isRatingQuestion: true,
                target: "COURSE"
            }
        ]
    });
    
    // UI state
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");
    const [formSubmitted, setFormSubmitted] = useState(false);
    const navigate = useNavigate();

    // Helper function to update form data
    const updateFormData = (field, value) => {
        setFormData(prev => ({
            ...prev,
            [field]: value
        }));
    };

    // Helper function to update a specific question
    const updateQuestion = (index, field, value) => {
        const updatedQuestions = [...formData.questions];
        updatedQuestions[index] = {
            ...updatedQuestions[index],
            [field]: value
        };
        
        console.log(`Updated question ${index}, field: ${field}, new value:`, value);
        console.log("New question object:", updatedQuestions[index]);
        
        setFormData(prev => ({
            ...prev,
            questions: updatedQuestions
        }));
    };

    // Add a new question
 const addQuestion = () => {
    const newId = formData.questions.length + 1;
    const isEven = newId % 2 === 0;
    
    // Make sure isRatingQuestion is explicitly a boolean value
    const newQuestion = {
        id: newId,
        questionText: isEven 
            ? "How would you rate the instructor's teaching style?" 
            : "What aspects of the course could be improved?",
        isRatingQuestion: isEven === true, // Explicit boolean conversion
        target: isEven ? "INSTRUCTOR" : "COURSE"
    };
    
    console.log("Adding new question with isRatingQuestion =", newQuestion.isRatingQuestion);
    
    setFormData(prev => ({
        ...prev,
        questions: [...prev.questions, newQuestion]
    }));
};

    // Remove a question
    const removeQuestion = (index) => {
        if (formData.questions.length <= 1) return;
        
        const updatedQuestions = formData.questions.filter((_, i) => i !== index);
        
        setFormData(prev => ({
            ...prev,
            questions: updatedQuestions
        }));
    };

    // Validate the form data
    const validateFormData = () => {
        // Title validation
        if (!formData.title.trim()) {
            setError("Form title is required");
            return false;
        }
        
        // Questions validation
        for (let i = 0; i < formData.questions.length; i++) {
            const question = formData.questions[i];
            if (!question.questionText || !question.questionText.trim()) {
                setError(`Question ${i + 1} text is required`);
                return false;
            }
        }
        
        return true;
    };

    // Handle form submission
const handleSubmit = async (e) => {
    e.preventDefault();
    
    // Reset previous errors
    setError("");
    
    // Validate the form
    if (!validateFormData()) {
        return;
    }
    
    try {
        setLoading(true);
        setFormSubmitted(true);
        
const payload = {
    title: formData.title,
    description: formData.description,
    questions: formData.questions.map((q, index) => {
        // Use ratingQuestion instead of isRatingQuestion to match the backend DTO field name
        return {
            questionText: q.questionText || `Question ${index + 1}`,
            ratingQuestion: q.isRatingQuestion === true, // Send as ratingQuestion
            target: q.target || "COURSE"
        };
    })
};

console.log("Submitting form with explicitly typed data:", JSON.stringify(payload, null, 2));        
        // Use the service
        const result = await createFeedbackForm(payload);
        
        console.log("Form submission successful:", result);
        
        // Navigate to dashboard on success
        navigate("/admin-dashboard");
    } catch (err) {
        console.error("Error submitting form:", err);
        setError(err.message || "Failed to create feedback form");
    } finally {
        setLoading(false);
    }
};

    // Data integrity check effect
    useEffect(() => {
        if (formSubmitted) {
            console.log("Form data after submit attempt:", formData);
        }
    }, [formSubmitted, formData]);

    return (
        <div className="create-feedback-form">
            <h2>Create Feedback Form</h2>
            
            {error && <div className="error-message">{error}</div>}
            
            <form onSubmit={handleSubmit}>
                {/* Form Title */}
                <div className="form-group">
                    <label htmlFor="formTitle">Form Title *</label>
                    <input
                        type="text"
                        id="formTitle"
                        value={formData.title}
                        onChange={(e) => updateFormData("title", e.target.value)}
                        placeholder="Enter form title"
                        required
                    />
                </div>
                
                {/* Form Description */}
                <div className="form-group">
                    <label htmlFor="formDescription">Description</label>
                    <textarea
                        id="formDescription"
                        value={formData.description}
                        onChange={(e) => updateFormData("description", e.target.value)}
                        placeholder="Enter form description (optional)"
                        rows={3}
                    />
                </div>
                
                {/* Questions Section */}
                <div className="questions-section">
                    <h3>Questions</h3>
                    
                    {formData.questions.map((question, index) => (
                        <div key={question.id} className="question-card">
                            <div className="question-header">
                                <h4>Question {index + 1}</h4>
                                <button 
                                    type="button" 
                                    className="remove-btn"
                                    onClick={() => removeQuestion(index)}
                                    disabled={formData.questions.length <= 1}
                                >
                                    Remove
                                </button>
                            </div>
                            
                            {/* Question Text Input */}
                            <div className="form-group">
                                <label htmlFor={`question-${question.id}-text`}>
                                    Question Text *
                                </label>
                                <input
                                    type="text"
                                    id={`question-${question.id}-text`}
                                    value={question.questionText}
                                    onChange={(e) => {
                                        const value = e.target.value;
                                        console.log(`Question ${index + 1} text changed to: "${value}"`);
                                        updateQuestion(index, "questionText", value);
                                    }}
                                    onBlur={(e) => {
                                        if (!e.target.value.trim()) {
                                            const defaultText = `Question ${index + 1}`;
                                            console.log(`Setting default text: "${defaultText}"`);
                                            updateQuestion(index, "questionText", defaultText);
                                        }
                                    }}
                                    placeholder="Enter your question"
                                    required
                                />
                            </div>
                            
                            {/* Question Type Selection */}
<div className="form-group">
    <label>Question Type</label>
    <div className="radio-group">
        <label className="radio-label">
            <input
                type="radio"
                name={`questionType-${index}`} // Use index instead of question.id
                checked={question.isRatingQuestion === true}
                onChange={() => {
                    console.log(`Setting question ${index + 1} to Rating type`);
                    updateQuestion(index, "isRatingQuestion", true);
                }}
            />
            Rating (1-5 stars)
        </label>
        <label className="radio-label">
            <input
                type="radio"
                name={`questionType-${index}`} // Use index instead of question.id
                checked={question.isRatingQuestion === false} 
                onChange={() => {
                    console.log(`Setting question ${index + 1} to Text Response type`);
                    updateQuestion(index, "isRatingQuestion", false);
                }}
            />
            Text Response
        </label>
    </div>
</div>                       
                            {/* Question Target Selection */}
                            <div className="form-group">
                                <label>Question Target</label>
                                <select
                                    value={question.target}
                                    onChange={(e) => updateQuestion(index, "target", e.target.value)}
                                >
                                    <option value="COURSE">Course Content</option>
                                    <option value="INSTRUCTOR">Instructor</option>
                                    <option value="MATERIALS">Course Materials</option>
                                    <option value="GENERAL">General</option>
                                </select>
                            </div>
                        </div>
                    ))}
                    
                    {/* Add Question Button */}
                    <button 
                        type="button" 
                        className="add-question-btn"
                        onClick={addQuestion}
                    >
                        Add Another Question
                    </button>
                </div>
                
                {/* Form Actions */}
                <div className="form-actions">
                    <button 
                        type="button" 
                        className="cancel-btn"
                        onClick={() => navigate("/admin-dashboard")}
                    >
                        Cancel
                    </button>
                    <button 
                        type="submit" 
                        className="submit-btn"
                        disabled={loading}
                    >
                        {loading ? "Creating..." : "Create Feedback Form"}
                    </button>
                </div>
            </form>
            
            {/* Debug Display */}
            {process.env.NODE_ENV !== 'production' && (
                <div className="debug-panel">
                    <h4>Form Data (Debug)</h4>
                    <pre>{JSON.stringify(formData, null, 2)}</pre>
                </div>
            )}
        </div>
    );
};

export default CreateFeedbackForm;