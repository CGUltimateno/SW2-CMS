import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import feedbackService from '../services/feedbackService';
import FeedbackFormManagement from '../components/admin/FeedbackFormManagement';
import './AdminDashboard.css';

const AdminDashboard = () => {
    const [feedbackForms, setFeedbackForms] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchFeedbackForms = async () => {
            try {
                setLoading(true);
                // Use the correct function name from your service
                const data = await feedbackService.getAllFeedbackForms();
                console.log("Feedback forms data:", data);
                setFeedbackForms(Array.isArray(data) ? data : []);
                setError(null);
            } catch (error) {
                console.error("Error fetching feedback forms:", error);
                const errorMessage = error.response?.status === 403 
                    ? "Access denied. You don't have permission to view feedback forms." 
                    : "Failed to load feedback forms. Please try again later.";
                setError(errorMessage);
                            } finally {
                setLoading(false);
            }
        };

        fetchFeedbackForms();
    }, []);

    if (loading) {
        return <div className="loading-spinner">Loading dashboard data...</div>;
    }

    return (
        <div className="admin-dashboard">
            <h2 className="dashboard-title">Admin Dashboard</h2>
            
            {error && <div className="error-message">{error}</div>}
            
            <div className="dashboard-section">
                <FeedbackFormManagement />
            </div>

            <div className="card-grid">
                <div className="card">
                    <div className="card-title">Active Forms</div>
                    <div className="stat-value">{feedbackForms.filter(form => form.active).length}</div>
                </div>
                <div className="card">
                    <div className="card-title">Total Forms</div>
                    <div className="stat-value">{feedbackForms.length}</div>
                </div>
                <div className="card">
                    <div className="card-title">Courses</div>
                    <div className="stat-value">8</div>
                </div>
            </div>
        </div>
    );
};

export default AdminDashboard;