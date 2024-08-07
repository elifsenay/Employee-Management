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

    const handleDelete = async (id) => {
        const token = localStorage.getItem('token');

        const response = await fetch(`http://localhost:8080/api/employees/${id}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${token}`,
            }
        });

        if (response.ok) {
            setEmployees(employees.filter(employee => employee.id !== id));
        } else {
            alert('Failed to delete employee');
        }
    };

    const handleUpdate = (id) => {
        navigate(`/update-employee/${id}`);
    };

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
                            <button onClick={() => handleUpdate(employee.id)}>Update</button>
                            <button onClick={() => handleDelete(employee.id)}>Delete</button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}

export default EmployeeList;
