import React, { useEffect, useState } from 'react';
import feedbackService from '../services/feedbackService';

const StudentFeedback = () => {
    const [feedbackForms, setFeedbackForms] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchFeedbackForms = async () => {
            try {
                const forms = await feedbackService.getFeedbackForms();
                setFeedbackForms(forms);
            } catch (err) {
                setError('Failed to load feedback forms');
            } finally {
                setLoading(false);
            }
        };

        fetchFeedbackForms();
    }, []);

    const handleSubmit = async (formId, feedbackData) => {
        try {
            await feedbackService.submitFeedback(formId, feedbackData);
            alert('Feedback submitted successfully!');
        } catch (err) {
            setError('Failed to submit feedback');
        }
    };

    if (loading) {
        return <div>Loading feedback forms...</div>;
    }

    if (error) {
        return <div>{error}</div>;
    }

    return (
        <div className="container mt-5">
            <h2>Student Feedback</h2>
            {feedbackForms.length === 0 ? (
                <p>No feedback forms available.</p>
            ) : (
                feedbackForms.map((form) => (
                    <div key={form.id} className="feedback-form">
                        <h3>{form.title}</h3>
                        <form onSubmit={(e) => {
                            e.preventDefault();
                            const feedbackData = {}; // Collect feedback data from form inputs
                            handleSubmit(form.id, feedbackData);
                        }}>
                            {/* Render form questions dynamically here */}
                            <button type="submit" className="btn btn-primary">Submit Feedback</button>
                        </form>
                    </div>
                ))
            )}
        </div>
    );
};

export default StudentFeedback;