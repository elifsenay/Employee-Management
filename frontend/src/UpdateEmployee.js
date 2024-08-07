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
        remainingLeaveDays: 0,
        password: '',
    });

    useEffect(() => {
        const fetchEmployee = async () => {
            const token = localStorage.getItem('token');

            const response = await fetch(`http://localhost:8080/api/employees/${id}`, {
                headers: {
                    'Authorization': `Bearer ${token}`,
                }
            });

            if (response.ok) {
                const data = await response.json();
                setEmployee(data);
            } else {
                alert('Failed to fetch employee');
            }
        };

        fetchEmployee();
    }, [id]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setEmployee((prevState) => ({
            ...prevState,
            [name]: value,
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const token = localStorage.getItem('token');

        const response = await fetch(`http://localhost:8080/api/employees/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`,
            },
            body: JSON.stringify(employee),
        });

        if (response.ok) {
            navigate('/employee-list');
        } else {
            alert('Failed to update employee');
        }
    };

    return (
        <div className="employee-entry-container">
            <h2>Update Employee</h2>
            <form onSubmit={handleSubmit}>
                <label>
                    First Name:
                    <input type="text" name="firstName" value={employee.firstName} onChange={handleChange} />
                </label>
                <br />
                <label>
                    Last Name:
                    <input type="text" name="lastName" value={employee.lastName} onChange={handleChange} />
                </label>
                <br />
                <label>
                    Email:
                    <input type="email" name="email" value={employee.email} onChange={handleChange} />
                </label>
                <br />
                <label>
                    Department:
                    <input type="text" name="department" value={employee.department} onChange={handleChange} />
                </label>
                <br />
                <label>
                    Remaining Leave Days:
                    <input type="number" name="remainingLeaveDays" value={employee.remainingLeaveDays} onChange={handleChange} />
                </label>
                <br />
                <label>
                    Password:
                    <input type="password" name="password" value={employee.password} onChange={handleChange} />
                </label>
                <br />
                <button type="submit">Update Employee</button>
            </form>
        </div>
    );
}

export default UpdateEmployee;
