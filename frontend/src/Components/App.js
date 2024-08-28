import React from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import Home from './Auth/Home';
import EmployeeEntry from './Employee/EmployeeEntry';
import EmployeeList from './Employee/EmployeeList';
import LeaveRequest from './LeaveRequest/LeaveRequest';
import EmployeeLogin from "./Employee/EmployeeLogin";
import UpdateEmployee from './Employee/UpdateEmployee';
import LeaveRequestsList from './LeaveRequest/LeaveRequestsList';
import UpdateLeaveRequest from "./LeaveRequest/UpdateLeaveRequest";
import PrivateRoute from './Auth/PrivateRoute';
import '../Styles/App.css';
import ChangePassword from "./Auth/ChangePassword";
import UploadDocument from "./LeaveRequest/UploadDocument";

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
