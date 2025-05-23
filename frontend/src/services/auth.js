import axios from 'axios';

const API_URL = 'http://localhost:8080/api/auth/';

const register = (firstName, lastName, username, email, phoneNumber, password, role) => {
    return axios.post(API_URL + 'register', {
        firstName,
        lastName,
        username,
        email,
        phoneNumber,
        password,
        role
    });
};

const login = (username, password) => {
    return axios
        .post(API_URL + 'login', {
            username,
            password,
        })
        .then((response) => {
            if (response.data.accessToken) {
                localStorage.setItem('user', JSON.stringify(response.data));
            }
            return response.data;
        });
};

const logout = () => {
    localStorage.removeItem('user');
};

const getCurrentUser = () => {
    return JSON.parse(localStorage.getItem('user'));
};

export default {
    register,
    login,
    logout,
    getCurrentUser,
};