// src/EmployeeEntry.js
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './EmployeeEntry.css';
import LogoutButton from "./LogoutButton";

function EmployeeEntry() {
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [email, setEmail] = useState('');
    const [department, setDepartment] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();

    const handleSubmit = (e) => {
        e.preventDefault();

        const token = localStorage.getItem('token');

        fetch('http://localhost:8080/api/employees', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`,
            },
            body: JSON.stringify({ firstName, lastName, email, department, password }),
        })
            .then(response => {
                if (response.ok) {
                    navigate('/employee-list');
                } else {
                    alert('Failed to add employee');
                }
            })
            .catch(error => console.error('Error:', error));
    };

    return (
        <div className="employee-entry-container">
            <LogoutButton/>
            <h2>Add Employee</h2>
            <form onSubmit={handleSubmit}>
                <label>
                    First Name:
                    <input type="text" value={firstName} onChange={(e) => setFirstName(e.target.value)} />
                </label>
                <br />
                <label>
                    Last Name:
                    <input type="text" value={lastName} onChange={(e) => setLastName(e.target.value)} />
                </label>
                <br />
                <label>
                    Email:
                    <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} />
                </label>
                <br />
                <label>
                    Department:
                    <input type="text" value={department} onChange={(e) => setDepartment(e.target.value)} />
                </label>
                <br />
                <label>
                    Password:
                    <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} />
                </label>
                <br />
                <button type="submit">Add Employee</button>
            </form>
        </div>
    );
}

export default EmployeeEntry;
