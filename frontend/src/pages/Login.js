import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import authService from '../services/auth';
import './Login.css';

const Login = () => {
    const [credentials, setCredentials] = useState({ username: '', password: '' });
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleChange = (e) => {
        const { name, value } = e.target;
        setCredentials({ ...credentials, [name]: value });
    };

const handleSubmit = async (e) => {
    e.preventDefault();
    try {
        const response = await authService.login(credentials.username, credentials.password);
        
        const user = {
            ...response,
            role: response.role || determineRole(credentials.username)
        };
        
        localStorage.setItem('user', JSON.stringify(user));
        
        // Redirect based on role
        switch(user.role) {
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
        setError('Invalid username or password');
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
                    />
                </div>
                <button type="submit" className="btn btn-primary">Login</button>
            </form>
        </div>
    );
};

export default Login;