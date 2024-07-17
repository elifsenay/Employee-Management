import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import EmployeeEntry from './EmployeeEntry';
import LeaveEntry from './LeaveEntry';
import EmployeeList from './EmployeeList';
import './App.css';

function App() {
    return (
        <Router>
            <div className="App">
                <Routes>
                    <Route path="/employee-entry" element={<EmployeeEntry />} />
                    <Route path="/leave-entry" element={<LeaveEntry />} />
                    <Route path="/employee-list" element={<EmployeeList />} />
                </Routes>
            </div>
        </Router>
    );
}

export default App;

