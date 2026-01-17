package pl.milenamrugala.laoshi_hao.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.milenamrugala.laoshi_hao.form.BookingForm;
import pl.milenamrugala.laoshi_hao.repository.BookingRepository;
import pl.milenamrugala.laoshi_hao.repository.TeacherRepository;
import pl.milenamrugala.laoshi_hao.entity.Booking;
import pl.milenamrugala.laoshi_hao.entity.Teacher;
import pl.milenamrugala.laoshi_hao.entity.Student;
import pl.milenamrugala.laoshi_hao.repository.StudentRepository;
import java.time.LocalDate;
import java.util.List;

@Controller
public class BookingController {

    private final TeacherRepository teacherRepository;
    private final BookingRepository bookingRepository;
    private final StudentRepository studentRepository;

    public BookingController(TeacherRepository teacherRepository,
                             BookingRepository bookingRepository,
                             StudentRepository studentRepository) {
        this.teacherRepository = teacherRepository;
        this.bookingRepository = bookingRepository;
        this.studentRepository = studentRepository;
    }

    @GetMapping("/teachers/{id}/booking")
    public String showBookingForm(@PathVariable Long id, Model model) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));

        BookingForm form = new BookingForm();

        model.addAttribute("teacher", teacher);
        model.addAttribute("bookingForm", form);

        return "booking-form";
    }

    @PostMapping("/teachers/{id}/booking")
    public String createBooking(@PathVariable Long id,
                                @Valid BookingForm bookingForm,
                                BindingResult bindingResult,
                                Model model) {

        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));

        if (bindingResult.hasErrors()) {
            model.addAttribute("teacher", teacher);
            return "booking-form";
        }

        Integer capacity = teacher.getCapacity();

        if (capacity != null) {
            // aktywne bookingi: tylko PENDING lub CONFIRMED, od dzisiaj w przyszłość
            List<Booking> activeBookings = bookingRepository.findAll().stream()
                    .filter(b -> b.getTeacher().getId().equals(teacher.getId()))
                    .filter(b -> b.getLessonDate() != null && !b.getLessonDate().isBefore(LocalDate.now()))
                    .filter(b -> "PENDING".equalsIgnoreCase(b.getStatus()) || "CONFIRMED".equalsIgnoreCase(b.getStatus()))
                    .toList();

            if (activeBookings.size() >= capacity) {
                bindingResult.reject(
                        "capacity.full",
                        "This teacher has no free slots available at the moment. Contact this teacher directly through message."
                );
                model.addAttribute("teacher", teacher);
                return "booking-form";
            }
        }


// find or create Student by username
        Student student = studentRepository.findByUsername(bookingForm.getStudentUsername())
                .orElseGet(() -> {
                    Student s = new Student(
                            bookingForm.getStudentUsername(),
                            bookingForm.getStudentEmail(),
                            bookingForm.getStudentFirstName(),
                            bookingForm.getStudentLastName(),
                            bookingForm.getStudentPhone()
                    );
                    return studentRepository.save(s);
                });

        Booking booking = new Booking(
                teacher,
                bookingForm.getStudentFirstName(),
                bookingForm.getStudentLastName(),
                bookingForm.getStudentUsername(),
                bookingForm.getStudentEmail(),
                bookingForm.getStudentPhone(),
                bookingForm.getLessonDate(),
                bookingForm.getLessonTime(),
                bookingForm.getNote()
        );

        booking.setStudent(student);

        bookingRepository.save(booking);

        // later we can add a "booking confirmed" page; for now go back to teacher profile
        return "redirect:/teachers/" + id;
    }
}