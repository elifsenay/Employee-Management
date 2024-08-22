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
    const [errors, setErrors] = useState({});
    const token = localStorage.getItem('token');

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

    const validateForm = () => {
        const newErrors = {};
        if (!selectedEmployee) newErrors.employee = 'Please select an employee';
        if (!startDate) newErrors.startDate = '* Start date is required';
        if (!endDate) newErrors.endDate = '* End date is required';
        if (endDate && startDate && new Date(endDate) < new Date(startDate)) {
            newErrors.endDate = '* End date cannot be before start date';
        }
        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        if (!validateForm()) return;

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
                setErrors({})
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
                <label>
                    Employee:
                    <Select
                        value={selectedEmployee}
                        onChange={setSelectedEmployee}
                        options={employees.map(emp => ({
                            value: emp,
                            label: `${emp.firstName} ${emp.lastName} (ID: ${emp.id})`
                        }))}
                        placeholder="Select an employee"
                        styles={{
                            control: (base) => ({
                                ...base,
                                borderColor: errors.employee ? 'red' : base.borderColor,
                            })
                        }}
                    />
                    {errors.employee && <p className="error-text">{errors.employee}</p>}
                </label>

                <label>
                    Start Date:
                    <input
                        type="date"
                        value={startDate}
                        onChange={(e) => setStartDate(e.target.value)}
                        style={{ borderColor: errors.startDate ? 'red' : '' }}
                    />
                    {errors.startDate && <p className="error-text">{errors.startDate}</p>}
                </label>

                <label>
                    End Date:
                    <input
                        type="date"
                        value={endDate}
                        onChange={(e) => setEndDate(e.target.value)}
                        style={{ borderColor: errors.endDate ? 'red' : '' }}
                    />
                    {errors.endDate && <p className="error-text">{errors.endDate}</p>}
                </label>

                <button type="submit">Add</button>
            </form>
            {feedback && !errors.apiError && <p className="feedback">{feedback}</p>}
            {errors.apiError && <p className="error-text">{errors.apiError}</p>}
            <Link to="/leave-requests-list" className="view-requests-button">View Leave Requests List</Link>
        </div>
    );
}

export default LeaveRequest;