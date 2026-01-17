# Laoshi Hao

Laoshi Hao is a web application for connecting students with language teachers.
It allows teachers to present their profiles and manage bookings, while students can browse teachers, book lessons, and communicate with them.

The project was built as a full-stack Spring Boot application with a custom UI and without authentication at the current stage.

---

## Features

### Public
- Home page with role selection (Student / Teacher)
- Teachers list with avatars
- Teacher public profile page
- Booking form for lessons
- Contact form to send messages to teachers

### Student
- Student registration (without authentication)
- Student dashboard:
  - View upcoming and past bookings
  - View sent messages
- Edit student profile

### Teacher
- Teacher registration (without authentication)
- Teacher dashboard:
  - Profile overview
  - Upcoming and past bookings
  - Confirm or cancel bookings
  - View and delete messages
- Edit teacher profile
- Upload, change, and delete profile photo

---

## Tech Stack

### Backend
- Java 17+
- Spring Boot
- Spring MVC
- Spring Validation
- Spring Data JPA (Hibernate)
- Thymeleaf
- PostgreSQL / H2 (depending on environment)

### Frontend
- Thymeleaf templates
- HTML5
- CSS3 (custom design, no frameworks)
- Responsive layout
- Kawaii / anime-inspired UI theme

### Other
- Multipart file upload (teacher photos)
- Server-side validation
- Clean MVC architecture
- No authentication/security (planned for future development)

---

## Project Structure

- `controller` – MVC controllers (Student, Teacher, Booking, Message)
- `entity` – JPA entities
- `repository` – Spring Data repositories
- `form` – DTOs with validation
- `templates` – Thymeleaf views
- `static/css` – global styling
- `static/img` – avatars and assets

---

## Future Improvements

- Authentication and authorization (Spring Security)
- Role-based access (Student / Teacher)
- Messaging inbox for students
- Availability calendar for teachers
- Pagination and search filters
- REST API version

---

## Author

Created by **Milena Mrugała** This project was developed as a portfolio application to demonstrate full-stack Java & Spring skills.


<img width="1389" height="775" alt="Zrzut ekranu 2026-01-17 o 17 02 47" src="https://github.com/user-attachments/assets/d6b9bd1a-9426-4be6-82b6-4fde9a57687d" />

<img width="1380" height="769" alt="Zrzut ekranu 2026-01-17 o 17 02 58" src="https://github.com/user-attachments/assets/a9cb9d6e-c571-4f5e-ab3e-d46318dc2466" />

<img width="1391" height="771" alt="Zrzut ekranu 2026-01-17 o 17 03 47" src="https://github.com/user-attachments/assets/a401c08f-4f84-408c-a2f9-7c6f4af0f22b" />

<img width="1384" height="768" alt="Zrzut ekranu 2026-01-17 o 17 04 04" src="https://github.com/user-attachments/assets/4eb1c272-b1d1-4fbd-b12b-b7aa934ff9ad" />

<img width="1379" height="774" alt="Zrzut ekranu 2026-01-17 o 17 04 18" src="https://github.com/user-attachments/assets/4686feae-2e82-44fb-9c28-9d4e7a84bc5b" />








