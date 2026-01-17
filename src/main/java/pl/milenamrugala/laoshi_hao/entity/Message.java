package pl.milenamrugala.laoshi_hao.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import pl.milenamrugala.laoshi_hao.entity.Student;

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // who this message is for
    @ManyToOne(optional = false)
    private Teacher teacher;

    // student info
    private String studentFirstName;
    private String studentLastName;
    private String studentUsername;
    private String studentEmail;
    private String studentPhone;

    @ManyToOne
    private Student student;

    // the message itself
    @Column(length = 2000)
    private String content;

    private LocalDateTime createdAt;

    protected Message() {
        // JPA
    }

    public Message(Teacher teacher,
                   String studentFirstName,
                   String studentLastName,
                   String studentUsername,
                   String studentEmail,
                   String studentPhone,
                   String content) {

        this.teacher = teacher;
        this.studentFirstName = studentFirstName;
        this.studentLastName = studentLastName;
        this.studentUsername = studentUsername;
        this.studentEmail = studentEmail;
        this.studentPhone = studentPhone;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
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

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
