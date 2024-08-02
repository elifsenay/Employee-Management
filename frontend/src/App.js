import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import Home from './Home';
import EmployeeEntry from './EmployeeEntry';
import EmployeeList from './EmployeeList';
import LeaveRequest from './LeaveRequest';
import UpdateEmployee from './UpdateEmployee';
import LoginPage from './LoginPage';
import AdminDashboard from './AdminDashboard';
import EmployeeDashboard from './EmployeeDashboard';
import './App.css';

// Role-based Private Route Component
const PrivateRoute = ({ element, roles }) => {
    const userRole = localStorage.getItem('userRole');
    if (userRole && roles.includes(userRole)) {
        return element;
    }
    return <Navigate to="/login" />;
};

function App() {
    const [role, setRole] = useState(null);

    useEffect(() => {
        setRole(localStorage.getItem('userRole'));
    }, []);

    return (
        <Router>
            <div className="App">
                <Routes>
                    <Route path="/" element={<LoginPage />} />
                    <Route path="/home" element={<Home />} />
                    <Route path="/employee-entry" element={<PrivateRoute element={<EmployeeEntry />} roles={['EMPLOYEE', 'ADMIN']} />} />
                    <Route path="/employee-list" element={<PrivateRoute element={<EmployeeList />} roles={['EMPLOYEE', 'ADMIN']} />} />
                    <Route path="/leave-requests" element={<PrivateRoute element={<LeaveRequest />} roles={['EMPLOYEE', 'ADMIN']} />} />
                    <Route path="/update-employee/:id" element={<PrivateRoute element={<UpdateEmployee />} roles={['ADMIN']} />} />
                    <Route path="/admin/dashboard" element={<PrivateRoute element={<AdminDashboard />} roles={['ADMIN']} />} />
                    <Route path="/employee/dashboard" element={<PrivateRoute element={<EmployeeDashboard />} roles={['EMPLOYEE', 'ADMIN']} />} />
                </Routes>
            </div>
        </Router>
    );
}

export default App;
