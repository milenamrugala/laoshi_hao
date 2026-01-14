package pl.milenamrugala.laoshi_hao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class MessageForm {

    @NotBlank(message = "Your name is required")
    private String studentFirstName;

    @NotBlank(message = "Your name is required")
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

    @NotBlank(message = "Message cannot be empty")
    @Size(max = 2000, message = "Message is too long")
    private String content;

    public String getStudentFirstName() {
        return studentFirstName;
    }

    public String getStudentLastName() {
        return studentLastName;
    }

    public void setStudentFirstName(String studentFirstName) {
        this.studentFirstName = studentFirstName != null ? studentFirstName.trim() : null;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content != null ? content.trim() : null;
    }
}