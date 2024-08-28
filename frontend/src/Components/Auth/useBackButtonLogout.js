import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

function useBackButtonLogout() {
    const navigate = useNavigate();

    useEffect(() => {
        const token = localStorage.getItem('token');

        if (!token) {
            navigate('/login');
        }

        const handlePopState = (event) => {
            event.preventDefault();
            const confirmLogout = window.confirm("Are you sure you want to go back? This will log you out.");
            if (confirmLogout) {
                localStorage.removeItem('token');
                navigate('/login');
            } else {
                // Re-push the current state to prevent back navigation
                window.history.pushState(null, null, window.location.href);
            }
        };

        // Push initial state to the history
        window.history.pushState(null, null, window.location.href);

        // Add popstate listener
        window.addEventListener('popstate', handlePopState);

        return () => {
            window.removeEventListener('popstate', handlePopState);
        };
    }, [navigate]);
}

export default useBackButtonLogout;
