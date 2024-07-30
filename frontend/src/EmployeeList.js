// src/EmployeeList.js
import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './EmployeeList.css';

function EmployeeList() {
    const [employees, setEmployees] = useState([]);
    const navigate = useNavigate();

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

    const handleDelete = (id) => {
        fetch(`http://localhost:8080/api/employees/${id}`, {
            method: 'DELETE',
            headers: {
                'Authorization': 'Basic ' + btoa('user:password') // Add your username and password
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                setEmployees(employees.filter(employee => employee.id !== id));
            })
            .catch(error => console.error('Error deleting employee:', error));
    };

    const handleUpdate = (id) => {
        navigate(`/update-employee/${id}`);
    };

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
                    <th>Actions</th>
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
                        <td>
                            <div className="action-buttons">
                                <button onClick={() => handleUpdate(employee.id)}>Update</button>
                                <button onClick={() => handleDelete(employee.id)}>Delete</button>
                            </div>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}

export default EmployeeList;
