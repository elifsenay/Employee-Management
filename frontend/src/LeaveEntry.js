import React, { useState } from 'react';
import axios from 'axios';
import './Form.css';

function LeaveEntry() {
    const [leave, setLeave] = useState({
        employeeId: '',
        leaveDays: ''
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setLeave({ ...leave, [name]: value });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        axios.post('/api/leaves', leave)
            .then(response => {
                console.log(response.data);
                // Handle success
            })
            .catch(error => {
                console.error(error);
                // Handle error
            });
    };

    return (
        <div className="form-container">
            <h1>Leave Entry</h1>
            <form onSubmit={handleSubmit}>
                <label>
                    Employee ID:
                    <input type="text" name="employeeId" value={leave.employeeId} onChange={handleChange} />
                </label>
                <label>
                    Leave Days:
                    <input type="number" name="leaveDays" value={leave.leaveDays} onChange={handleChange} />
                </label>
                <button type="submit">Submit</button>
            </form>
        </div>
    );
}

export default LeaveEntry;
