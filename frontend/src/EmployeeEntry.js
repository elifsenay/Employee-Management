// src/EmployeeEntry.js
import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import './EmployeeEntry.css';

function EmployeeEntry() {
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [email, setEmail] = useState('');
    const [department, setDepartment] = useState('');
    const [feedback, setFeedback] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        fetch('http://localhost:8080/api/employees', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ firstName, lastName, email, department }),
        })
            .then(response => response.json())
            .then(data => {
                setFeedback('Employee added successfully!');
                setFirstName('');
                setLastName('');
                setEmail('');
                setDepartment('');
            })
            .catch((error) => {
                setFeedback('Error adding employee. Please try again.');
                console.error('Error:', error);
            });
    };

    return (
        <div className="employee-entry-container">
            <h2>Add Employee</h2>
            <form onSubmit={handleSubmit}>
                <label>First Name:</label>
                <input type="text" value={firstName} onChange={(e) => setFirstName(e.target.value)} />
                <label>Last Name:</label>
                <input type="text" value={lastName} onChange={(e) => setLastName(e.target.value)} />
                <label>Email:</label>
                <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} />
                <label>Department:</label>
                <input type="text" value={department} onChange={(e) => setDepartment(e.target.value)} />
                <button type="submit">Add</button>
            </form>
            {feedback && <p className="feedback">{feedback}</p>}
            <p>
                <Link to="/employee-list">Go to Employee List</Link>
            </p>
        </div>
    );
}

export default EmployeeEntry;
