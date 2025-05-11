import React, { useEffect, useState } from 'react';
import axios from 'axios';

const ManageCourses = () => {
    const [courses, setCourses] = useState([]);
    const [courseName, setCourseName] = useState('');
    const [editingCourseId, setEditingCourseId] = useState(null);

    useEffect(() => {
        fetchCourses();
    }, []);

    const fetchCourses = async () => {
        const response = await axios.get('http://localhost:8080/courses');
        setCourses(response.data);
    };

    const handleAddOrUpdateCourse = async (e) => {
        e.preventDefault();
        if (editingCourseId) {
            await axios.put(`http://localhost:8080/courses/${editingCourseId}`, { name: courseName });
        } else {
            await axios.post('http://localhost:8080/courses', { name: courseName });
        }
        setCourseName('');
        setEditingCourseId(null);
        fetchCourses();
    };

    const handleEditCourse = (course) => {
        setCourseName(course.name);
        setEditingCourseId(course.id);
    };

    const handleDeleteCourse = async (courseId) => {
        await axios.delete(`http://localhost:8080/courses/${courseId}`);
        fetchCourses();
    };

    return (
        <div className="container mt-5">
            <h2>Manage Courses</h2>
            <form onSubmit={handleAddOrUpdateCourse}>
                <div className="form-group">
                    <label htmlFor="courseName">Course Name</label>
                    <input
                        type="text"
                        className="form-control"
                        id="courseName"
                        value={courseName}
                        onChange={(e) => setCourseName(e.target.value)}
                        required
                    />
                </div>
                <button type="submit" className="btn btn-primary mt-3">
                    {editingCourseId ? 'Update Course' : 'Add Course'}
                </button>
            </form>
            <h3 className="mt-5">Existing Courses</h3>
            <ul className="list-group">
                {courses.map(course => (
                    <li key={course.id} className="list-group-item d-flex justify-content-between align-items-center">
                        {course.name}
                        <div>
                            <button className="btn btn-warning btn-sm" onClick={() => handleEditCourse(course)}>Edit</button>
                            <button className="btn btn-danger btn-sm ml-2" onClick={() => handleDeleteCourse(course.id)}>Delete</button>
                        </div>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default ManageCourses;