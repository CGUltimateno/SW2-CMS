import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import './Header.css';

const Header = () => {
    const navigate = useNavigate();
    
    // Get user info from localStorage
    const userString = localStorage.getItem('user');
    const user = userString ? JSON.parse(userString) : null;
    
    // Determine if user is logged in and their role
    const isLoggedIn = !!user;
    const userRole = user?.role || '';
    
    const handleLogout = () => {
        localStorage.removeItem('user');
        navigate('/login');
    };

    return (
        <header className="header">
            <div className="logo">
                <Link to="/">
                    <h1>Course Feedback System</h1>
                </Link>
            </div>
            <nav className="nav">
                {isLoggedIn && (
                    <>
                        {/* Show links based on user role */}
                        {userRole === 'STUDENT' && (
                            <Link to="/student-feedback">Submit Feedback</Link>
                        )}
                        
                        {userRole === 'ADMIN' && (
                            <Link to="/admin">Admin Dashboard</Link>
                        )}
                        
                        {userRole === 'INSTRUCTOR' && (
                            <Link to="/instructor">Instructor Dashboard</Link>
                        )}
                        
                        <button onClick={handleLogout} className="logout-btn">
                            Logout
                        </button>
                    </>
                )}
                
                {!isLoggedIn && (
                    <Link to="/login">Login</Link>
                )}
            </nav>
        </header>
    );
};

export default Header;