// src/EmployeeList.js
import React, { useEffect, useState } from 'react';
import './EmployeeList.css';

function EmployeeList() {
    const [employees, setEmployees] = useState([]);

    useEffect(() => {
        fetch('http://localhost:8080/api/employees', {
            headers: {
                'Authorization': 'Basic ' + btoa('user:password') // Add your username and password
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => setEmployees(data))
            .catch(error => console.error('Error fetching employees:', error));
    }, []);

    return (
        <div className="employee-list-container">
            <h2>Employee List</h2>
            <table className="employee-list-table">
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
