import React, { useState, useEffect } from 'react';
import { exportFeedbackSummary } from '../../services/feedbackService';

const ExportFeedback = () => {
    const [feedbackData, setFeedbackData] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchFeedbackData = async () => {
            try {
                const data = await exportFeedbackSummary();
                setFeedbackData(data);
            } catch (err) {
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };

        fetchFeedbackData();
    }, []);

    const handleExport = (format) => {
        // Logic to export feedback data in the selected format (e.g., CSV, PDF)
        // This could involve calling a service function that handles the export
        console.log(`Exporting feedback data as ${format}`);
    };

    if (loading) {
        return <div>Loading feedback data...</div>;
    }

    if (error) {
        return <div>Error: {error}</div>;
    }

    return (
        <div>
            <h2>Export Feedback Summary</h2>
            <div>
                <button onClick={() => handleExport('CSV')}>Export as CSV</button>
                <button onClick={() => handleExport('PDF')}>Export as PDF</button>
            </div>
            <h3>Feedback Data</h3>
            <pre>{JSON.stringify(feedbackData, null, 2)}</pre>
        </div>
    );
};

export default ExportFeedback;