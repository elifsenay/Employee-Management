// src/LoginButtonsPage.js
import React from 'react';
import { Link } from 'react-router-dom';
import './LoginButtonsPage.css';

function LoginButtonsPage() {
    return (
        <div className="login-buttons-container">
            <h2>Login Page</h2>
            <Link to="/login/employee">
                <button>Employee Login</button>
            </Link>
            <Link to="/login/admin">
                <button>Admin Login</button>
            </Link>
        </div>
    );
}

export default LoginButtonsPage;
