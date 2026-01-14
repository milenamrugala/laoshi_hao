package pl.milenamrugala.laoshi_hao;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public class BookingForm {

    @NotBlank(message = "First name is required")
    private String studentFirstName;

    @NotBlank(message = "Last name is required")
    private String studentLastName;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    @Pattern(
            regexp = "^[a-zA-Z0-9._]+$",
            message = "Username can only contain letters, digits, dots and underscores (no spaces, no @)"
    )
    private String studentUsername;

    @NotBlank(message = "Email is required")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Please enter a valid email address"
    )
    private String studentEmail;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^\\+[0-9]+$",
            message = "Phone must start with + and contain digits only"
    )
    @Size(min = 8, max = 15, message = "Phone number must be between 8 and 15 digits")
    private String studentPhone;

    @NotNull(message = "Lesson date is required")
    @FutureOrPresent(message = "Lesson date cannot be in the past")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate lessonDate;

    @NotNull(message = "Lesson time is required")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime lessonTime;

    @Size(max = 2000, message = "Note is too long")
    private String note;

    public String getStudentFirstName() {
        return studentFirstName;
    }

    public void setStudentFirstName(String studentFirstName) {
        this.studentFirstName = studentFirstName != null ? studentFirstName.trim() : null;
    }

    public String getStudentLastName() {
        return studentLastName;
    }

    public void setStudentLastName(String studentLastName) {
        this.studentLastName = studentLastName != null ? studentLastName.trim() : null;
    }

    public String getStudentUsername() {
        return studentUsername;
    }

    public void setStudentUsername(String studentUsername) {
        this.studentUsername = studentUsername != null ? studentUsername.trim().toLowerCase() : null;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail != null ? studentEmail.trim().toLowerCase() : null;
    }

    public String getStudentPhone() {
        return studentPhone;
    }

    public void setStudentPhone(String studentPhone) {
        this.studentPhone = studentPhone != null ? studentPhone.trim() : null;
    }

    public LocalDate getLessonDate() {
        return lessonDate;
    }

    public void setLessonDate(LocalDate lessonDate) {
        this.lessonDate = lessonDate;
    }

    public LocalTime getLessonTime() {
        return lessonTime;
    }

    public void setLessonTime(LocalTime lessonTime) {
        this.lessonTime = lessonTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note != null ? note.trim() : null;
    }
}