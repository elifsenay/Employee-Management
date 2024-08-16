// src/Home.js
import React from 'react';
import { Link } from 'react-router-dom';
import './Home.css';

function Home() {
    return (
        <div className="home-container">
            <h1>Employee Management System</h1>
            <nav>
                <ul>
                    <li><Link to="/employee-entry">Add Employee</Link></li>
                    <li><Link to="/employee-list">Employee List</Link></li>
                    <li><Link to="/leave-requests">Leave Requests</Link></li>
                    <li><Link to="/leave-requests-list">Leave Requests List</Link></li>
                </ul>
            </nav>
        </div>
    );
}

export default Home;
