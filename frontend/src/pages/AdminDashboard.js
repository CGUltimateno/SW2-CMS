import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import feedbackService from '../services/feedbackService';
import './AdminDashboard.css';

const AdminDashboard = () => {
    const [feedbackForms, setFeedbackForms] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchFeedbackForms = async () => {
            try {
                setLoading(true);
                const response = await feedbackService.getFeedbackForms();
                setFeedbackForms(response.data);
                setError(null);
            } catch (error) {
                console.error("Error fetching feedback forms:", error);
                setError("Failed to load feedback forms. Please try again later.");
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
            <div className="action-bar">
                <h2>Admin Dashboard</h2>
                <Link to="/create-feedback-form" className="btn btn-primary">Create Feedback Form</Link>
            </div>

            {error && <div className="error-message">{error}</div>}

            <div className="card-grid">
                <div className="card">
                    <div className="card-title">Active Forms</div>
                    <div className="stat-value">{feedbackForms.length}</div>
                </div>
                <div className="card">
                    <div className="card-title">Responses</div>
                    <div className="stat-value">142</div>
                </div>
                <div className="card">
                    <div className="card-title">Courses</div>
                    <div className="stat-value">8</div>
                </div>
            </div>

            <h3>Feedback Forms</h3>
            {feedbackForms.length === 0 ? (
                <p>No feedback forms available. Create one to get started.</p>
            ) : (
                <table className="table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Title</th>
                            <th>Created At</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {feedbackForms.map(form => (
                            <tr key={form.id}>
                                <td>{form.id}</td>
                                <td>{form.title}</td>
                                <td>{new Date(form.createdAt).toLocaleDateString()}</td>
                                <td>
                                    <span className={`status ${form.active ? 'status-active' : 'status-inactive'}`}>
                                        {form.active ? 'Active' : 'Inactive'}
                                    </span>
                                </td>
                                <td>
                                    <Link to={`/manage-feedback-form/${form.id}`} className="btn btn-info">Manage</Link>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            )}
        </div>
    );
};

export default AdminDashboard;