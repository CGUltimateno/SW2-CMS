import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const CreateFeedbackForm = () => {
    const [formTitle, setFormTitle] = useState("");
    const [questions, setQuestions] = useState([{ questionText: "" }]);
    const navigate = useNavigate();

    const handleQuestionChange = (index, event) => {
        const newQuestions = [...questions];
        newQuestions[index].questionText = event.target.value;
        setQuestions(newQuestions);
    };

    const addQuestion = () => {
        setQuestions([...questions, { questionText: "" }]);
    };

    const removeQuestion = (index) => {
        const newQuestions = questions.filter((_, i) => i !== index);
        setQuestions(newQuestions);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const feedbackForm = {
            title: formTitle,
            questions: questions.map(q => q.questionText),
        };
        await axios.post("http://localhost:8080/feedback-forms", feedbackForm);
        navigate("/admin-dashboard");
    };

    return (
        <div className="container mt-5">
            <h2>Create Feedback Form</h2>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="formTitle">Form Title</label>
                    <input
                        type="text"
                        className="form-control"
                        id="formTitle"
                        value={formTitle}
                        onChange={(e) => setFormTitle(e.target.value)}
                        required
                    />
                </div>
                {questions.map((question, index) => (
                    <div key={index} className="form-group">
                        <label htmlFor={`question${index}`}>Question {index + 1}</label>
                        <input
                            type="text"
                            className="form-control"
                            id={`question${index}`}
                            value={question.questionText}
                            onChange={(e) => handleQuestionChange(index, e)}
                            required
                        />
                        <button type="button" onClick={() => removeQuestion(index)} className="btn btn-danger mt-2">Remove Question</button>
                    </div>
                ))}
                <button type="button" onClick={addQuestion} className="btn btn-primary mt-3">Add Question</button>
                <button type="submit" className="btn btn-success mt-3">Create Form</button>
            </form>
        </div>
    );
};

export default CreateFeedbackForm;