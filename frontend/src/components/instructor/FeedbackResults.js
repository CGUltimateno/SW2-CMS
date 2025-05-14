import React, { useState, useEffect } from 'react';
import { getFeedbackResults, exportFeedbackSummary } from '../../services/feedbackService';
import './FeedbackResults.css';

const FeedbackResults = ({ courseId, onBackClick }) => {
    const [results, setResults] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    
    useEffect(() => {
        const loadResults = async () => {
            try {
                setLoading(true);
                setError(null);
                
                const data = await getFeedbackResults(courseId);
                console.log("Feedback results:", data);
                setResults(data);
            } catch (err) {
                console.error("Error loading feedback:", err);
                setError("Failed to load feedback results. Please try again later.");
            } finally {
                setLoading(false);
            }
        };
        
        if (courseId) {
            loadResults();
        }
    }, [courseId]);
    
    
    const handleExport = async () => {
        try {
            const pdfBlob = await exportFeedbackSummary(courseId);
            
            // Create a URL for the blob
            const url = window.URL.createObjectURL(pdfBlob);
            
            // Create a temporary link and trigger download
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', `feedback-report-${courseId}.pdf`);
            document.body.appendChild(link);
            link.click();
            
            // Clean up
            link.parentNode.removeChild(link);
            window.URL.revokeObjectURL(url);
        } catch (err) {
            console.error("Error exporting feedback:", err);
            setError("Failed to export feedback report. Please try again later.");
        }
    };
    
    if (loading) {
        return <div className="loading-spinner">Loading feedback results...</div>;
    }
    
    if (error) {
        return (
            <div className="error-container">
                <p className="error-message">{error}</p>
                <button onClick={onBackClick} className="back-button">
                    Back to Course List
                </button>
            </div>
        );
    }
    
    if (!results || results.totalResponses === 0) {
        return (
            <div className="no-results">
                <h3>No Feedback Available</h3>
                <p>There is no feedback available for this course yet.</p>
                <button onClick={onBackClick} className="back-button">
                    Back to Course List
                </button>
            </div>
        );
    }
    
    return (
        <div className="feedback-results">
            <div className="results-header">
                <div>
                    <h2>{results.courseName} - Feedback Results</h2>
                    <p className="course-description">{results.courseDescription}</p>
                </div>
                <div className="header-actions">
                    <button onClick={handleExport} className="export-button">
                        Export Report
                    </button>
                    <button onClick={onBackClick} className="back-button">
                        Back to Course List
                    </button>
                </div>
            </div>
            
            <div className="results-summary">
                <div className="summary-card">
                    <span className="summary-value">{results.totalResponses}</span>
                    <span className="summary-label">Total Responses</span>
                </div>
                <div className="summary-card">
                    <span className="summary-value">{results.averageRating.toFixed(1)}</span>
                    <span className="summary-label">Average Rating</span>
                </div>
            </div>
            
            <div className="results-sections">
                {/* Rating Questions Section */}
                {Object.keys(results.ratingQuestionResults).length > 0 && (
                    <div className="results-section">
                        <h3>Rating Questions</h3>
                        
                        {Object.entries(results.ratingQuestionResults).map(([questionText, data]) => (
                            <div key={questionText} className="question-result">
                                <h4>{questionText}</h4>
                                
                                <div className="rating-summary">
                                    <div className="average-rating">
                                        <span className="rating-value">{data.averageRating.toFixed(1)}</span>
                                        <span className="rating-label">/5 Average</span>
                                    </div>
                                    
                                    <div className="rating-distribution">
                                        {[5, 4, 3, 2, 1].map(rating => {
                                            const count = data.ratingDistribution[rating] || 0;
                                            const percentage = data.responseCount > 0 
                                                ? (count / data.responseCount) * 100 
                                                : 0;
                                            
                                            return (
                                                <div key={rating} className="rating-bar">
                                                    <span className="rating-label">{rating}★</span>
                                                    <div className="bar-container">
                                                        <div 
                                                            className="bar-fill"
                                                            style={{ width: `${percentage}%` }}
                                                        ></div>
                                                    </div>
                                                    <span className="count-label">{count}</span>
                                                </div>
                                            );
                                        })}
                                    </div>
                                </div>
                            </div>
                        ))}
                    </div>
                )}
                
                {/* Text Responses Section */}
                {Object.keys(results.textResponses).length > 0 && (
                    <div className="results-section">
                        <h3>Text Responses</h3>
                        
                        {Object.entries(results.textResponses).map(([questionText, responses]) => (
                            <div key={questionText} className="question-result">
                                <h4>{questionText}</h4>
                                
                                <div className="text-responses">
                                    {responses.length === 0 ? (
                                        <p className="no-responses">No responses yet</p>
                                    ) : (
                                        <ul className="responses-list">
                                            {responses.map((response, index) => (
                                                <li key={index} className="text-response">
                                                    "{response}"
                                                </li>
                                            ))}
                                        </ul>
                                    )}
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
};

export default FeedbackResults;