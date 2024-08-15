import React from 'react';
import './Home.css';

function Home() {
    return (
        <div className="home-container">
            <h1>Employee Management System</h1>
            <div className="links-container">
                <a href="/employee-entry">Add Employee</a>
                <a href="/employee-list">Employee List</a>
                <a href="/leave-requests">Leave Requests</a>
            </div>
        </div>
    );
}

export default Home;
