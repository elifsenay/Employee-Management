import React, { useState } from 'react';
import axios from 'axios';
import './Form.css';

function EmployeeEntry() {
    const [employee, setEmployee] = useState({
        firstName: '',
        lastName: '',
        email: '',
        department: ''
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setEmployee({ ...employee, [name]: value });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        axios.post('/api/employees', employee)
            .then(response => {
                console.log(response.data);
                // Handle success
            })
            .catch(error => {
                console.error(error);
                // Handle error
            });
    };

    return (
        <div className="form-container">
            <h1>Employee Entry</h1>
            <form onSubmit={handleSubmit}>
                <label>
                    First Name:
                    <input type="text" name="firstName" value={employee.firstName} onChange={handleChange} />
                </label>
                <label>
                    Last Name:
                    <input type="text" name="lastName" value={employee.lastName} onChange={handleChange} />
                </label>
                <label>
                    Email:
                    <input type="email" name="email" value={employee.email} onChange={handleChange} />
                </label>
                <label>
                    Department:
                    <input type="text" name="department" value={employee.department} onChange={handleChange} />
                </label>
                <button type="submit">Submit</button>
            </form>
        </div>
    );
}

export default EmployeeEntry;
