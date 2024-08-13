import React, { useState, useEffect, useCallback } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import Select from 'react-select';
import './LeaveRequest.css';
import LogoutButton from "./LogoutButton";

function LeaveRequest() {
    const [employee, setEmployee] = useState(null);
    const [employees, setEmployees] = useState([]);
    const [startDate, setStartDate] = useState('');
    const [endDate, setEndDate] = useState('');
    const [feedback, setFeedback] = useState('');
    const [leaveRequests, setLeaveRequests] = useState([]);
    const token = localStorage.getItem('token');
    const navigate = useNavigate();

    // Fetch employees
    useEffect(() => {
        fetch('http://localhost:8080/api/employees', {
            headers: {
                'Authorization': `Bearer ${token}`,
            }
        })
            .then(response => response.json())
            .then(data => setEmployees(data))
            .catch(error => console.error('Error fetching employees:', error));
    }, [token]);

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
                console.log(data); // Log the entire response to inspect it
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

    const handleSubmit = (e) => {
        e.preventDefault();

        // Ensure an employee is selected
        if (!employee) {
            setFeedback('Please select an employee.');
            return;
        }

        let startDateObj, endDateObj;

// If startDate is a string, convert it to a Date object
        if (typeof startDate === 'string' || startDate instanceof String) {
            startDateObj = new Date(startDate);
        } else {
            startDateObj = startDate;
        }

// If endDate is a string, convert it to a Date object
        if (typeof endDate === 'string' || endDate instanceof String) {
            endDateObj = new Date(endDate);
        } else {
            endDateObj = endDate;
        }

// Now format the dates
        const formattedStartDate = startDateObj.toISOString().split('T')[0];
        const formattedEndDate = endDateObj.toISOString().split('T')[0];

        console.log("Formatted Start Date:", formattedStartDate);
        console.log("Formatted End Date:", formattedEndDate);
        // Create the request payload
        const leaveRequestPayload = {
            employee: { id: employee.value.id },  // Send only the ID
            startDate: formattedStartDate,
            endDate: formattedEndDate
        };

        // Make the POST request to the backend API
        fetch('http://localhost:8080/api/leaverequests', {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(leaveRequestPayload),
        })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(err => { throw new Error(err.message); });
                }
                return response.json();
            })
            .then(() => {
                setFeedback('Leave request added successfully!');
                setEmployee(null);
                setStartDate('');
                setEndDate('');
                fetchLeaveRequests(); // Refresh the list after adding
            })
            .catch((error) => {
                setFeedback(`Error adding leave request: ${error.message}`);
                console.error('Error:', error);
            });
    };


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

    const formatDate = (dateString) => {
        const date = new Date(dateString);
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        const year = date.getFullYear();
        return `${month}/${day}/${year}`;
    };

    return (
        <div className="leave-request-container">
            <LogoutButton />
            <h2>Add Leave Request</h2>
            <form onSubmit={handleSubmit}>
                <label>Employee:</label>
                <Select
                    value={employee}
                    onChange={(selectedOption) => setEmployee(selectedOption)}
                    options={employees.map(emp => ({
                        value: emp,
                        label: `${emp.firstName} ${emp.lastName} (ID: ${emp.id})`
                    }))}
                    placeholder="Select an employee"
                />
                <label>Start Date:</label>
                <input type="date" value={startDate} onChange={(e) => setStartDate(e.target.value)} />
                <label>End Date:</label>
                <input type="date" value={endDate} onChange={(e) => setEndDate(e.target.value)} />
                <button type="submit">Add</button>
            </form>
            {feedback && <p className="feedback">{feedback}</p>}
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
                                <button onClick={() => handleUpdate(request.id)}>Update</button>
                                <button onClick={() => handleDelete(request.id)}>Delete</button>
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