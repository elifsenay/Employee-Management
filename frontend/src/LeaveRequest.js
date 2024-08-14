import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import Select from 'react-select';
import './LeaveRequest.css';
import LogoutButton from "./LogoutButton";

function LeaveRequest() {
    const [employee, setEmployee] = useState(null);
    const [employees, setEmployees] = useState([]);
    const [startDate, setStartDate] = useState('');
    const [endDate, setEndDate] = useState('');
    const [feedback, setFeedback] = useState('');
    const token = localStorage.getItem('token');

    useEffect(() => {
        fetch('http://localhost:8080/api/employees', {
            headers: {
                'Authorization': `Bearer ${token}`,
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to fetch employees.');
                }
                return response.json();
            })
            .then(data => setEmployees(data))
            .catch(error => setFeedback(`Error fetching employees: ${error.message}`));
    }, [token]);

    const handleSubmit = (e) => {
        e.preventDefault();

        if (!employee) {
            setFeedback('Please select an employee.');
            return;
        }

        if (!startDate || !endDate) {
            setFeedback('Please select both start and end dates.');
            return;
        }

        if (new Date(startDate) > new Date(endDate)) {
            setFeedback('End date must be after start date.');
            return;
        }

        // Directly using the values from the form inputs
        const leaveRequestPayload = {
            employee: { id: employee.value.id }, // Assuming employee.value is already set
            startDate: startDate, // Directly using the start date from the input
            endDate: endDate // Directly using the end date from the input
        };

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
                setFeedback('Leave request added successfully!');
                setEmployee(null);
                setStartDate('');
                setEndDate('');
            })
            .catch(error => {
                setFeedback(`Error adding leave request: ${error.message}`);
                console.error('Error:', error);
            });
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
                    isClearable
                />
                <label>Start Date:</label>
                <input
                    type="date"
                    value={startDate}
                    onChange={(e) => setStartDate(e.target.value)}
                    required
                />
                <label>End Date:</label>
                <input
                    type="date"
                    value={endDate}
                    onChange={(e) => setEndDate(e.target.value)}
                    required
                />
                <button type="submit">Add</button>
            </form>
            {feedback && <p className={`feedback ${feedback.toLowerCase().includes('error') ? 'error' : 'success'}`}>{feedback}</p>}
            <Link to="/leave-requests-list" className="view-requests-button">View Leave Requests List</Link>
        </div>
    );
}

export default LeaveRequest;
