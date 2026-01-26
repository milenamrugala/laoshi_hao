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
- MySQL

### Frontend
- Thymeleaf templates
- HTML5
- CSS

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

This project was developed as a portfolio application to demonstrate full-stack Java & Spring skills.

<img width="1298" height="777" alt="Zrzut ekranu 2026-01-22 o 20 18 46" src="https://github.com/user-attachments/assets/d2e72714-c0f9-43e2-842d-d7dba53fa52c" />

<img width="1281" height="776" alt="Zrzut ekranu 2026-01-22 o 20 18 58" src="https://github.com/user-attachments/assets/173d1d3b-c0c1-4488-a079-27169182280b" />

<img width="1281" height="775" alt="Zrzut ekranu 2026-01-22 o 20 19 09" src="https://github.com/user-attachments/assets/68c65769-1e4e-4928-83cd-6d9adf860da6" />

<img width="1298" height="776" alt="Zrzut ekranu 2026-01-22 o 20 19 33" src="https://github.com/user-attachments/assets/5c40aae4-b33f-4c0a-8d15-ecd929cd3588" />

<img width="1283" height="778" alt="Zrzut ekranu 2026-01-22 o 20 20 03" src="https://github.com/user-attachments/assets/0b663396-19de-4d5d-80a8-2ee53b824183" />

<img width="1283" height="777" alt="Zrzut ekranu 2026-01-22 o 20 20 16" src="https://github.com/user-attachments/assets/72802565-fa4a-45ad-8f64-54917781286a" />

<img width="1283" height="753" alt="Zrzut ekranu 2026-01-22 o 20 20 38" src="https://github.com/user-attachments/assets/b19b44db-a517-41fd-8f6d-7807ca697eb0" />

<img width="1272" height="780" alt="Zrzut ekranu 2026-01-22 o 20 20 50" src="https://github.com/user-attachments/assets/850063f4-3036-4473-92d3-4021ebe40af0" />

<img width="497" height="647" alt="Zrzut ekranu 2026-01-26 o 12 33 12" src="https://github.com/user-attachments/assets/d819e096-ed19-4578-8b74-2717a91cd1a5" />
