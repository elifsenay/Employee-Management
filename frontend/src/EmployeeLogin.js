import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './EmployeeLogin.css';

function EmployeeLogin() {
    const [employeeId, setEmployeeId] = useState('');
    const [password, setPassword] = useState('');
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

    return (
        <div className="login-container">
            <h2>Employee Login</h2>
            <form onSubmit={handleSubmit}>
                <label>
                    Employee ID:
                    <input type="text" value={employeeId} onChange={(e) => setEmployeeId(e.target.value)} />
                </label>
                <br />
                <label>
                    Password:
                    <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} />
                </label>
                <br />
                <button type="submit">Login</button>
            </form>
        </div>
    );
}

export default EmployeeLogin;
