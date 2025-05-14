import React, { useEffect, useState } from 'react';
import courseService from '../services/courseService';
import InstructorCourseForm from '../components/instructor/InstructorCourseForm';
import InstructorCourseList from '../components/instructor/InstructorCourseList';
import FeedbackResults from '../components/instructor/FeedbackResults';
import './InstructorDashboard.css';

const InstructorDashboard = () => {
    const [courses, setCourses] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [activeTab, setActiveTab] = useState('courses');
    const [selectedCourseId, setSelectedCourseId] = useState(null);
    
    useEffect(() => {
        const fetchCourses = async () => {
            try {
                setLoading(true);
                setError(null);
                
                // Get instructor's courses
                const coursesData = await courseService.getInstructorCourses();
                console.log("Instructor courses:", coursesData);
                setCourses(coursesData);
                
            } catch (err) {
                console.error("Error fetching courses:", err);
                setError("Failed to load your courses. Please try again later.");
            } finally {
                setLoading(false);
            }
        };
        
        fetchCourses();
    }, []);
    
    const handleCourseCreated = (newCourse) => {
        setCourses(prevCourses => [...prevCourses, newCourse]);
    };
    
    const handleViewFeedback = (courseId) => {
        setSelectedCourseId(courseId);
        setActiveTab('feedback');
    };
    
    const handleBackToCourses = () => {
        setSelectedCourseId(null);
        setActiveTab('courses');
    };
    
    if (loading) {
        return <div className="loading-container">Loading dashboard data...</div>;
    }
    
    return (
        <div className="instructor-dashboard">
            <h2>Instructor Dashboard</h2>
            
            {error && <div className="error-message">{error}</div>}
            
            <div className="dashboard-tabs">
                <button 
                    className={`tab-button ${activeTab === 'courses' ? 'active' : ''}`}
                    onClick={() => setActiveTab('courses')}
                >
                    My Courses
                </button>
                {selectedCourseId && (
                    <button 
                        className={`tab-button ${activeTab === 'feedback' ? 'active' : ''}`}
                        onClick={() => setActiveTab('feedback')}
                    >
                        Course Feedback
                    </button>
                )}
            </div>
            
            <div className="tab-content">
                {activeTab === 'courses' && (
                    <div className="courses-section">
                        <InstructorCourseForm onCourseCreated={handleCourseCreated} />
                        <InstructorCourseList 
                            courses={courses} 
                            onViewFeedback={handleViewFeedback} 
                        />
                    </div>
                )}
                
                {activeTab === 'feedback' && selectedCourseId && (
                    <FeedbackResults 
                        courseId={selectedCourseId} 
                        onBackClick={handleBackToCourses} 
                    />
                )}
            </div>
        </div>
    );
};

export default InstructorDashboard;