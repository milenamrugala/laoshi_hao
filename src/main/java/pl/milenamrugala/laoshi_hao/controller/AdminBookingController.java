package pl.milenamrugala.laoshi_hao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.milenamrugala.laoshi_hao.repository.BookingRepository;
import pl.milenamrugala.laoshi_hao.repository.TeacherRepository;
import pl.milenamrugala.laoshi_hao.entity.Booking;
import pl.milenamrugala.laoshi_hao.entity.Teacher;

import java.util.List;

@Controller
public class AdminBookingController {

    private final BookingRepository bookingRepository;
    private final TeacherRepository teacherRepository;

    public AdminBookingController(BookingRepository bookingRepository,
                                  TeacherRepository teacherRepository) {
        this.bookingRepository = bookingRepository;
        this.teacherRepository = teacherRepository;
    }

    @GetMapping("/admin/bookings")
    public String listAllBookings(Model model) {
        List<Booking> bookings = bookingRepository.findAllByOrderByLessonDateAscLessonTimeAsc();
        model.addAttribute("bookings", bookings);
        return "admin/bookings";
    }

    @GetMapping("/admin/teachers/{id}/bookings")
    public String listBookingsForTeacher(@PathVariable Long id, Model model) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));

        List<Booking> bookings = bookingRepository.findByTeacherIdOrderByLessonDateAscLessonTimeAsc(id);

        model.addAttribute("teacher", teacher);
        model.addAttribute("bookings", bookings);

        return "admin/teacher-bookings";
    }

    @PostMapping("/admin/bookings/{id}/delete")
    public String deleteBooking(@PathVariable Long id,
                                @RequestParam(required = false) Long teacherId) {

        if (bookingRepository.existsById(id)) {
            bookingRepository.deleteById(id);
        }

        if (teacherId != null) {
            // deleted from "bookings for a specific teacher" view
            return "redirect:/admin/teachers/" + teacherId + "/bookings";
        }

        // deleted from "all bookings" view
        return "redirect:/admin/bookings";
    }
}