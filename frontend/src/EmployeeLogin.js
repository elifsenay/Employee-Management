import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom'; // Import useNavigate
import './EmployeeLogin.css';
import showPasswordIcon from './showPassword.png'; // Adjust the path based on your folder structure

function EmployeeLogin() {
    const [employeeId, setEmployeeId] = useState('');
    const [password, setPassword] = useState('');
    const [showPassword, setShowPassword] = useState(false);
    const navigate = useNavigate(); // Initialize navigate

    const handleTogglePasswordVisibility = () => {
        setShowPassword(!showPassword);
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        // Handle login logic here
        // If login is successful:
        navigate('/home'); // Redirect to the home page
    };

    return (
        <div className="employee-login-container">
            <h1>Employee Login</h1>
            <form onSubmit={handleSubmit}>
                <label htmlFor="employeeId">Employee ID:</label>
                <input
                    type="text"
                    id="employeeId"
                    value={employeeId}
                    onChange={(e) => setEmployeeId(e.target.value)}
                    required
                />

                <label htmlFor="password">Password:</label>
                <div className="password-container">
                    <input
                        type={showPassword ? "text" : "password"}
                        id="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                    <img
                        src={showPasswordIcon}
                        alt="Show Password"
                        onClick={handleTogglePasswordVisibility}
                    />
                </div>

                <button type="submit">Login</button>
            </form>
        </div>
    );
}

export default EmployeeLogin;
