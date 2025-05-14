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
                        {userRole === 'ROLE_USER' && (
  <>
    <li className="nav-item">
      <Link to="/dashboard" className="nav-link">My Courses</Link>
    </li>
    <li className="nav-item">
      <Link to="/feedback" className="nav-link">My Feedback</Link>
    </li>
  </>
)}
                        
                        {userRole === 'ROLE_ADMIN' && (
                            <Link to="/admin">Admin Dashboard</Link>
                        )}
                        
                        {userRole === 'ROLE_INSTRUCTOR' && (
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