import React from 'react';
import { useNavigate } from 'react-router-dom';
import './LogoutButton.css';

function LogoutButton() {
    const navigate = useNavigate();

    const handleLogout = () => {
<<<<<<< Updated upstream
        localStorage.removeItem('token');
        navigate('/employee-login');
    };

    return (
        <button onClick={handleLogout} className="logout-button">Logout</button>
=======
        localStorage.removeItem('token'); // Remove token from local storage
        navigate('/login'); // Redirect to login page
    };

    return (
        <button onClick={handleLogout} className="logout-button">
            Logout
        </button>
>>>>>>> Stashed changes
    );
}

export default LogoutButton;
