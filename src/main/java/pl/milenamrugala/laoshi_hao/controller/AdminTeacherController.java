package pl.milenamrugala.laoshi_hao.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.milenamrugala.laoshi_hao.entity.Teacher;
import pl.milenamrugala.laoshi_hao.form.TeacherForm;
import pl.milenamrugala.laoshi_hao.repository.BookingRepository;
import pl.milenamrugala.laoshi_hao.repository.MessageRepository;
import pl.milenamrugala.laoshi_hao.repository.TeacherRepository;

import java.util.List;

@Controller
public class AdminTeacherController {

    private final TeacherRepository teacherRepository;
    private final MessageRepository messageRepository;
    private final BookingRepository bookingRepository;

    // ====== Dropdown data ======
    private static final List<String> LANGUAGES = List.of(
            "English", "Chinese", "Polish", "German", "Spanish", "French",
            "Italian", "Japanese", "Korean"
    );

    private static final List<String> NATIONALITIES = List.of(
            "Polish", "Chinese", "German", "Spanish", "French", "Italian",
            "Japanese", "Korean", "Ukrainian", "American", "British"
    );

    private static final List<String> CITIES = List.of(
            "Online", "Warsaw", "Krakow", "Wroclaw", "Gdansk", "Poznan", "Lodz"
    );

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
        addTeacherDropdownData(model);
        return "admin/teacher-form";
    }

    @PostMapping("/admin/teachers")
    public String createTeacher(@Valid @ModelAttribute("teacherForm") TeacherForm teacherForm,
                                BindingResult bindingResult,
                                Model model) {

        // always needed on errors (so selects are filled)
        addTeacherDropdownData(model);

        // Check if username already exists
        teacherRepository.findByUsername(teacherForm.getUsername())
                .ifPresent(t -> bindingResult.rejectValue(
                        "username",
                        "error.username",
                        "This username is already taken. Please choose another."
                ));

        // Check if email already exists (recommended, like in other controllers)
        teacherRepository.findByEmail(teacherForm.getEmail())
                .ifPresent(t -> bindingResult.rejectValue(
                        "email",
                        "error.email",
                        "This email is already used. Please choose another."
                ));

        // validate dropdown-like fields against allowed lists
        validateTeacherDropdownFields(teacherForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "admin/teacher-form";
        }

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

        addTeacherDropdownData(model);

        return "admin/teacher-edit";
    }

    @PostMapping("/admin/teachers/{id}/edit")
    public String updateTeacher(@PathVariable Long id,
                                @Valid @ModelAttribute("teacherForm") TeacherForm teacherForm,
                                BindingResult bindingResult,
                                Model model) {

        // always needed on errors (so selects are filled)
        model.addAttribute("teacherId", id);
        addTeacherDropdownData(model);

        // check username uniqueness EXCEPT current teacher
        teacherRepository.findByUsername(teacherForm.getUsername())
                .filter(t -> !t.getId().equals(id))
                .ifPresent(t -> bindingResult.rejectValue(
                        "username",
                        "error.username",
                        "This username is already taken. Choose another."
                ));

        // check email uniqueness EXCEPT current teacher
        teacherRepository.findByEmail(teacherForm.getEmail())
                .filter(t -> !t.getId().equals(id))
                .ifPresent(t -> bindingResult.rejectValue(
                        "email",
                        "error.email",
                        "This email is already used. Choose another."
                ));

        // validate dropdown-like fields against allowed lists
        validateTeacherDropdownFields(teacherForm, bindingResult);

        if (bindingResult.hasErrors()) {
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

        Teacher teacher = teacherRepository.findById(id).orElse(null);

        if (teacher != null) {
            bookingRepository.deleteByTeacher(teacher);
            messageRepository.deleteByTeacher(teacher);
            teacherRepository.delete(teacher);
        }

        return "redirect:/admin/teachers";
    }

    // ===================== helpers =====================

    private void addTeacherDropdownData(Model model) {
        model.addAttribute("languages", LANGUAGES);
        model.addAttribute("nationalities", NATIONALITIES);
        model.addAttribute("cities", CITIES);
    }

    private void validateTeacherDropdownFields(TeacherForm form, BindingResult bindingResult) {
        if (form.getLanguage() != null && !form.getLanguage().isBlank() && !LANGUAGES.contains(form.getLanguage())) {
            bindingResult.rejectValue("language", "invalid.language", "Please select a language from the list.");
        }

        if (form.getNativeLanguage() != null && !form.getNativeLanguage().isBlank() && !LANGUAGES.contains(form.getNativeLanguage())) {
            bindingResult.rejectValue("nativeLanguage", "invalid.nativeLanguage", "Please select a native language from the list.");
        }

        if (form.getNationality() != null && !form.getNationality().isBlank() && !NATIONALITIES.contains(form.getNationality())) {
            bindingResult.rejectValue("nationality", "invalid.nationality", "Please select a nationality from the list.");
        }

        if (form.getCity() != null && !form.getCity().isBlank() && !CITIES.contains(form.getCity())) {
            bindingResult.rejectValue("city", "invalid.city", "Please select a city from the list.");
        }
    }
}
