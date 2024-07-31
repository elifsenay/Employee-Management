// src/LeaveRequest.js
import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import './LeaveRequest.css';

function LeaveRequest() {
    const [employeeId, setEmployeeId] = useState('');
    const [startDate, setStartDate] = useState('');
    const [endDate, setEndDate] = useState('');
    const [feedback, setFeedback] = useState('');
    const [leaveRequests, setLeaveRequests] = useState([]);
    const [isEditing, setIsEditing] = useState(false);
    const [editId, setEditId] = useState(null);

    useEffect(() => {
        fetchLeaveRequests();
    }, []);

    const fetchLeaveRequests = () => {
        fetch('http://localhost:8080/api/leaverequests')
            .then(response => response.json())
            .then(data => setLeaveRequests(data))
            .catch(error => console.error('Error:', error));
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        const method = isEditing ? 'PUT' : 'POST';
        const url = isEditing
            ? `http://localhost:8080/api/leaverequests/${editId}`
            : 'http://localhost:8080/api/leaverequests';

        fetch(url, {
            method: method,
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
                    return response.json().then(err => { throw new Error(err.message) });
                }
                return response.json();
            })
            .then(() => {
                setFeedback(isEditing ? 'Leave request updated successfully!' : 'Leave request added successfully!');
                setEmployeeId('');
                setStartDate('');
                setEndDate('');
                setIsEditing(false);
                setEditId(null);
                fetchLeaveRequests(); // Refresh the list after adding
            })
            .catch((error) => {
                setFeedback(`Error adding leave request: ${error.message}`);
                console.error('Error:', error);
            });
    };

    const handleDelete = (id) => {
        fetch(`http://localhost:8080/api/leaverequests/${id}`, {
            method: 'DELETE',
        })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(err => { throw new Error(err.message) });
                }
                setFeedback('Leave request deleted successfully!');
                fetchLeaveRequests();
            })
            .catch((error) => {
                setFeedback(`Error deleting leave request: ${error.message}`);
                console.error('Error:', error);
            });
    };

    const handleEdit = (id) => {
        const leaveRequest = leaveRequests.find(req => req.id === id);
        setEmployeeId(leaveRequest.employeeId);
        setStartDate(leaveRequest.startDate);
        setEndDate(leaveRequest.endDate);
        setIsEditing(true);
        setEditId(id);
    };

    const formatDate = (dateString) => {
        const date = new Date(dateString);
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        const year = date.getFullYear();
        return `${month}/${day}/${year}`;
    };

    return (
        <div className="leave-request-container">
            <h2>{isEditing ? 'Edit Leave Request' : 'Add Leave Request'}</h2>
            <form onSubmit={handleSubmit}>
                <label>Employee ID:</label>
                <input type="text" value={employeeId} onChange={(e) => setEmployeeId(e.target.value)} />
                <label>Start Date:</label>
                <input type="date" value={startDate} onChange={(e) => setStartDate(e.target.value)} />
                <label>End Date:</label>
                <input type="date" value={endDate} onChange={(e) => setEndDate(e.target.value)} />
                <button type="submit">{isEditing ? 'Update' : 'Add'}</button>
            </form>
            {feedback && <p className={`feedback ${feedback.includes('Error') ? 'error' : ''}`}>{feedback}</p>}
            <h2>Leave Requests</h2>
            <div className="leave-requests-box">
                <table>
                    <thead>
                    <tr>
                        <th>Employee ID</th>
                        <th>Start Date</th>
                        <th>End Date</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {leaveRequests.map(request => (
                        <tr key={request.id}>
                            <td>{request.employeeId}</td>
                            <td>{formatDate(request.startDate)}</td>
                            <td>{formatDate(request.endDate)}</td>
                            <td>
                                <div className="action-buttons">
                                    <button onClick={() => handleEdit(request.id)}>Update</button>
                                    <button onClick={() => handleDelete(request.id)}>Delete</button>
                                </div>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
            <p>
                <Link to="/employee-list">Go to Employee List</Link>
            </p>
        </div>
    );
}

export default LeaveRequest;
