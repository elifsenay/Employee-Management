// src/App.js
import React from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import Home from './Home';
import EmployeeEntry from './EmployeeEntry';
import EmployeeList from './EmployeeList';
import LeaveRequest from './LeaveRequest';
import EmployeeLogin from "./EmployeeLogin";
import './App.css';

function App() {
    return (
        <Router>
            <div className="App">
                <Routes>
                    <Route path="/" element={<Navigate to="/login" />} />
                    <Route path="/login" element={<EmployeeLogin />} />
                    <Route path="/home" element={<Home />} />
                    <Route path="/employee-entry" element={<EmployeeEntry />} />
                    <Route path="/employee-list" element={<EmployeeList />} />
                    <Route path="/leave-requests" element={<LeaveRequest />} />
                </Routes>
            </div>
        </Router>
    );
}

export default App;
