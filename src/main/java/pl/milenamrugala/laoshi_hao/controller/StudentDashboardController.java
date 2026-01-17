package pl.milenamrugala.laoshi_hao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pl.milenamrugala.laoshi_hao.entity.Student;
import pl.milenamrugala.laoshi_hao.entity.Booking;
import pl.milenamrugala.laoshi_hao.entity.Message;
import pl.milenamrugala.laoshi_hao.repository.StudentRepository;
import pl.milenamrugala.laoshi_hao.repository.BookingRepository;
import pl.milenamrugala.laoshi_hao.repository.MessageRepository;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import pl.milenamrugala.laoshi_hao.form.StudentForm;

import java.time.LocalDate;
import java.util.List;

@Controller
public class StudentDashboardController {

    private final StudentRepository studentRepository;
    private final BookingRepository bookingRepository;
    private final MessageRepository messageRepository;

    public StudentDashboardController(StudentRepository studentRepository,
                                      BookingRepository bookingRepository,
                                      MessageRepository messageRepository) {
        this.studentRepository = studentRepository;
        this.bookingRepository = bookingRepository;
        this.messageRepository = messageRepository;
    }

    @GetMapping("/students/{id}/dashboard")
    public String showStudentDashboard(@PathVariable Long id, Model model) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        LocalDate today = LocalDate.now();

        // wszystkie bookingi tego studenta
        List<Booking> allBookings = bookingRepository
                .findByStudentIdOrderByLessonDateAscLessonTimeAsc(id);

        // nadchodzące bookingi (dziś lub później)
        List<Booking> upcomingBookings = allBookings.stream()
                .filter(b -> b.getLessonDate() != null && !b.getLessonDate().isBefore(today))
                .toList();

        // przeszłe bookingi (przed dzisiaj)
        List<Booking> pastBookings = allBookings.stream()
                .filter(b -> b.getLessonDate() != null && b.getLessonDate().isBefore(today))
                .toList();

        // wiadomości tego studenta do nauczycieli, najnowsze pierwsze
        List<Message> allMessages = messageRepository
                .findByStudentIdOrderByCreatedAtDesc(id);

        List<Message> recentMessages = allMessages.stream()
                .limit(50)
                .toList();

        model.addAttribute("student", student);
        model.addAttribute("upcomingBookings", upcomingBookings);
        model.addAttribute("pastBookings", pastBookings);
        model.addAttribute("recentMessages", recentMessages);

        return "student-dashboard";
    }

    @GetMapping("/students/{id}/edit")
    public String showStudentEditForm(@PathVariable Long id, Model model) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        StudentForm form = new StudentForm();
        form.setFirstName(student.getFirstName());
        form.setLastName(student.getLastName());
        form.setUsername(student.getUsername());
        form.setEmail(student.getEmail());
        form.setPhone(student.getPhone());

        model.addAttribute("studentForm", form);
        model.addAttribute("studentId", id);

        return "student-edit";
    }

    @PostMapping("/students/{id}/edit")
    public String updateStudentSelf(@PathVariable Long id,
                                    @Valid @ModelAttribute("studentForm") StudentForm studentForm,
                                    BindingResult bindingResult,
                                    Model model) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        // unikamy duplikatu username (inny student z tym samym username)
        studentRepository.findByUsername(studentForm.getUsername())
                .filter(s -> !s.getId().equals(id))
                .ifPresent(s -> bindingResult.rejectValue(
                        "username",
                        "username.exists",
                        "This username is already taken. Choose another."
                ));

        // unikamy duplikatu email (inny student z tym samym mailem)
        studentRepository.findByEmail(studentForm.getEmail())
                .filter(s -> !s.getId().equals(id))
                .ifPresent(s -> bindingResult.rejectValue(
                        "email",
                        "email.exists",
                        "This email is already used. Choose another."
                ));

        if (bindingResult.hasErrors()) {
            model.addAttribute("studentId", id);
            return "student-edit";
        }

        // tu najlepiej zwykłe settery w encji Student
        student.setFirstName(studentForm.getFirstName());
        student.setLastName(studentForm.getLastName());
        student.setUsername(studentForm.getUsername());
        student.setEmail(studentForm.getEmail());
        student.setPhone(studentForm.getPhone());

        studentRepository.save(student);

        return "redirect:/students/" + id + "/dashboard";
    }
}