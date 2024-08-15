import React, { useState, useEffect } from 'react';
import Select from 'react-select';
import { useNavigate, useParams } from 'react-router-dom';
import './UpdateLeaveRequest.css';

function UpdateLeaveRequest() {
    const { id } = useParams();
    const [employee, setEmployee] = useState(null);
    const [employees, setEmployees] = useState([]);
    const [startDate, setStartDate] = useState('');
    const [endDate, setEndDate] = useState('');
    const [feedback, setFeedback] = useState('');
    const token = localStorage.getItem('token');
    const navigate = useNavigate();

    useEffect(() => {
        fetch('http://localhost:8080/api/leaverequests/' + id, {
            headers: {
                'Authorization': `Bearer ${token}`,
            }
        })
            .then(response => response.json())
            .then(data => {
                setEmployee({ value: data.employee, label: `${data.employee.firstName} ${data.employee.lastName} (ID: ${data.employee.id})` });
                setStartDate(data.startDate);
                setEndDate(data.endDate);
            })
            .catch(error => console.error('Error fetching leave request:', error));
    }, [id, token]);

    useEffect(() => {
        fetch('http://localhost:8080/api/employees', {
            headers: {
                'Authorization': `Bearer ${token}`,
            }
        })
            .then(response => response.json())
            .then(data => setEmployees(data))
            .catch(error => console.error('Error fetching employees:', error));
    }, [token]);

    const handleSubmit = (e) => {
        e.preventDefault();

        if (!employee) {
            setFeedback('Please select an employee.');
            return;
        }

        const startDateObj = new Date(startDate);
        const endDateObj = new Date(endDate);

        const leaveRequestPayload = {
            employee: { id: employee.value.id },
            startDate: startDateObj.toISOString().split('T')[0],
            endDate: endDateObj.toISOString().split('T')[0]
        };

        fetch('http://localhost:8080/api/leaverequests/' + id, {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(leaveRequestPayload),
        })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(err => { throw new Error(err.message); });
                }
                setFeedback('Leave request updated successfully!');
                navigate('/leave-requests-list');
            })
            .catch(error => {
                setFeedback(`Error updating leave request: ${error.message}`);
                console.error('Error:', error);
            });
    };

    return (
        <div className="update-leave-request-container">
            <h2>Update Leave Request</h2>
            <form onSubmit={handleSubmit}>
                <label>Employee:</label>
                <Select
                    value={employee}
                    onChange={(selectedOption) => setEmployee(selectedOption)}
                    options={employees.map(emp => ({
                        value: emp,
                        label: `${emp.firstName} ${emp.lastName} (ID: ${emp.id})`
                    }))}
                    placeholder="Select an employee"
                />
                <label>Start Date:</label>
                <input type="date" value={startDate} onChange={(e) => setStartDate(e.target.value)} />
                <label>End Date:</label>
                <input type="date" value={endDate} onChange={(e) => setEndDate(e.target.value)} />
                <button type="submit">Update</button>
            </form>
            {feedback && <p className="feedback">{feedback}</p>}
        </div>
    );
}

export default UpdateLeaveRequest;
