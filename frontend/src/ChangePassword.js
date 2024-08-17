import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

function ChangePassword() {
    const [oldPassword, setOldPassword] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (newPassword !== confirmPassword) {
            setError("New password and confirm password do not match.");
            return;
        }

        const token = localStorage.getItem('token');
        const employeeId = localStorage.getItem('employeeId');

        if (!employeeId || !token) {
            setError("Invalid session. Please log in again.");
            navigate('/login');
            return;
        }

        try {
            const response = await fetch('http://localhost:8080/api/change-password', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`,
                },
                body: JSON.stringify({
                    employeeId: employeeId,
                    oldPassword: oldPassword,
                    newPassword: newPassword,
                }),
            });

            if (response.ok) {
                navigate('/employee-list');
            } else {
                const errorData = await response.text();
                setError(errorData);
            }
        } catch (error) {
            console.error('Error:', error);
            setError('An error occurred while changing password.');
        }
    };

    return (
        <div className="change-password-container">
            <h2>Change Password</h2>
            <form onSubmit={handleSubmit}>
                <label>
                    Old Password:
                    <input
                        type="password"
                        value={oldPassword}
                        onChange={(e) => setOldPassword(e.target.value)}
                        placeholder="Enter old password"
                        required
                    />
                </label>
                <label>
                    New Password:
                    <input
                        type="password"
                        value={newPassword}
                        onChange={(e) => setNewPassword(e.target.value)}
                        placeholder="Enter new password"
                        required
                        minLength="8"
                    />
                </label>
                <label>
                    Confirm New Password:
                    <input
                        type="password"
                        value={confirmPassword}
                        onChange={(e) => setConfirmPassword(e.target.value)}
                        placeholder="Confirm new password"
                        required
                        minLength="8"
                    />
                </label>
                {error && <p className="error">{error}</p>}
                <button type="submit">Change Password</button>
            </form>
        </div>
    );
}

export default ChangePassword;
