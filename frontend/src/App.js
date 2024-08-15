import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import EmployeeEntry from './EmployeeEntry';
import EmployeeList from './EmployeeList';
import EmployeeLogin from './EmployeeLogin';
import Home from './Home';
import LeaveRequest from './LeaveRequest';
<<<<<<< Updated upstream
import LeaveRequestsList from './LeaveRequestsList';
import UpdateEmployee from './UpdateEmployee';
import UpdateLeaveRequest from './UpdateLeaveRequest';
=======
import EmployeeLogin from "./EmployeeLogin";
import './App.css';
import LeaveRequestsList from "./LeaveRequestsList";
>>>>>>> Stashed changes

function App() {
    return (
        <Router>
<<<<<<< Updated upstream
            <Routes>
                <Route path="/" element={<EmployeeLogin />} />
                <Route path="/employee-entry" element={<EmployeeEntry />} />
                <Route path="/employee-list" element={<EmployeeList />} />
                <Route path="/home" element={<Home />} />
                <Route path="/leave-requests" element={<LeaveRequest />} />
                <Route path="/leave-requests-list" element={<LeaveRequestsList />} />
                <Route path="/update-employee/:id" element={<UpdateEmployee />} />
                <Route path="/update-leave-request/:id" element={<UpdateLeaveRequest />} />
            </Routes>
=======
            <div className="App">
                <Routes>
                    <Route path="/" element={<Navigate to="/login" />} />
                    <Route path="/login" element={<EmployeeLogin />} />
                    <Route path="/home" element={<Home />} />
                    <Route path="/employee-entry" element={<EmployeeEntry />} />
                    <Route path="/employee-list" element={<EmployeeList />} />
                    <Route path="/leave-requests" element={<LeaveRequest />} />
                    <Route path="/leave-requests-list" element={<LeaveRequestsList />} />
                </Routes>
            </div>
>>>>>>> Stashed changes
        </Router>
    );
}

export default App;
