package pl.milenamrugala.laoshi_hao;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BookingController {

    private final TeacherRepository teacherRepository;
    private final BookingRepository bookingRepository;

    public BookingController(TeacherRepository teacherRepository,
                             BookingRepository bookingRepository) {
        this.teacherRepository = teacherRepository;
        this.bookingRepository = bookingRepository;
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

        bookingRepository.save(booking);

        // later we can add a "booking confirmed" page; for now go back to teacher profile
        return "redirect:/teachers/" + id;
    }
}