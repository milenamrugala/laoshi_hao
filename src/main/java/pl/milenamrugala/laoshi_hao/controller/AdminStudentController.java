package pl.milenamrugala.laoshi_hao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.milenamrugala.laoshi_hao.entity.Student;
import pl.milenamrugala.laoshi_hao.entity.Message;
import pl.milenamrugala.laoshi_hao.entity.Booking;
import pl.milenamrugala.laoshi_hao.repository.StudentRepository;
import pl.milenamrugala.laoshi_hao.repository.MessageRepository;
import pl.milenamrugala.laoshi_hao.repository.BookingRepository;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import pl.milenamrugala.laoshi_hao.form.StudentForm;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Controller
public class AdminStudentController {

    private final StudentRepository studentRepository;
    private final MessageRepository messageRepository;
    private final BookingRepository bookingRepository;

    public AdminStudentController(StudentRepository studentRepository,
                                  MessageRepository messageRepository,
                                  BookingRepository bookingRepository) {
        this.studentRepository = studentRepository;
        this.messageRepository = messageRepository;
        this.bookingRepository = bookingRepository;
    }

    // LISTA STUDENTÓW
    @GetMapping("/admin/students")
    public String listStudents(Model model) {
        model.addAttribute("students", studentRepository.findAll());
        return "admin/students";
    }

    // FORMULARZ EDYCJI
    @GetMapping("/admin/students/{id}/edit")
    public String showEditStudentForm(@PathVariable Long id, Model model) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        StudentForm form = new StudentForm();
        form.setUsername(student.getUsername());
        form.setEmail(student.getEmail());
        form.setFirstName(student.getFirstName());
        form.setLastName(student.getLastName());
        form.setPhone(student.getPhone());

        model.addAttribute("studentForm", form);
        model.addAttribute("studentId", id);

        return "admin/student-edit";
    }

    // ZAPIS EDYCJI
    @PostMapping("/admin/students/{id}/edit")
    public String updateStudent(@PathVariable Long id,
                                @Valid StudentForm studentForm,
                                BindingResult bindingResult,
                                Model model) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        // sprawdzenie unikalności username (dla innych studentów)
        studentRepository.findByUsername(studentForm.getUsername())
                .filter(s -> !s.getId().equals(id))
                .ifPresent(s -> bindingResult.rejectValue(
                        "username",
                        "username.exists",
                        "This username is already taken. Choose another."
                ));

        // sprawdzenie unikalności email (dla innych studentów)
        studentRepository.findByEmail(studentForm.getEmail())
                .filter(s -> !s.getId().equals(id))
                .ifPresent(s -> bindingResult.rejectValue(
                        "email",
                        "email.exists",
                        "This email is already used. Choose another."
                ));

        if (bindingResult.hasErrors()) {
            model.addAttribute("studentId", id);
            return "admin/student-edit";
        }

        // aktualizacja danych
        // (brak setterów w encji? możesz dodać, ale tu zrobimy przez refleksję albo po prostu dodaj settery)
        try {
            java.lang.reflect.Field fUsername = Student.class.getDeclaredField("username");
            fUsername.setAccessible(true);
            fUsername.set(student, studentForm.getUsername());

            java.lang.reflect.Field fEmail = Student.class.getDeclaredField("email");
            fEmail.setAccessible(true);
            fEmail.set(student, studentForm.getEmail());

            java.lang.reflect.Field fFirst = Student.class.getDeclaredField("firstName");
            fFirst.setAccessible(true);
            fFirst.set(student, studentForm.getFirstName());

            java.lang.reflect.Field fLast = Student.class.getDeclaredField("lastName");
            fLast.setAccessible(true);
            fLast.set(student, studentForm.getLastName());

            java.lang.reflect.Field fPhone = Student.class.getDeclaredField("phone");
            fPhone.setAccessible(true);
            fPhone.set(student, studentForm.getPhone());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        studentRepository.save(student);

        return "redirect:/admin/students";
    }

    // USUWANIE STUDENTA
    @Transactional
    @PostMapping("/admin/students/{id}/delete")
    public String deleteStudent(@PathVariable Long id) {

        Student student = studentRepository.findById(id)
                .orElse(null);

        if (student != null) {
            bookingRepository.deleteByStudent(student);
            messageRepository.deleteByStudent(student);
            studentRepository.delete(student);
        }

        return "redirect:/admin/students";
    }


    // WIADOMOŚCI STUDENTA
    @GetMapping("/admin/students/{id}/messages")
    public String studentMessages(@PathVariable Long id, Model model) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        model.addAttribute("student", student);

        List<Message> messages = messageRepository.findAll()
                .stream()
                .filter(m -> m.getStudent() != null && m.getStudent().getId().equals(id))
                .toList();

        model.addAttribute("messages", messages);
        return "admin/student-messages";
    }

    // REZERWACJE STUDENTA
    @GetMapping("/admin/students/{id}/bookings")
    public String studentBookings(@PathVariable Long id, Model model) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        model.addAttribute("student", student);

        List<Booking> bookings = bookingRepository.findAll()
                .stream()
                .filter(b -> b.getStudent() != null && b.getStudent().getId().equals(id))
                .toList();

        model.addAttribute("bookings", bookings);
        return "admin/student-bookings";
    }
}