import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './EmployeeLogin.css';
import showPasswordIcon from './show-password.png';

function EmployeeLogin() {
    const [employeeId, setEmployeeId] = useState('');
    const [password, setPassword] = useState('');
    const [showPassword, setShowPassword] = useState(false);
    const navigate = useNavigate();

    const handleSubmit = (e) => {
        e.preventDefault();
        fetch('http://localhost:8080/api/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ employeeId, password }),
        })
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw new Error('Invalid credentials');
                }
            })
            .then(data => {
                localStorage.setItem('token', data.jwt);
                navigate('/home');
            })
            .catch(error => {
                alert(error.message);
            });
    };

    const togglePasswordVisibility = () => {
        setShowPassword(!showPassword);
    };

    return (
        <div className="login-container">
            <h2>Employee Login</h2>
            <form onSubmit={handleSubmit}>
                <label>
                    Employee ID
                    <input type="text" value={employeeId} onChange={(e) => setEmployeeId(e.target.value)} />
                </label>
                <label>
                    Password
                    <div className="password-container">
                        <input
                            type={showPassword ? 'text' : 'password'}
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                        />
                        <img
                        src={showPasswordIcon}
                        alt={"Show Password"}
                        className="password-toggle"
                        onClick={togglePasswordVisibility}
                        />
                    </div>
                </label>
                <button type="submit" className="login-button">Login</button>
            </form>
        </div>
    );
}

export default EmployeeLogin;
