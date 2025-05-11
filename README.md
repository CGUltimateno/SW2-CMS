# Course Feedback System

## Overview
The Course Feedback System is a web application designed to facilitate the collection and analysis of feedback from students regarding their courses and instructors. This system allows students to submit anonymous feedback, enables administrators to create and distribute feedback forms, and provides instructors with summarized results to improve their teaching methods.

## Features
- **Anonymous Feedback Submission**: Students can fill out feedback forms without revealing their identities.
- **Admin Controls**: Administrators can create and manage feedback forms, including defining questions and settings.
- **Instructor Dashboard**: Instructors can view summarized feedback results for their courses and analyze ratings.
- **Course and Instructor Ratings**: Users can view ratings and feedback per course or instructor.
- **Export Functionality**: Feedback summaries can be exported in various formats for reporting purposes.

## Project Structure
The project is divided into two main parts: the backend and the frontend.

### Backend
- **Java Spring Boot**: The backend is built using Spring Boot, providing RESTful APIs for the frontend to interact with.
- **Security**: Configured with security settings to manage authentication and authorization.
- **Data Models**: Includes models for Course, FeedbackForm, FeedbackResponse, Instructor, and User.
- **Repositories**: Data access layers for managing entities.
- **Services**: Business logic for handling feedback and user management.

### Frontend
- **React**: The frontend is developed using React, providing a dynamic user interface.
- **Components**: Organized into admin, instructor, and student components for specific functionalities.
- **Routing**: Utilizes React Router for navigation between different pages.
- **API Integration**: Communicates with the backend through defined API services.

## Getting Started
To run the Course Feedback System locally, follow these steps:

### Prerequisites
- Java JDK 11 or higher
- Node.js and npm
- Maven

### Backend Setup
1. Navigate to the `backend` directory.
2. Run `mvn clean install` to build the project.
3. Configure the `application.properties` file for your database settings.
4. Start the Spring Boot application using `mvn spring-boot:run`.

### Frontend Setup
1. Navigate to the `frontend` directory.
2. Run `npm install` to install the required dependencies.
3. Start the React application using `npm start`.

## Contributing
Contributions are welcome! Please submit a pull request or open an issue for any enhancements or bug fixes.

## License
This project is licensed under the MIT License. See the LICENSE file for more details.