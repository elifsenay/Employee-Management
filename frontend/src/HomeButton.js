import React from 'react';
import { useNavigate } from 'react-router-dom';
import './HomeButton.css';

function HomeButton() {
    const navigate = useNavigate();

    const handleHomeClick = () => {
        navigate('/home'); 
    };

    return (
        <button className="home-button" onClick={handleHomeClick}>
            Home
        </button>
    );
}

export default HomeButton;
