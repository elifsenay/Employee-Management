import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Home from './Home';
import EmployeeEntry from './EmployeeEntry';
import EmployeeList from './EmployeeList';
import LeaveRequest from './LeaveRequest';
import UpdateEmployee from './UpdateEmployee';
import UpdateLeaveRequest from './UpdateLeaveRequest';
import EmployeeLogin from './EmployeeLogin';
import './App.css';
import LeaveRequestsList from "./LeaveRequestsList";

function App() {
    return (
        <Router>
            <div className="App">
                <Routes>
                    <Route path="/" element={<EmployeeLogin />} />
                    <Route path="/home" element={<Home />} />
                    <Route path="/employee-entry" element={<EmployeeEntry />} />
                    <Route path="/employee-list" element={<EmployeeList />} />
                    <Route path="/leave-requests" element={<LeaveRequest />} />
                    <Route path="/update-employee/:id" element={<UpdateEmployee />} />
                    <Route path="/update-leave-request/:id" element={<UpdateLeaveRequest />} />
                    <Route path="/leave-requests-list" element={<LeaveRequestsList />} />
                </Routes>
            </div>
        </Router>
    );
}

export default App;
