package pl.milenamrugala.laoshi_hao.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // basic fields you already had
    private String firstName;
    private String lastName;
    private String language;
    private String city;

    @Column(unique = true)
    private String username;
    private String email;
    private String phone;
    private String nationality;
    private String nativeLanguage;
    private Integer capacity;

    @Column(precision = 10, scale = 2)
    private BigDecimal pricePerHour;

    @Column(name = "photo_filename")
    private String photoFilename;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Teacher() {
    }

    public Teacher(String firstName, String lastName, String language, String city) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.language = language;
        this.city = city;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    // NEW GETTERS + SETTERS

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getNativeLanguage() {
        return nativeLanguage;
    }

    public void setNativeLanguage(String nativeLanguage) {
        this.nativeLanguage = nativeLanguage;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getPhotoFilename() {
        return photoFilename;
    }

    public void setPhotoFilename(String photoFilename) {
        this.photoFilename = photoFilename;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public BigDecimal getPricePerHour() { return pricePerHour; }

    public void setPricePerHour(BigDecimal pricePerHour) { this.pricePerHour = pricePerHour; }

    @PrePersist
    public void setCreatedAt() {
        this.createdAt = LocalDateTime.now();
    }
}