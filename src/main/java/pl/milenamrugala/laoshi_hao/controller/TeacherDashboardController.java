package pl.milenamrugala.laoshi_hao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.milenamrugala.laoshi_hao.entity.Teacher;
import pl.milenamrugala.laoshi_hao.entity.Booking;
import pl.milenamrugala.laoshi_hao.entity.Message;
import pl.milenamrugala.laoshi_hao.repository.TeacherRepository;
import pl.milenamrugala.laoshi_hao.repository.BookingRepository;
import pl.milenamrugala.laoshi_hao.repository.MessageRepository;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import pl.milenamrugala.laoshi_hao.form.TeacherForm;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
// import java.nio.file.StandardCopyOption; // not needed right now

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Controller
public class TeacherDashboardController {

    private final TeacherRepository teacherRepository;
    private final BookingRepository bookingRepository;
    private final MessageRepository messageRepository;

    public TeacherDashboardController(TeacherRepository teacherRepository,
                                      BookingRepository bookingRepository,
                                      MessageRepository messageRepository) {
        this.teacherRepository = teacherRepository;
        this.bookingRepository = bookingRepository;
        this.messageRepository = messageRepository;
    }

    // ===================== DASHBOARD =====================

    @GetMapping("/teachers/{id}/dashboard")
    public String showDashboard(@PathVariable Long id, Model model) {

        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));

        LocalDate today = LocalDate.now();

        // all bookings for this teacher, ordered by date/time
        List<Booking> allBookings = bookingRepository
                .findByTeacherIdOrderByLessonDateAscLessonTimeAsc(id);

        // upcoming bookings (today or later, not null)
        List<Booking> upcomingBookings = allBookings.stream()
                .filter(b -> b.getLessonDate() != null && !b.getLessonDate().isBefore(today))
                .toList();

        // past bookings (before today)
        List<Booking> pastBookings = allBookings.stream()
                .filter(b -> b.getLessonDate() != null && b.getLessonDate().isBefore(today))
                .toList();

        // active = PENDING or CONFIRMED in the future
        long activeCount = upcomingBookings.stream()
                .filter(b -> {
                    String status = b.getStatus();
                    return status != null &&
                            ("PENDING".equalsIgnoreCase(status) || "CONFIRMED".equalsIgnoreCase(status));
                })
                .count();

        Integer capacity = teacher.getCapacity();
        Long freeSlots = null;
        if (capacity != null) {
            long diff = capacity - activeCount;
            freeSlots = diff < 0 ? 0 : diff;
        }

        // all messages for this teacher, newest first
        List<Message> allMessages = messageRepository
                .findByTeacherIdOrderByCreatedAtDesc(id);

        // last 50 messages
        List<Message> recentMessages = allMessages.stream()
                .limit(50)
                .toList();

        model.addAttribute("teacher", teacher);
        model.addAttribute("upcomingBookings", upcomingBookings);
        model.addAttribute("pastBookings", pastBookings);
        model.addAttribute("activeBookingsCount", activeCount);
        model.addAttribute("capacity", capacity);
        model.addAttribute("freeSlots", freeSlots);
        model.addAttribute("recentMessages", recentMessages);

        return "teacher-dashboard";
    }

    // ===================== EDIT TEACHER (DATA) =====================

    @GetMapping("/teachers/{id}/edit")
    public String showTeacherEditForm(@PathVariable Long id, Model model) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));

        TeacherForm form = new TeacherForm();
        form.setFirstName(teacher.getFirstName());
        form.setLastName(teacher.getLastName());
        form.setUsername(teacher.getUsername());
        form.setEmail(teacher.getEmail());
        form.setPhone(teacher.getPhone());
        form.setLanguage(teacher.getLanguage());
        form.setCity(teacher.getCity());
        form.setCapacity(teacher.getCapacity());
        form.setNativeLanguage(teacher.getNativeLanguage());
        form.setNationality(teacher.getNationality());

        model.addAttribute("teacherForm", form);
        model.addAttribute("teacherId", id);
        model.addAttribute("teacher", teacher); // for photo preview

        return "teacher-edit";
    }

    @PostMapping("/teachers/{id}/edit")
    public String updateTeacherSelf(@PathVariable Long id,
                                    @Valid @ModelAttribute("teacherForm") TeacherForm teacherForm,
                                    BindingResult bindingResult,
                                    Model model) {

        System.out.println(">>> UPDATE TEACHER SELF CALLED for id = " + id);
        System.out.println("FORM values: language=" + teacherForm.getLanguage()
                + ", capacity=" + teacherForm.getCapacity());

        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));

        // unique username
        teacherRepository.findByUsername(teacherForm.getUsername())
                .filter(t -> !t.getId().equals(id))
                .ifPresent(t -> bindingResult.rejectValue(
                        "username",
                        "username.exists",
                        "This username is already taken. Choose another."
                ));

        // unique email
        teacherRepository.findByEmail(teacherForm.getEmail())
                .filter(t -> !t.getId().equals(id))
                .ifPresent(t -> bindingResult.rejectValue(
                        "email",
                        "email.exists",
                        "This email is already used. Choose another."
                ));

        System.out.println("bindingResult.hasErrors() = " + bindingResult.hasErrors());

        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(err ->
                    System.out.println("  ERROR: " + err.toString())
            );
            model.addAttribute("teacherId", id);
            model.addAttribute("teacher", teacher);
            return "teacher-edit";
        }

        System.out.println("BEFORE UPDATE entity: language=" + teacher.getLanguage()
                + ", capacity=" + teacher.getCapacity());

        teacher.setFirstName(teacherForm.getFirstName());
        teacher.setLastName(teacherForm.getLastName());
        teacher.setUsername(teacherForm.getUsername());
        teacher.setEmail(teacherForm.getEmail());
        teacher.setPhone(teacherForm.getPhone());
        teacher.setLanguage(teacherForm.getLanguage());
        teacher.setCapacity(teacherForm.getCapacity());
        teacher.setCity(teacherForm.getCity());
        teacher.setNativeLanguage(teacherForm.getNativeLanguage());
        teacher.setNationality(teacherForm.getNationality());

        teacherRepository.save(teacher);

        // sprawdź, co poszło do bazy
        teacherRepository.findById(id).ifPresent(t ->
                System.out.println("AFTER SAVE FROM DB: language=" + t.getLanguage()
                        + ", capacity=" + t.getCapacity())
        );

        return "redirect:/teachers/" + id + "/dashboard";
    }

    // ===================== PHOTO UPLOAD / CHANGE =====================

    @PostMapping("/teachers/{id}/photo")
    public String changePhoto(@PathVariable Long id,
                              @RequestParam("photo") MultipartFile photo) throws IOException {

        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));

        if (photo == null || photo.isEmpty()) {
            // nothing selected → go back to edit
            return "redirect:/teachers/" + id + "/edit";
        }

        Path uploadDir = Paths.get("uploads/teacher-photos");
        Files.createDirectories(uploadDir);

        // delete old photo if exists
        if (teacher.getPhotoFilename() != null) {
            Path oldPath = uploadDir.resolve(teacher.getPhotoFilename());
            Files.deleteIfExists(oldPath);
        }

        String originalFilename = photo.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        }

        String filename = "teacher_" + id + "_" + System.currentTimeMillis() + extension;
        Path path = uploadDir.resolve(filename);
        Files.write(path, photo.getBytes());

        teacher.setPhotoFilename(filename);
        teacherRepository.save(teacher);

        return "redirect:/teachers/" + id + "/edit";
    }

    // ===================== PHOTO DELETE =====================

    @PostMapping("/teachers/{id}/photo/delete")
    public String deletePhoto(@PathVariable Long id) throws IOException {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));

        if (teacher.getPhotoFilename() != null) {
            Path uploadDir = Paths.get("uploads/teacher-photos");
            Path path = uploadDir.resolve(teacher.getPhotoFilename());
            Files.deleteIfExists(path);
            teacher.setPhotoFilename(null);
            teacherRepository.save(teacher);
        }

        return "redirect:/teachers/" + id + "/edit";
    }

    // ===================== BOOKINGS STATUS (teacher side) =====================

    @PostMapping("/teachers/{teacherId}/bookings/{bookingId}/confirm")
    public String confirmBookingForTeacher(@PathVariable Long teacherId,
                                           @PathVariable Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (!booking.getTeacher().getId().equals(teacherId)) {
            throw new IllegalArgumentException("Booking does not belong to this teacher");
        }

        try {
            java.lang.reflect.Field statusField = Booking.class.getDeclaredField("status");
            statusField.setAccessible(true);
            statusField.set(booking, "CONFIRMED");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        bookingRepository.save(booking);

        return "redirect:/teachers/" + teacherId + "/dashboard";
    }

    @PostMapping("/teachers/{teacherId}/bookings/{bookingId}/cancel")
    public String cancelBookingForTeacher(@PathVariable Long teacherId,
                                          @PathVariable Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (!booking.getTeacher().getId().equals(teacherId)) {
            throw new IllegalArgumentException("Booking does not belong to this teacher");
        }

        try {
            java.lang.reflect.Field statusField = Booking.class.getDeclaredField("status");
            statusField.setAccessible(true);
            statusField.set(booking, "CANCELLED");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        bookingRepository.save(booking);

        return "redirect:/teachers/" + teacherId + "/dashboard";
    }

    // ===================== MESSAGES DELETE (teacher side) =====================

    @PostMapping("/teachers/{teacherId}/messages/{messageId}/delete")
    public String deleteMessageForTeacher(@PathVariable Long teacherId,
                                          @PathVariable Long messageId) {

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Message not found"));

        if (!message.getTeacher().getId().equals(teacherId)) {
            throw new IllegalArgumentException("Message does not belong to this teacher");
        }

        messageRepository.delete(message);

        return "redirect:/teachers/" + teacherId + "/dashboard";
    }
}