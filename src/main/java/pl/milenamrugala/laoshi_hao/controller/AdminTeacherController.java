package pl.milenamrugala.laoshi_hao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import pl.milenamrugala.laoshi_hao.form.TeacherForm;
import pl.milenamrugala.laoshi_hao.repository.BookingRepository;
import pl.milenamrugala.laoshi_hao.repository.MessageRepository;
import pl.milenamrugala.laoshi_hao.repository.TeacherRepository;
import pl.milenamrugala.laoshi_hao.entity.Teacher;
import org.springframework.transaction.annotation.Transactional;

@Controller
public class AdminTeacherController {

    private final TeacherRepository teacherRepository;
    private final MessageRepository messageRepository;
    private final BookingRepository bookingRepository;

    public AdminTeacherController(TeacherRepository teacherRepository,
                                  MessageRepository messageRepository,
                                  BookingRepository bookingRepository) {
        this.teacherRepository = teacherRepository;
        this.messageRepository = messageRepository;
        this.bookingRepository = bookingRepository;
    }

    @GetMapping("/admin/teachers")
    public String adminTeachersList(Model model) {
        model.addAttribute("teachers", teacherRepository.findAll());
        return "admin/teachers";
    }

    @GetMapping("/admin/teachers/new")
    public String showNewTeacherForm(Model model) {
        model.addAttribute("teacherForm", new TeacherForm());
        return "admin/teacher-form";
    }

    @PostMapping("/admin/teachers")
    public String createTeacher(@Valid TeacherForm teacherForm,
                                BindingResult bindingResult,
                                Model model) {

        // Check if username already exists
        if (teacherRepository.findByUsername(teacherForm.getUsername()).isPresent()) {
            bindingResult.rejectValue(
                    "username",
                    "error.username",
                    "This username is already taken. Please choose another."
            );
        }

        // If validation errors, redisplay the form
        if (bindingResult.hasErrors()) {
            return "admin/teacher-form";
        }

        // Create new teacher
        Teacher teacher = new Teacher(
                teacherForm.getFirstName(),
                teacherForm.getLastName(),
                teacherForm.getLanguage(),
                teacherForm.getCity()
        );

        teacher.setUsername(teacherForm.getUsername());
        teacher.setEmail(teacherForm.getEmail());
        teacher.setPhone(teacherForm.getPhone());
        teacher.setNationality(teacherForm.getNationality());
        teacher.setNativeLanguage(teacherForm.getNativeLanguage());
        teacher.setCapacity(teacherForm.getCapacity());


        teacherRepository.save(teacher);

        return "redirect:/admin/teachers";
    }

    @GetMapping("/admin/teachers/{id}/edit")
    public String showEditTeacherForm(@PathVariable Long id, Model model) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));

        TeacherForm form = new TeacherForm();
        form.setFirstName(teacher.getFirstName());
        form.setLastName(teacher.getLastName());
        form.setLanguage(teacher.getLanguage());
        form.setCity(teacher.getCity());
        form.setUsername(teacher.getUsername());
        form.setEmail(teacher.getEmail());
        form.setPhone(teacher.getPhone());
        form.setNationality(teacher.getNationality());
        form.setNativeLanguage(teacher.getNativeLanguage());
        form.setCapacity(teacher.getCapacity());

        model.addAttribute("teacherForm", form);
        model.addAttribute("teacherId", id);

        return "admin/teacher-edit";
    }

    @PostMapping("/admin/teachers/{id}/edit")
    public String updateTeacher(@PathVariable Long id,
                                @Valid TeacherForm teacherForm,
                                BindingResult bindingResult,
                                Model model) {

        // check username uniqueness EXCEPT current teacher
        teacherRepository.findByUsername(teacherForm.getUsername())
                .filter(t -> !t.getId().equals(id))
                .ifPresent(t -> bindingResult.rejectValue(
                        "username",
                        "error.username",
                        "This username is already taken. Choose another."
                ));

        if (bindingResult.hasErrors()) {
            model.addAttribute("teacherId", id);
            return "admin/teacher-edit";
        }

        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));

        teacher.setFirstName(teacherForm.getFirstName());
        teacher.setLastName(teacherForm.getLastName());
        teacher.setLanguage(teacherForm.getLanguage());
        teacher.setCity(teacherForm.getCity());
        teacher.setUsername(teacherForm.getUsername());
        teacher.setEmail(teacherForm.getEmail());
        teacher.setPhone(teacherForm.getPhone());
        teacher.setNationality(teacherForm.getNationality());
        teacher.setNativeLanguage(teacherForm.getNativeLanguage());
        teacher.setCapacity(teacherForm.getCapacity());


        teacherRepository.save(teacher);

        return "redirect:/admin/teachers";
    }

    @Transactional
    @PostMapping("/admin/teachers/{id}/delete")
    public String deleteTeacher(@PathVariable Long id) {

        Teacher teacher = teacherRepository.findById(id)
                .orElse(null);

        if (teacher != null) {
            // najpierw kasujemy bookings i messages powiązane z tym nauczycielem
            bookingRepository.deleteByTeacher(teacher);
            messageRepository.deleteByTeacher(teacher);

            // dopiero potem nauczyciela
            teacherRepository.delete(teacher);
        }

        return "redirect:/admin/teachers";
    }
}