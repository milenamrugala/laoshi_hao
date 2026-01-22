package pl.milenamrugala.laoshi_hao.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.milenamrugala.laoshi_hao.entity.Student;
import pl.milenamrugala.laoshi_hao.entity.Teacher;
import pl.milenamrugala.laoshi_hao.form.StudentForm;
import pl.milenamrugala.laoshi_hao.form.TeacherForm;
import pl.milenamrugala.laoshi_hao.repository.StudentRepository;
import pl.milenamrugala.laoshi_hao.repository.TeacherRepository;

import java.util.List;

@Controller
public class RegistrationController {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    // ====== Dropdown / autocomplete data ======
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

    public RegistrationController(StudentRepository studentRepository,
                                  TeacherRepository teacherRepository) {
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
    }

    // =============== STUDENT REGISTER ===============

    @GetMapping("/register/student")
    public String showStudentRegistrationForm(Model model) {
        model.addAttribute("studentForm", new StudentForm());
        return "register-student";
    }

    @PostMapping("/register/student")
    public String handleStudentRegistration(
            @Valid @ModelAttribute("studentForm") StudentForm form,
            BindingResult bindingResult) {

        // unique username
        studentRepository.findByUsername(form.getUsername())
                .ifPresent(s -> bindingResult.rejectValue(
                        "username",
                        "username.exists",
                        "This username is already taken."));

        // unique email
        studentRepository.findByEmail(form.getEmail())
                .ifPresent(s -> bindingResult.rejectValue(
                        "email",
                        "email.exists",
                        "This email is already used."));

        if (bindingResult.hasErrors()) {
            return "register-student";
        }

        Student student = new Student();
        student.setFirstName(form.getFirstName());
        student.setLastName(form.getLastName());
        student.setUsername(form.getUsername());
        student.setEmail(form.getEmail());
        student.setPhone(form.getPhone());

        studentRepository.save(student);

        return "redirect:/students/" + student.getId() + "/dashboard";
    }

    // =============== TEACHER REGISTER ===============

    @GetMapping("/register/teacher")
    public String showTeacherRegistrationForm(Model model) {
        model.addAttribute("teacherForm", new TeacherForm());
        addTeacherDropdownData(model);
        return "register-teacher";
    }

    @PostMapping("/register/teacher")
    public String handleTeacherRegistration(
            @Valid @ModelAttribute("teacherForm") TeacherForm form,
            BindingResult bindingResult,
            Model model) {

        // IMPORTANT: needed when returning back to the form on errors
        addTeacherDropdownData(model);

        // unique username
        teacherRepository.findByUsername(form.getUsername())
                .ifPresent(t -> bindingResult.rejectValue(
                        "username",
                        "username.exists",
                        "This username is already taken."));

        // unique email
        teacherRepository.findByEmail(form.getEmail())
                .ifPresent(t -> bindingResult.rejectValue(
                        "email",
                        "email.exists",
                        "This email is already used."));

        // validate dropdown-like fields against allowed lists
        if (form.getLanguage() != null && !form.getLanguage().isBlank() && !LANGUAGES.contains(form.getLanguage())) {
            bindingResult.rejectValue("language", "invalid.language", "Please select a language from the list.");
        }

        if (form.getNativeLanguage() != null && !form.getNativeLanguage().isBlank() && !LANGUAGES.contains(form.getNativeLanguage())) {
            bindingResult.rejectValue("nativeLanguage", "invalid.nativeLanguage", "Please select a native language from the list.");
        }

        if (form.getNationality() != null && !form.getNationality().isBlank() && !NATIONALITIES.contains(form.getNationality())) {
            bindingResult.rejectValue("nationality", "invalid.nationality", "Please select a nationality from the list.");
        }

        // If city is a datalist (or even a select), enforce allowed values:
        if (form.getCity() != null && !form.getCity().isBlank() && !CITIES.contains(form.getCity())) {
            bindingResult.rejectValue("city", "invalid.city", "Please select a city from the list.");
        }

        if (bindingResult.hasErrors()) {
            return "register-teacher";
        }

        Teacher teacher = new Teacher();
        teacher.setFirstName(form.getFirstName());
        teacher.setLastName(form.getLastName());
        teacher.setUsername(form.getUsername());
        teacher.setEmail(form.getEmail());
        teacher.setPhone(form.getPhone());
        teacher.setLanguage(form.getLanguage());
        teacher.setCity(form.getCity());
        teacher.setCapacity(form.getCapacity());
        teacher.setNativeLanguage(form.getNativeLanguage());
        teacher.setNationality(form.getNationality());
        teacher.setPricePerHour(form.getPricePerHour());

        teacherRepository.save(teacher);

        return "redirect:/teachers/" + teacher.getId() + "/dashboard";
    }

    private void addTeacherDropdownData(Model model) {
        model.addAttribute("languages", LANGUAGES);
        model.addAttribute("nationalities", NATIONALITIES);
        model.addAttribute("cities", CITIES);
    }
}
