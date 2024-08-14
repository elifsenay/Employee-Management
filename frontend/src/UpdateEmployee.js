import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import './UpdateEmployee.css';

function UpdateEmployee() {
    const { id } = useParams();
    const [employee, setEmployee] = useState({
        firstName: '',
        lastName: '',
        email: '',
        department: ''
    });
    const token = localStorage.getItem('token');
    const navigate = useNavigate();

    useEffect(() => {
        const fetchEmployee = async () => {
            try {
                const response = await fetch(`http://localhost:8080/api/employees/${id}`, {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                });

                if (response.ok) {
                    const data = await response.json();
                    setEmployee({
                        firstName: data.firstName || '',
                        lastName: data.lastName || '',
                        email: data.email || '',
                        department: data.department || ''
                    });
                } else {
                    alert('Failed to fetch employee details');
                }
            } catch (error) {
                console.error('Error fetching employee details:', error);
            }
        };

        fetchEmployee();
    }, [id, token]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setEmployee((prevEmployee) => ({
            ...prevEmployee,
            [name]: value
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const response = await fetch(`http://localhost:8080/api/employees/${id}`, {
                method: 'PUT',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(employee),
            });

            if (response.ok) {
                navigate('/employee-list');
            } else {
                alert('Failed to update employee details');
            }
        } catch (error) {
            console.error('Error updating employee details:', error);
            alert('Failed to update employee details');
        }
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
                <br />
                <label>
                    Last Name:
                    <input
                        type="text"
                        name="lastName"
                        value={employee.lastName}
                        onChange={handleChange}
                    />
                </label>
                <br />
                <label>
                    Email:
                    <input
                        type="email"
                        name="email"
                        value={employee.email}
                        onChange={handleChange}
                    />
                </label>
                <br />
                <label>
                    Department:
                    <input
                        type="text"
                        name="department"
                        value={employee.department}
                        onChange={handleChange}
                    />
                </label>
                <br />
                <button type="submit">Update</button>
            </form>
        </div>
    );
}

export default UpdateEmployee;
