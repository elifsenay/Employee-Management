import React, { useState, useEffect } from 'react';
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

    const handleSubmit = (e) => {
        e.preventDefault();

        if (!employee) {
            setFeedback('Please select an employee.');
            return;
        }

        const leaveRequestPayload = {
            employee: { id: employee.value.id },
            startDate,
            endDate
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
                />
                <label>Start Date:</label>
                <input type="date" value={startDate} onChange={(e) => setStartDate(e.target.value)} />
                <label>End Date:</label>
                <input type="date" value={endDate} onChange={(e) => setEndDate(e.target.value)} />
                <button type="submit">Add</button>
            </form>
            {feedback && <p className="feedback">{feedback}</p>}
        </div>
    );
}

export default LeaveRequest;
