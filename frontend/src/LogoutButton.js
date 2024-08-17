import React from 'react';
import { useNavigate } from 'react-router-dom';
import './LogoutButton.css';

function LogoutButton() {
    const navigate = useNavigate();

    const handleLogout = () => {
        localStorage.removeItem('token');  // Remove the token from localStorage
        navigate('/login');  // Redirect the user to the login page
    };

    return (
        <button className="logout-button" onClick={handleLogout}>
            Logout
        </button>
    );
}

export default LogoutButton;
