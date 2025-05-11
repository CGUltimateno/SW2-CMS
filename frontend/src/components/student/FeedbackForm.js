import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import feedbackService from '../../services/feedbackService';
import './FeedbackForm.css';

const FeedbackForm = () => {
    const navigate = useNavigate();
    const [courses, setCourses] = useState([]);
    const [feedback, setFeedback] = useState({
        courseId: '',
        responses: {},
    });

    useEffect(() => {
        const fetchCourses = async () => {
            const result = await feedbackService.getCourses();
            setCourses(result.data);
        };
        fetchCourses();
    }, []);

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFeedback({
            ...feedback,
            [name]: value,
        });
    };

    const handleResponseChange = (questionId, value) => {
        setFeedback({
            ...feedback,
            responses: {
                ...feedback.responses,
                [questionId]: value,
            },
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        await feedbackService.submitFeedback(feedback);
        navigate('/thank-you');
    };

    return (
        <div className="feedback-form-container">
            <h2>Course Feedback Form</h2>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="courseId">Select Course</label>
                    <select
                        name="courseId"
                        id="courseId"
                        value={feedback.courseId}
                        onChange={handleInputChange}
                        required
                    >
                        <option value="">Select a course</option>
                        {courses.map((course) => (
                            <option key={course.id} value={course.id}>
                                {course.name}
                            </option>
                        ))}
                    </select>
                </div>
                {/* Assuming questions are fetched from the backend */}
                {courses.length > 0 && courses[0].questions.map((question) => (
                    <div key={question.id} className="form-group">
                        <label>{question.text}</label>
                        <input
                            type="text"
                            onChange={(e) => handleResponseChange(question.id, e.target.value)}
                            required
                        />
                    </div>
                ))}
                <button type="submit" className="btn btn-primary">Submit Feedback</button>
            </form>
        </div>
    );
};

export default FeedbackForm;