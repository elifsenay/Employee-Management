import React from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import Home from './Home';
import EmployeeEntry from './EmployeeEntry';
import EmployeeList from './EmployeeList';
import LeaveRequest from './LeaveRequest';
import EmployeeLogin from "./EmployeeLogin";
import UpdateEmployee from './UpdateEmployee';
import LeaveRequestsList from './LeaveRequestsList';
import UpdateLeaveRequest from "./UpdateLeaveRequest";
import PrivateRoute from './PrivateRoute';
import './App.css';
import ChangePassword from "./ChangePassword";
import UploadDocument from "./UploadDocument";

function App() {
    return (
        <Router>
            <div className="App">
                <Routes>
                    <Route path="/" element={<Navigate to="/login" />} />
                    <Route path="/login" element={<EmployeeLogin />} />

                    <Route path="/home" element={
                        <PrivateRoute>
                            <Home />
                        </PrivateRoute>
                    } />
                    <Route path="/employee-entry" element={
                        <PrivateRoute>
                            <EmployeeEntry />
                        </PrivateRoute>
                    } />
                    <Route path="/employee-list" element={
                        <PrivateRoute>
                            <EmployeeList />
                        </PrivateRoute>
                    } />
                    <Route path="/leave-requests" element={
                        <PrivateRoute>
                            <LeaveRequest />
                        </PrivateRoute>
                    } />
                    <Route path="/update-employee/:id" element={
                        <PrivateRoute>
                            <UpdateEmployee />
                        </PrivateRoute>
                    } />
                    <Route path="/leave-requests-list" element={
                        <PrivateRoute>
                            <LeaveRequestsList />
                        </PrivateRoute>
                    } />
                    <Route path="/update-leave-request/:id" element={
                        <PrivateRoute>
                            <UpdateLeaveRequest />
                        </PrivateRoute>
                    } />
                    <Route path="/change-password/:employeeId" element={
                        <PrivateRoute>
                            <ChangePassword />
                        </PrivateRoute>
                    } />
                    <Route path="/upload-document/:leaveRequestId" element={
                        <PrivateRoute>
                            <UploadDocument />
                        </PrivateRoute>
                    } />
                </Routes>
            </div>
        </Router>
    );
}

export default App;
