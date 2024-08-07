import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
/*import './UpdateLeaveRequest.css';*/

function UpdateLeaveRequest() {
    const { id } = useParams();
    const [leaveRequest, setLeaveRequest] = useState({
        employeeId: '',
        startDate: '',
        endDate: ''
    });
    const token = localStorage.getItem('token');
    const navigate = useNavigate();

    useEffect(() => {
        const fetchLeaveRequest = async () => {
            const response = await fetch(`http://localhost:8080/api/leaverequests/${id}`, {
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });

            if (response.ok) {
                const data = await response.json();
                setLeaveRequest(data);
            } else {
                alert('Failed to fetch leave request');
            }
        };

        fetchLeaveRequest();
    }, [id, token]);

    const handleSubmit = async (e) => {
        e.preventDefault();

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
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setLeaveRequest({ ...leaveRequest, [name]: value });
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
                        value={leaveRequest.employeeId}
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
