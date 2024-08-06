// src/EmployeeList.js
import React, { useEffect, useState } from 'react';
import './EmployeeList.css';

function EmployeeList() {
    const [employees, setEmployees] = useState([]);

    useEffect(() => {
        const fetchEmployees = async () => {
            const token = localStorage.getItem('token');

            const response = await fetch('http://localhost:8080/api/employees', {
                headers: {
                    'Authorization': `Bearer ${token}`,
                }
            });

            if (response.ok) {
                const data = await response.json();
                setEmployees(data);
            } else {
                alert('Failed to fetch employees');
            }
        };

        fetchEmployees();
    }, []);

    return (
        <div className="employee-list-container">
            <h2>Employee List</h2>
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Email</th>
                    <th>Department</th>
                    <th>Remaining Leave Days</th>
                </tr>
                </thead>
                <tbody>
                {employees.map(employee => (
                    <tr key={employee.id}>
                        <td>{employee.id}</td>
                        <td>{employee.firstName}</td>
                        <td>{employee.lastName}</td>
                        <td>{employee.email}</td>
                        <td>{employee.department}</td>
                        <td>{employee.remainingLeaveDays}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}

export default EmployeeList;
