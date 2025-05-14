import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import authService from '../services/auth';
import './Login.css';

const Login = () => {
    const [credentials, setCredentials] = useState({ username: '', password: '' });
    const [error, setError] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const navigate = useNavigate();

    const handleChange = (e) => {
        const { name, value } = e.target;
        setCredentials({ ...credentials, [name]: value });
        // Clear error when user starts typing again
        if (error) setError('');
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setIsLoading(true);
        setError('');
        
        try {
            console.log("Attempting login with:", credentials.username);
            const response = await authService.login(credentials.username, credentials.password);
            console.log("Login response:", response);
            
            // Determine role based on response or username
            const userRole = response.role || determineRole(credentials.username);
            console.log("Assigned role:", userRole);
            
            // Create user object and store in localStorage
            const user = {
                ...response,
                role: userRole
            };
            
            localStorage.setItem('user', JSON.stringify(user));
            
            // Show success message before redirecting
            alert(`Successfully logged in as ${userRole.toLowerCase()}`);
            
            // Redirect based on role
            switch(userRole) {
                case 'ADMIN':
                    navigate('/admin');
                    break;
                case 'INSTRUCTOR':
                    navigate('/instructor');
                    break;
                case 'STUDENT':
                    navigate('/student-feedback');
                    break;
                default:
                    navigate('/');
            }
        } catch (err) {
            console.error("Login error:", err);
            setError('Invalid username or password');
        } finally {
            setIsLoading(false);
        }
    };

    // Helper function to determine role based on username for demo purposes
    const determineRole = (username) => {
        if (username.includes('admin')) return 'ADMIN';
        if (username.includes('instructor')) return 'INSTRUCTOR';
        return 'STUDENT';
    };

    return (
        <div className="login-container">
            <h2>Login</h2>
            {error && <div className="error">{error}</div>}
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="username">Username</label>
                    <input
                        type="text"
                        name="username"
                        id="username"
                        value={credentials.username}
                        onChange={handleChange}
                        required
                        disabled={isLoading}
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="password">Password</label>
                    <input
                        type="password"
                        name="password"
                        id="password"
                        value={credentials.password}
                        onChange={handleChange}
                        required
                        disabled={isLoading}
                    />
                </div>
                <button 
                    type="submit" 
                    className="btn btn-primary" 
                    disabled={isLoading}
                >
                    {isLoading ? 'Logging in...' : 'Login'}
                </button>
            </form>
            <p className="register-link">
                Don't have an account? <Link to="/register">Register here</Link>
            </p>
        </div>
    );
};

export default Login;