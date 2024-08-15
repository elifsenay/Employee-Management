import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import './UpdateEmployee.css';

function UpdateEmployee() {
    const { id } = useParams();
    const [employee, setEmployee] = useState({
        firstName: '',
        lastName: '',
        email: '',
        department: '',
    });
    const token = localStorage.getItem('token');
    const navigate = useNavigate();

    useEffect(() => {
        fetch(`http://localhost:8080/api/employees/${id}`, {
            headers: {
                'Authorization': `Bearer ${token}`,
            }
        })
            .then(response => response.json())
            .then(data => setEmployee(data))
            .catch(error => console.error('Error fetching employee:', error));
    }, [id, token]);

    const handleChange = (e) => {
        setEmployee({
            ...employee,
            [e.target.name]: e.target.value,
        });
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        fetch(`http://localhost:8080/api/employees/${id}`, {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(employee),
        })
            .then(response => {
                if (response.ok) {
                    navigate('/employee-list');
                } else {
                    alert('Failed to update employee');
                }
            })
            .catch(error => console.error('Error updating employee:', error));
    };

    return (
        <div className="update-employee-container">
            <h2>Update Employee</h2>
            <form onSubmit={handleSubmit}>
                <label>
                    First Name:
                    <input
                        type="text"
                        name="firstName"
                        value={employee.firstName}
                        onChange={handleChange}
                    />
                </label>
                <label>
                    Last Name:
                    <input
                        type="text"
                        name="lastName"
                        value={employee.lastName}
                        onChange={handleChange}
                    />
                </label>
                <label>
                    Email:
                    <input
                        type="email"
                        name="email"
                        value={employee.email}
                        onChange={handleChange}
                    />
                </label>
                <label>
                    Department:
                    <input
                        type="text"
                        name="department"
                        value={employee.department}
                        onChange={handleChange}
                    />
                </label>
                <button type="submit">Update Employee</button>
            </form>
        </div>
    );
}

export default UpdateEmployee;
