import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import './LeaveRequest.css';

function LeaveRequest() {
    const [employeeId, setEmployeeId] = useState('');
    const [startDate, setStartDate] = useState('');
    const [endDate, setEndDate] = useState('');
    const [feedback, setFeedback] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        fetch('http://localhost:8080/api/leaverequests', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                employeeId: Number(employeeId),
                startDate: new Date(startDate).toISOString().split('T')[0],
                endDate: new Date(endDate).toISOString().split('T')[0]
            }),
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(() => {
                setFeedback('Leave request added successfully!');
                setEmployeeId('');
                setStartDate('');
                setEndDate('');
            })
            .catch((error) => {
                setFeedback('Error adding leave request. Please try again.');
                console.error('Error:', error);
            });
    };

    return (
        <div className="leave-request-container">
            <h2>Add Leave Request</h2>
            <form onSubmit={handleSubmit}>
                <label>Employee ID:</label>
                <input type="text" value={employeeId} onChange={(e) => setEmployeeId(e.target.value)} />
                <label>Start Date:</label>
                <input type="date" value={startDate} onChange={(e) => setStartDate(e.target.value)} />
                <label>End Date:</label>
                <input type="date" value={endDate} onChange={(e) => setEndDate(e.target.value)} />
                <button type="submit">Add</button>
            </form>
            {feedback && <p className="feedback">{feedback}</p>}
            <p>
                <Link to="/employee-list">Go to Employee List</Link>
            </p>
        </div>
    );
}

export default LeaveRequest;
