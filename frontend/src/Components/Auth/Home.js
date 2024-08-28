// src/Home.js
import React from 'react';
import { Link } from 'react-router-dom';
import LogoutButton from './LogoutButton';
import useBackButtonLogout from './useBackButtonLogout';
import '../../Styles/Auth/Home.css';

function Home() {
    useBackButtonLogout();
    return (
        <div className="home-container">

            <LogoutButton />
            <h1>Employee Management System</h1>
            <nav>
                <ul>
                    <li><Link to="/employee-entry">New Employee</Link></li>
                    <li><Link to="/employee-list">Employee List</Link></li>
                    <li><Link to="/leave-requests">New Leave Request</Link></li>
                    <li><Link to="/leave-requests-list">Leave Requests List</Link></li>
                </ul>
            </nav>
        </div>
    );
}

export default Home;
