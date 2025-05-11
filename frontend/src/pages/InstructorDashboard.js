import React, { useEffect, useState } from 'react';
import { getFeedbackResults } from '../services/feedbackService';
import FeedbackResults from '../components/instructor/FeedbackResults';

const InstructorDashboard = () => {
    const [feedbackData, setFeedbackData] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchFeedbackData = async () => {
            try {
                const results = await getFeedbackResults();
                setFeedbackData(results);
            } catch (error) {
                console.error("Error fetching feedback data:", error);
            } finally {
                setLoading(false);
            }
        };

        fetchFeedbackData();
    }, []);

    if (loading) {
        return <div>Loading...</div>;
    }

    return (
        <div className="instructor-dashboard">
            <h2>Instructor Dashboard</h2>
            {feedbackData.length > 0 ? (
                <FeedbackResults data={feedbackData} />
            ) : (
                <p>No feedback results available.</p>
            )}
        </div>
    );
};

export default InstructorDashboard;