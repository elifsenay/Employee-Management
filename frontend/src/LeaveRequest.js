import React, { useState, useEffect } from 'react';
import Select from 'react-select';
import { Link } from 'react-router-dom';
import './LeaveRequest.css';
import LogoutButton from "./LogoutButton";
import HomeButton from "./HomeButton";

function LeaveRequest() {
    const [selectedEmployee, setSelectedEmployee] = useState(null);
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

        if (!selectedEmployee) {
            setFeedback('Please select an employee.');
            return;
        }

        fetch('http://localhost:8080/api/leaverequests', {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                employeeId: selectedEmployee.value.id,
                startDate: new Date(startDate).toISOString().split('T')[0],
                endDate: new Date(endDate).toISOString().split('T')[0],
            }),
        })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(err => { throw new Error(err.message); });
                }
                return response.json();
            })
            .then(() => {
                setFeedback('Leave request added successfully!');
                setSelectedEmployee(null);
                setStartDate('');
                setEndDate('');
            })
            .catch((error) => {
                setFeedback(`Error adding leave request: ${error.message}`);
                console.error('Error:', error);
            });
    };

    return (
        <div className="leave-request-container">
            <HomeButton/>
            <LogoutButton />
            <h2>Add Leave Request</h2>
            <form onSubmit={handleSubmit}>
                <label>Employee:</label>
                <Select
                    value={selectedEmployee}
                    onChange={setSelectedEmployee}
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
            <Link to="/leave-requests-list" className="view-requests-button">View Leave Requests List</Link>
        </div>
    );
}

export default LeaveRequest;
