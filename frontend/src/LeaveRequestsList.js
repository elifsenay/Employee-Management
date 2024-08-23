import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './LeaveRequestsList.css';
import LogoutButton from "./LogoutButton";
import HomeButton from "./HomeButton";
import viewIcon from './view-files.png';

function LeaveRequestsList() {
    const [leaveRequests, setLeaveRequests] = useState([]);
    const [employees, setEmployees] = useState([]);
    const [searchTerm, setSearchTerm] = useState('');
    const token = localStorage.getItem('token');
    const navigate = useNavigate();

    useEffect(() => {
        const fetchData = async () => {
            try {
                const leaveRequestsResponse = await fetch('http://localhost:8080/api/leaverequests', {
                    headers: { 'Authorization': `Bearer ${token}` },
                });
                const employeesResponse = await fetch('http://localhost:8080/api/employees', {
                    headers: { 'Authorization': `Bearer ${token}` },
                });

                if (leaveRequestsResponse.ok && employeesResponse.ok) {
                    const leaveRequestsData = await leaveRequestsResponse.json();
                    const employeesData = await employeesResponse.json();

                    setLeaveRequests(leaveRequestsData);
                    setEmployees(employeesData);
                } else {
                    console.error('Failed to fetch data');
                }
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };

        fetchData();
    }, [token]);

    const filteredLeaveRequests = leaveRequests.filter(request => {
        const employee = employees.find(emp => emp.id === request.employeeId);
        const employeeName = employee ? `${employee.firstName} ${employee.lastName}` : '';
        return employeeName.toLowerCase().includes(searchTerm.toLowerCase()) ||
            request.startDate.includes(searchTerm) ||
            request.endDate.includes(searchTerm);
    });

    const getEmployeeName = (employeeId) => {
        const employee = employees.find(emp => emp.id === employeeId);
        return employee ? `${employee.firstName} ${employee.lastName}` : 'N/A';
    };

    const getRemainingLeaveDays = (employeeId) => {
        const employee = employees.find(emp => emp.id === employeeId);
        return employee ? employee.remainingLeaveDays : 'N/A';
    };

    const handleUpdate = (id) => {
        navigate(`/update-leave-request/${id}`);
    };

    const handleDelete = async (id) => {
        try {
            const response = await fetch(`http://localhost:8080/api/leaverequests/${id}`, {
                method: 'DELETE',
                headers: { 'Authorization': `Bearer ${token}` },
            });

            if (response.ok) {
                setLeaveRequests(leaveRequests.filter(request => request.id !== id));
            } else {
                alert('Failed to delete leave request');
            }
        } catch (error) {
            console.error('Error deleting leave request:', error);
        }
    };

    const handleUploadDocument = (leaveRequestId) => {
        navigate(`/upload-document/${leaveRequestId}`);
    };

    const handleViewDocument = (leaveRequestId, documentPath) => {
        window.open(`http://localhost:8080/api/leaverequests/${leaveRequestId}/document?path=${documentPath}`, '_blank');
    };

    return (
        <div className="leave-requests-container">
            <HomeButton/>
            <LogoutButton />
            <h2>Leave Requests</h2>
            <input
                type="text"
                placeholder="Search by employee name or date"
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="search-bar"
            />
            <table className="leave-requests-table">
                <thead>
                <tr>
                    <th>Employee</th>
                    <th>Remaining Leave Days</th>
                    <th>Start Date</th>
                    <th>End Date</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {filteredLeaveRequests.length > 0 && filteredLeaveRequests.map(request => (
                    <tr key={request.id}>
                        <td>{getEmployeeName(request.employeeId)}</td>
                        <td>{getRemainingLeaveDays(request.employeeId)}</td>
                        <td>{request.startDate}</td>
                        <td>{request.endDate}</td>
                        <td className="actions">
                            <button className="update-button" onClick={() => handleUpdate(request.id)}>Update</button>
                            <button className="delete-button" onClick={() => handleDelete(request.id)}>Delete</button>
                            <button className="upload-button" onClick={() => handleUploadDocument(request.id)}>Upload Document</button>
                            {request.documentPath && (
                                <button className="view-button" onClick={() => handleViewDocument(request.id)}>
                                    <img src={viewIcon} alt="View Document" />
                                </button> )}
                            </td>
                    </tr>
                ))}
                </tbody>
            </table>
            <button className="new-leave-request-button" onClick={() => navigate('/leave-requests')}>
                Create New Leave Request
            </button>
        </div>
    );
}

export default LeaveRequestsList;
