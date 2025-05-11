import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Header from './components/common/Header';
import Footer from './components/common/Footer';
import AdminDashboard from './pages/AdminDashboard';
import InstructorDashboard from './pages/InstructorDashboard';
import Login from './pages/Login';
import StudentFeedback from './pages/StudentFeedback';
import './theme.css';

const App = () => {
    return (
        <Router>
            <Header />
            <div className="page-container">
                <div className="container">
                    <Routes>
                        <Route path="/" element={<Login />} />
                        <Route path="/login" element={<Login />} />
                        <Route path="/admin" element={<AdminDashboard />} />
                        <Route path="/instructor" element={<InstructorDashboard />} />
                        <Route path="/student-feedback" element={<StudentFeedback />} />
                    </Routes>
                </div>
            </div>
            <Footer />
        </Router>
    );
};

export default App;