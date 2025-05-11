# Course Feedback System

## Overview
The Course Feedback System is a web application designed to facilitate the collection and analysis of feedback from students regarding their courses and instructors. The system allows students to submit anonymous feedback, enables administrators to create and distribute feedback forms, and provides instructors with summarized results of the feedback received.

## Features
- **Anonymous Feedback Submission**: Students can fill out feedback forms without revealing their identities.
- **Admin Functionality**: Admins can create and manage feedback forms, including defining questions and settings.
- **Instructor Dashboard**: Instructors can view summarized feedback results for their courses and analyze ratings.
- **Feedback Export**: The system includes functionality to export feedback summaries in various formats for further analysis.

## Project Structure
The project is divided into two main parts: the backend and the frontend.

### Backend
- **Technology**: Spring Boot
- **Key Components**:
  - Controllers for handling requests related to authentication, feedback, and reports.
  - Services for business logic related to feedback management and user accounts.
  - Repositories for data access to various entities such as courses, feedback forms, and responses.

### Frontend
- **Technology**: React
- **Key Components**:
  - Components for different user roles (admin, instructor, student) to interact with the system.
  - Pages for dashboards and feedback forms.
  - Services for API calls and authentication.

## Getting Started
To run the Course Feedback System, follow these steps:

### Prerequisites
- Node.js and npm installed for the frontend.
- Java and Maven installed for the backend.

### Backend Setup
1. Navigate to the `backend` directory.
2. Run `mvn spring-boot:run` to start the backend server.

### Frontend Setup
1. Navigate to the `frontend` directory.
2. Run `npm install` to install dependencies.
3. Run `npm start` to start the React application.

## Contributing
Contributions are welcome! Please feel free to submit a pull request or open an issue for any suggestions or improvements.

## License
This project is licensed under the MIT License.