import React, { useEffect, useState } from 'react';
import { getFeedbackResults } from '../../services/feedbackService';
import './FeedbackResults.css';

const FeedbackResults = () => {
    const [results, setResults] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [stats, setStats] = useState({
        averageRating: 0,
        totalResponses: 0,
        highestRated: ''
    });

    useEffect(() => {
        const fetchResults = async () => {
            try {
                setLoading(true);
                const data = await getFeedbackResults(1); // Replace with actual courseId
                setResults(data);
                
                // Calculate stats if data exists
                if (data && data.length > 0) {
                    const totalRatings = data.reduce((sum, item) => sum + item.averageRating, 0);
                    const avgRating = (totalRatings / data.length).toFixed(1);
                    const highest = data.reduce((prev, current) => 
                        (prev.averageRating > current.averageRating) ? prev : current
                    );
                    
                    setStats({
                        averageRating: avgRating,
                        totalResponses: data.length,
                        highestRated: highest.courseName
                    });
                }
            } catch (err) {
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };

        fetchResults();
    }, []);

    if (loading) {
        return <div className="loading-spinner">Loading feedback results...</div>;
    }

    if (error) {
        return <div className="error-message">Error fetching feedback results: {error}</div>;
    }

    return (
        <div className="feedback-results">
            <h2>Feedback Results</h2>
            
            <div className="feedback-stats">
                <div className="stat-card">
                    <div className="stat-value">{stats.averageRating}</div>
                    <div className="stat-label">Average Rating</div>
                </div>
                <div className="stat-card">
                    <div className="stat-value">{stats.totalResponses}</div>
                    <div className="stat-label">Total Responses</div>
                </div>
                <div className="stat-card">
                    <div className="stat-value">{stats.highestRated}</div>
                    <div className="stat-label">Highest Rated Course</div>
                </div>
            </div>
            
            {results.length === 0 ? (
                <p>No feedback available.</p>
            ) : (
                <>
                    <table className="table">
                        <thead>
                            <tr>
                                <th>Course</th>
                                <th>Instructor</th>
                                <th>Average Rating</th>
                                <th>Responses</th>
                            </tr>
                        </thead>
                        <tbody>
                            {results.map((result, index) => (
                                <tr key={index}>
                                    <td>{result.courseName}</td>
                                    <td>{result.instructorName}</td>
                                    <td>{result.averageRating}</td>
                                    <td>{result.responses || 'N/A'}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                    
                    <div className="comments-section">
                        <h3>Student Comments</h3>
                        {results.flatMap(result => 
                            result.comments.map((comment, i) => (
                                <div key={`${result.courseName}-${i}`} className="comment-card">
                                    <p>{comment}</p>
                                    <small>Course: {result.courseName}</small>
                                </div>
                            ))
                        )}
                    </div>
                </>
            )}
        </div>
    );
};

export default FeedbackResults;