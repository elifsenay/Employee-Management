// src/App.js
import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Home from './Home';
import EmployeeEntry from './EmployeeEntry';
import EmployeeList from './EmployeeList';
import LeaveRequest from './LeaveRequest';
import UpdateEmployee from './UpdateEmployee';
import './App.css';

function App() {
    return (
        <Router>
            <div className="App">
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/employee-entry" element={<EmployeeEntry />} />
                    <Route path="/employee-list" element={<EmployeeList />} />
                    <Route path="/leave-requests" element={<LeaveRequest />} />
                    <Route path="/update-employee/:id" element={<UpdateEmployee />} />
                </Routes>
            </div>
        </Router>
    );
}

export default App;
