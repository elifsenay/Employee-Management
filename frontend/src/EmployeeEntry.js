import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './EmployeeEntry.css';
import LogoutButton from "./LogoutButton";

function EmployeeEntry() {
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [email, setEmail] = useState('');
    const [department, setDepartment] = useState('');
    const [password, setPassword] = useState(''); // Ensure password state is managed
    const navigate = useNavigate();

    const handleSubmit = (e) => {
        e.preventDefault();

        // Ensure all fields are filled, including password
        if (!firstName || !lastName || !email || !department || !password) {
            alert("All fields, including password, must be filled out");
            return;
        }

        const employeeData = {
            firstName,
            lastName,
            email,
            department,
            password // Ensure this is not empty
        };

        const token = localStorage.getItem('token');
        console.log('Employee Data:', employeeData); // Log the payload to be sent


        fetch('http://localhost:8080/api/employees', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`,
            },
            body: JSON.stringify(employeeData),
        })
            .then(response => {
                if (response.ok) {
                    navigate('/employee-list');
                } else {
                    return response.json().then(errorData => {
                        console.error('Error adding employee:', errorData);
                        alert('Failed to add employee: ' + JSON.stringify(errorData));
                    });
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Failed to add employee: ' + error.message);
            });
    };

    return (
        <div className="employee-entry-container">
            <LogoutButton/>
            <h2>Add Employee</h2>
            <form onSubmit={handleSubmit}>
                <label>
                    First Name:
                    <input type="text" value={firstName} onChange={(e) => setFirstName(e.target.value)} />
                </label>
                <br />
                <label>
                    Last Name:
                    <input type="text" value={lastName} onChange={(e) => setLastName(e.target.value)} />
                </label>
                <br />
                <label>
                    Email:
                    <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} />
                </label>
                <br />
                <label>
                    Department:
                    <input type="text" value={department} onChange={(e) => setDepartment(e.target.value)} />
                </label>
                <br />
                <label>
                    Password:
                    <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} />
                </label>
                <br />
                <button type="submit">Add Employee</button>
            </form>
        </div>
    );
}

export default EmployeeEntry;
