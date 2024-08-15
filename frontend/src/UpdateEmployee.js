// src/UpdateEmployee.js
import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import './EmployeeEntry.css';

function UpdateEmployee() {
    const { id } = useParams();
    const navigate = useNavigate();
    const [employee, setEmployee] = useState({
        firstName: '',
        lastName: '',
        email: '',
        department: '',
        remainingLeaveDays: 0
    });
    const [feedback, setFeedback] = useState('');

    useEffect(() => {
        fetch(`http://localhost:8080/api/employees/${id}`)
            .then(response => response.json())
            .then(data => setEmployee(data))
            .catch(error => console.error('Error fetching employee:', error));
    }, [id]);

    const handleSubmit = (e) => {
        e.preventDefault();
        fetch(`http://localhost:8080/api/employees/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(employee),
        })
            .then(response => {
                if (response.ok) {
                    setFeedback('Employee updated successfully!');
                    setTimeout(() => navigate('/employee-list'), 2000);
                } else {
                    setFeedback('Error updating employee. Please try again.');
                }
            })
            .catch(error => {
                setFeedback('Error updating employee. Please try again.');
                console.error('Error:', error);
            });
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setEmployee(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    return (
        <div className="employee-entry-container">
            <h2>Update Employee</h2>
            <form onSubmit={handleSubmit}>
                <label>First Name:</label>
                <input type="text" name="firstName" value={employee.firstName} onChange={handleChange} />
                <label>Last Name:</label>
                <input type="text" name="lastName" value={employee.lastName} onChange={handleChange} />
                <label>Email:</label>
                <input type="email" name="email" value={employee.email} onChange={handleChange} />
                <label>Department:</label>
                <input type="text" name="department" value={employee.department} onChange={handleChange} />
                <label>Remaining Leave Days:</label>
                <input type="number" name="remainingLeaveDays" value={employee.remainingLeaveDays} onChange={handleChange} />
                <button type="submit">Update</button>
            </form>
            {feedback && <p className="feedback">{feedback}</p>}
        </div>
    );
}

export default UpdateEmployee;
