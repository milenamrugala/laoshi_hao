package pl.milenamrugala.laoshi_hao.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

public class TeacherForm {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Language is required")
    private String language;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    @Pattern(
            regexp = "^[a-zA-Z0-9._]+$",
            message = "Username can only contain letters, digits, dots and underscores (no spaces, no @)"
    )
    private String username;

    @NotBlank(message = "Email is required")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Please enter a valid email address"
    )
    private String email;

    // no whitespace allowed, only digits and +, -, () allowed (you can adjust)
    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^\\+[0-9]{4,15}$",
            message = "Phone must start with + and contain only digits, no spaces"
    )
    @Size(
            min = 8,
            max = 15,
            message = "Phone number must be between 8 and 15 digits"
    )
    private String phone;

    @NotBlank(message = "Nationality is required")
    private String nationality;

    @NotBlank(message = "Native language is required")
    private String nativeLanguage;

    @Min(value = 1, message = "Capacity must be at least 1")
    @Max(value = 100, message = "Capacity cannot be greater than 100")
    private Integer capacity;

    // getters and setters

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName != null ? firstName.trim() : null;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName != null ? lastName.trim() : null;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language != null ? language.trim() : null;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city != null ? city.trim() : null;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        // normalize: trim + lowercase
        this.username = username != null ? username.trim().toLowerCase() : null;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email != null ? email.trim().toLowerCase() : null;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        // remove spaces just in case, and validate with regex
        this.phone = phone != null ? phone.replaceAll("\\s+", "") : null;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality != null ? nationality.trim() : null;
    }

    public String getNativeLanguage() {
        return nativeLanguage;
    }

    public void setNativeLanguage(String nativeLanguage) {
        this.nativeLanguage = nativeLanguage != null ? nativeLanguage.trim() : null;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}
