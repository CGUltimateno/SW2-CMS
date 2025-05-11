const validateEmail = (email) => {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return re.test(String(email).toLowerCase());
};

const validateFeedback = (feedback) => {
    return feedback && feedback.trim().length > 0;
};

const validateCourseSelection = (course) => {
    return course && course.length > 0;
};

const validateRating = (rating) => {
    return rating >= 1 && rating <= 5;
};

export { validateEmail, validateFeedback, validateCourseSelection, validateRating };