import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './EmployeeList.css';

function EmployeeList() {
    const [employees, setEmployees] = useState([]);
    const navigate = useNavigate();

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

    // Function to handle deleting an employee
    const handleDelete = async (employeeId) => {
        const token = localStorage.getItem('token');

        const response = await fetch(`http://localhost:8080/api/employees/${employeeId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${token}`,
            }
        });

        if (response.ok) {
            setEmployees(employees.filter(employee => employee.id !== employeeId));
        } else {
            alert('Failed to delete employee');
        }
    };

    // Function to handle updating an employee (redirect)
    const handleUpdate = (employeeId) => {
        navigate(`/update-employee/${employeeId}`);
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
                        <td className="action-buttons">
                            <button className="update-button" onClick={() => handleUpdate(employee.id)}>Update</button>
                            <button className="delete-button" onClick={() => handleDelete(employee.id)}>Delete</button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
            <button className="new-employee-button" onClick={() => navigate('/employee-entry')}>Create New Employee</button>
        </div>
    );
}

export default EmployeeList;
