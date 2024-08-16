import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import './UpdateLeaveRequest.css';

function UpdateLeaveRequest() {
    const { id } = useParams();
    const [employeeName, setEmployeeName] = useState('');
    const [startDate, setStartDate] = useState('');
    const [endDate, setEndDate] = useState('');
    const [feedback, setFeedback] = useState('');
    const navigate = useNavigate();
    const token = localStorage.getItem('token');

    // Fetch leave request and employee data
    useEffect(() => {
        const fetchData = async () => {
            const leaveRequestResponse = await fetch(`http://localhost:8080/api/leaverequests/${id}`, {
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
            });

            if (leaveRequestResponse.ok) {
                const leaveRequestData = await leaveRequestResponse.json();
                setStartDate(leaveRequestData.startDate);
                setEndDate(leaveRequestData.endDate);

                const employeeResponse = await fetch(`http://localhost:8080/api/employees/${leaveRequestData.employeeId}`, {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                    },
                });

                if (employeeResponse.ok) {
                    const employeeData = await employeeResponse.json();
                    setEmployeeName(`${employeeData.firstName} ${employeeData.lastName}`);
                } else {
                    setFeedback('Failed to fetch employee data');
                }
            } else {
                setFeedback('Failed to fetch leave request data');
            }
        };

        fetchData();
    }, [id, token]);

    const handleSubmit = (e) => {
        e.preventDefault();
        fetch(`http://localhost:8080/api/leaverequests/${id}`, {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                startDate: new Date(startDate).toISOString().split('T')[0],
                endDate: new Date(endDate).toISOString().split('T')[0],
            }),
        })
            .then(response => {
                if (response.ok) {
                    setFeedback('Leave request updated successfully!');
                    navigate('/leave-requests-list');
                } else {
                    setFeedback('Failed to update leave request.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                setFeedback('Error updating leave request.');
            });
    };

    const handleCancel = () => {
        navigate('/leave-requests-list');  // Navigate back to leave requests list
    };

    return (
        <div className="update-leave-request-container">
            <h2>Update Leave Request</h2>
            <form onSubmit={handleSubmit}>
                <label>Employee:</label>
                <input
                    type="text"
                    value={employeeName}
                    disabled
                    className="employee-name-display"
                />

                <label>Start Date:</label>
                <input type="date" value={startDate} onChange={(e) => setStartDate(e.target.value)} />

                <label>End Date:</label>
                <input type="date" value={endDate} onChange={(e) => setEndDate(e.target.value)} />

                <div className="button-group">
                    <button type="submit" className="update-button">Update</button>
                    <button type="button" className="cancel-button" onClick={handleCancel}>Cancel</button>
                </div>
            </form>
            {feedback && <p className="feedback">{feedback}</p>}
        </div>
    );
}

export default UpdateLeaveRequest;