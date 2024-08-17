import React from 'react';
import { useNavigate } from 'react-router-dom';
import './ChangePasswordButton.css';

function ChangePasswordButton() {
    const navigate = useNavigate();

    const handleChangePassword = () => {
        navigate('/change-password');
    };

    return (
        <button className="change-password-button" onClick={handleChangePassword}>
            Change Password
        </button>
    );
}

export default ChangePasswordButton;
