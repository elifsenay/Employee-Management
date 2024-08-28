import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../../Styles/Employee/EmployeeEntry.css';
import LogoutButton from "../Auth/LogoutButton";
import HomeButton from "../Auth/HomeButton";

function EmployeeEntry() {
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [email, setEmail] = useState('');
    const [department, setDepartment] = useState('');
    const [password, setPassword] = useState('');
    const [errors, setErrors] = useState({});
    const navigate = useNavigate();

    const validateForm = () => {
        const newErrors = {};

        if (!firstName) newErrors.firstName = '* First name is required';
        if (!lastName) newErrors.lastName = '* Last name is required';
        if (!email) newErrors.email = '* Email is required';
        if (!department) newErrors.department = '* Department is required';
        if (!password) newErrors.password = '* Password is required';

        return newErrors;
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        const validationErrors = validateForm();
        if (Object.keys(validationErrors).length > 0) {
            setErrors(validationErrors);
            return;
        }

        const token = localStorage.getItem('token');

        fetch('http://localhost:8080/api/employees', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`,
            },
            body: JSON.stringify({ firstName, lastName, email, department, password }),
        })
            .then(response => {
                if (response.ok) {
                    navigate('/employee-list');
                } else {
                    setErrors({ apiError: 'Failed to add employee' });
                }
            })
            .catch(error => console.error('Error:', error));
    };

    return (
        <div className="employee-entry-container">
            <HomeButton />
            <LogoutButton />
            <h2>Add New Employee</h2>
            <form onSubmit={handleSubmit}>
                <label>
                    First Name
                    <input
                        type="text"
                        value={firstName}
                        onChange={(e) => setFirstName(e.target.value)}
                        placeholder="Please enter first name"
                        style={{ borderColor: errors.firstName ? '#c82333' : '' }}
                    />
                    {errors.firstName && <p className="error-text">{errors.firstName}</p>}
                </label>

                <label>
                    Last Name
                    <input
                        type="text"
                        value={lastName}
                        onChange={(e) => setLastName(e.target.value)}
                        placeholder="Please enter last name"
                        style={{ borderColor: errors.lastName ? 'red' : '' }}
                    />
                    {errors.lastName && <p className="error-text">{errors.lastName}</p>}
                </label>

                <label>
                    Email
                    <input
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        placeholder="Please enter email address"
                        style={{ borderColor: errors.email ? 'red' : '' }}
                    />
                    {errors.email && <p className="error-text">{errors.email}</p>}
                </label>

                <label>
                    Department
                    <input
                        type="text"
                        value={department}
                        onChange={(e) => setDepartment(e.target.value)}
                        placeholder="Please enter department"
                        style={{ borderColor: errors.department ? 'red' : '' }}
                    />
                    {errors.department && <p className="error-text">{errors.department}</p>}
                </label>

                <label>
                    Password
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        placeholder="Please enter password"
                        style={{ borderColor: errors.password ? 'red' : '' }}
                    />
                    {errors.password && <p className="error-text">{errors.password}</p>}
                </label>

                {errors.apiError && <p className="error-text">{errors.apiError}</p>}

                <button type="submit">Add Employee</button>
            </form>
        </div>
    );
}

export default EmployeeEntry;
