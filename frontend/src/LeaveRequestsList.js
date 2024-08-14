import React, { useState, useEffect, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import './LeaveRequestsList.css';

function LeaveRequestsList() {
    const [leaveRequests, setLeaveRequests] = useState([]);
    const token = localStorage.getItem('token');
    const navigate = useNavigate();

    const fetchLeaveRequests = useCallback(() => {
        fetch('http://localhost:8080/api/leaverequests', {
            headers: {
                'Authorization': `Bearer ${token}`,
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
            })
            .then(data => {
                setLeaveRequests(data);
            })
            .catch(error => {
                console.error('Error fetching leave requests:', error);
                setLeaveRequests([]); // Set to an empty array or handle the error as needed
            });
    }, [token]);

    useEffect(() => {
        fetchLeaveRequests();
    }, [fetchLeaveRequests]);

    const handleDelete = async (id) => {
        const response = await fetch(`http://localhost:8080/api/leaverequests/${id}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            }
        });

        if (response.ok) {
            setLeaveRequests(leaveRequests.filter(request => request.id !== id));
        } else {
            alert('Failed to delete leave request');
        }
    };

    const handleUpdate = (id) => {
        navigate(`/update-leave-request/${id}`);
    };

    const handleCreateNew = () => {
        navigate('/leave-requests'); // Navigates to the Leave Request creation page
    };

    const formatDate = (dateString) => {
        const date = new Date(dateString);
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        const year = date.getFullYear();
        return `${month}/${day}/${year}`;
    };

    return (
        <div className="leave-requests-list-container">
            <h2>Leave Requests</h2>
            <div className="leave-requests-box">
                <table>
                    <thead>
                    <tr>
                        <th>Employee</th>
                        <th>Start Date</th>
                        <th>End Date</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {leaveRequests.map(request => (
                        <tr key={request.id}>
                            <td>
                                {request.employee ?
                                    `${request.employee.firstName} ${request.employee.lastName}` :
                                    `Employee ID: ${request.employee ? request.employee.id : 'Unknown Employee'}`
                                }
                            </td>
                            <td>{formatDate(request.startDate)}</td>
                            <td>{formatDate(request.endDate)}</td>
                            <td>
                                <button className="update-button" onClick={() => handleUpdate(request.id)}>Update</button>
                                <button className="delete-button" onClick={() => handleDelete(request.id)}>Delete</button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
            <div className="link-to-add">
                <button onClick={handleCreateNew} className="create-button">Create New Leave Request</button>
            </div>
        </div>
    );
}

export default LeaveRequestsList;
