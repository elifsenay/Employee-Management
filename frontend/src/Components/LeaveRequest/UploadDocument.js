import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import '../../Styles/LeaveRequest/UploadDocument.css';
import HomeButton from '../Auth/HomeButton';
import LogoutButton from '../Auth/LogoutButton';
import trashIcon from '../../Assets/115789_trash_icon.png';

function UploadDocument() {
    const [file, setFile] = useState(null);
    const [feedback, setFeedback] = useState('');
    const { leaveRequestId } = useParams();
    const navigate = useNavigate(); // Hook to navigate between routes
    const token = localStorage.getItem('token');

    const handleFileChange = (e) => {
        setFile(e.target.files[0]);
    };

    const handleFileRemove = () => {
        setFile(null);
    };

    const handleFileUpload = async (e) => {
        e.preventDefault();

        const formData = new FormData();
        console.log(formData);
        debugger;
        formData.append("file", file);

        if (!leaveRequestId || isNaN(leaveRequestId)) {
            setFeedback("Leave Request ID is invalid or undefined.");
            return;
        }


        await fetch(`http://localhost:8080/api/leaverequests/${leaveRequestId}/upload`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
            },
            body: formData,
        })
            .then(response => {
                if (response.headers.get('content-type').includes('application/json')) {
                    return response.json();
                }
                return response.text();
            })
            .then(data => {
                if (typeof data === 'string') {
                    setFeedback(data);
                } else {
                    setFeedback('File uploaded successfully');
                }
                setFile(null);
            })
            .catch((error) => {
                console.error('Error:', error);
                setFeedback('Error uploading file: ' + error.message);
            });
    };

    const handleDone = () => {
        navigate('/leave-requests-list'); // Redirect to the Leave Requests list page
    };

    return (
        <div className="upload-document-container">
            <HomeButton />
            <LogoutButton />
            <h2>Document Upload</h2>
            <form onSubmit={handleFileUpload}>
                <label>Select File:</label>
                <div className="file-input-container">
                    <input
                        type="file"
                        onChange={handleFileChange}
                        disabled={file !== null}
                    />
                    {file && (
                        <button
                            type="button"
                            className="remove-file-button"
                            onClick={handleFileRemove}
                        >
                            <img src={trashIcon} alt="Remove" />
                        </button>
                    )}
                </div>
                <button type="submit" disabled={!file}>Upload</button>
            </form>
            {feedback && <p className={`feedback ${feedback.includes('Error') ? 'error-text' : ''}`}>{feedback}</p>}
            {feedback === 'File uploaded successfully'}
                <button className="done-button" onClick={handleDone}>Done</button>

        </div>
    );
}

export default UploadDocument;
