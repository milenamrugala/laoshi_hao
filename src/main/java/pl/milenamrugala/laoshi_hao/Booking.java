package pl.milenamrugala.laoshi_hao;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Teacher teacher;

    // student info
    private String studentFirstName;
    private String studentLastName;
    private String studentUsername;
    private String studentEmail;
    private String studentPhone;

    // requested lesson time
    private LocalDate lessonDate;
    private LocalTime lessonTime;

    // optional note from student
    @Column(length = 2000)
    private String note;

    // metadata
    private LocalDateTime createdAt;

    // could be PENDING, APPROVED, CANCELLED etc.
    private String status;

    protected Booking() {
        // JPA
    }

    public Booking(Teacher teacher,
                   String studentFirstName,
                   String studentLastName,
                   String studentUsername,
                   String studentEmail,
                   String studentPhone,
                   LocalDate lessonDate,
                   LocalTime lessonTime,
                   String note) {

        this.teacher = teacher;
        this.studentFirstName = studentFirstName;
        this.studentLastName = studentLastName;
        this.studentUsername = studentUsername;
        this.studentEmail = studentEmail;
        this.studentPhone = studentPhone;
        this.lessonDate = lessonDate;
        this.lessonTime = lessonTime;
        this.note = note;
        this.createdAt = LocalDateTime.now();
        this.status = "PENDING";
    }

    public Long getId() {
        return id;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public String getStudentFirstName() {
        return studentFirstName;
    }

    public String getStudentLastName() {
        return studentLastName;
    }

    public String getStudentUsername() {
        return studentUsername;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public String getStudentPhone() {
        return studentPhone;
    }

    public LocalDate getLessonDate() {
        return lessonDate;
    }

    public LocalTime getLessonTime() {
        return lessonTime;
    }

    public String getNote() {
        return note;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getStatus() {
        return status;
    }
}