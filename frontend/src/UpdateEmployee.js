import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import './UpdateEmployee.css';
import LogoutButton from "./LogoutButton";

function UpdateEmployee() {
    const [employee, setEmployee] = useState({
        firstName: '',
        lastName: '',
        email: '',
        department: '',
    });
    const { id } = useParams();
    const navigate = useNavigate();

    useEffect(() => {
        const fetchEmployee = async () => {
            const token = localStorage.getItem('token');

            const response = await fetch(`http://localhost:8080/api/employees/${id}`, {
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
            });

            if (response.ok) {
                const data = await response.json();
                setEmployee(data);
            } else {
                alert('Failed to fetch employee data');
            }
        };

        fetchEmployee();
    }, [id]);

    const handleSubmit = (e) => {
        e.preventDefault();

        const token = localStorage.getItem('token');

        fetch(`http://localhost:8080/api/employees/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`,
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
            .catch(error => console.error('Error:', error));
    };

    const handleChange = (e) => {
        setEmployee({
            ...employee,
            [e.target.name]: e.target.value
        });
    };

    const handleCancel = () => {
        navigate('/employee-list'); // Redirects to the employee list page
    };

    return (
        <div className="update-employee-container">
            <LogoutButton />
            <h2>Update Employee</h2>
            <form onSubmit={handleSubmit} autoComplete="off">
                <label>
                    First Name:
                    <input
                        type="text"
                        name="firstName"
                        value={employee.firstName}
                        onChange={handleChange}
                        placeholder="Enter first name"
                        autoComplete="off"
                    />
                </label>

                <label>
                    Last Name:
                    <input
                        type="text"
                        name="lastName"
                        value={employee.lastName}
                        onChange={handleChange}
                        placeholder="Enter last name"
                        autoComplete="off"
                    />
                </label>

                <label>
                    Email:
                    <input
                        type="email"
                        name="email"
                        value={employee.email}
                        onChange={handleChange}
                        placeholder="Enter email address"
                        autoComplete="off"
                    />
                </label>

                <label>
                    Department:
                    <input
                        type="text"
                        name="department"
                        value={employee.department}
                        onChange={handleChange}
                        placeholder="Enter department"
                        autoComplete="off"
                    />
                </label>

                <div className="button-group">
                    <button type="submit" className="update-button">Update Employee</button>
                    <button type="button" className="cancel-button" onClick={handleCancel}>Cancel</button>
                </div>
            </form>
        </div>
    );
}

export default UpdateEmployee;
