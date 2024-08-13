import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import './UpdateLeaveRequest.css';

function UpdateLeaveRequest() {
    const { id } = useParams();
    const [leaveRequest, setLeaveRequest] = useState({
        employee: {
            id: ''
        },
        startDate: '',
        endDate: ''
    });
    const token = localStorage.getItem('token');
    const navigate = useNavigate();

    useEffect(() => {
        const fetchLeaveRequest = async () => {
            try {
                const response = await fetch(`http://localhost:8080/api/leaverequests/${id}`, {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                });

                if (response.ok) {
                    const data = await response.json();
                    setLeaveRequest({
                        employee: { id: data.employee ? data.employee.id : '' },
                        startDate: data.startDate ? data.startDate.split('T')[0] : '',
                        endDate: data.endDate ? data.endDate.split('T')[0] : ''
                    });
                } else {
                    alert('Failed to fetch leave request');
                }
            } catch (error) {
                console.error('Error fetching leave request:', error);
            }
        };

        fetchLeaveRequest();
    }, [id, token]);

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const response = await fetch(`http://localhost:8080/api/leaverequests/${id}`, {
                method: 'PUT',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(leaveRequest),
            });

            if (response.ok) {
                navigate('/leave-requests');
            } else {
                alert('Failed to update leave request');
            }
        } catch (error) {
            console.error('Error updating leave request:', error);
            alert('Failed to update leave request');
        }
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setLeaveRequest((prev) => {
            if (name === 'employeeId') {
                return { ...prev, employee: { ...prev.employee, id: value } };
            }
            return { ...prev, [name]: value };
        });
    };

    return (
        <div className="update-leave-request-container">
            <h2>Update Leave Request</h2>
            <form onSubmit={handleSubmit}>
                <label>
                    Employee ID:
                    <input
                        type="text"
                        name="employeeId"
                        value={leaveRequest.employee.id}
                        onChange={handleChange}
                        readOnly
                    />
                </label>
                <br />
                <label>
                    Start Date:
                    <input
                        type="date"
                        name="startDate"
                        value={leaveRequest.startDate}
                        onChange={handleChange}
                    />
                </label>
                <br />
                <label>
                    End Date:
                    <input
                        type="date"
                        name="endDate"
                        value={leaveRequest.endDate}
                        onChange={handleChange}
                    />
                </label>
                <br />
                <button type="submit">Update</button>
            </form>
        </div>
    );
}

export default UpdateLeaveRequest;
