package pl.milenamrugala.laoshi_hao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import pl.milenamrugala.laoshi_hao.entity.Teacher;
import pl.milenamrugala.laoshi_hao.repository.TeacherRepository;

import java.math.BigDecimal;

@Controller
public class TeacherController {

    private final TeacherRepository teacherRepository;

    public TeacherController(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @GetMapping("/teachers")
    public String teachers(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "maxPrice", required = false) String maxPrice,
            Model model
    ) {
        String query = (q == null) ? "" : q.trim();

        BigDecimal max = null;
        if (maxPrice != null && !maxPrice.trim().isEmpty()) {
            try {
                max = new BigDecimal(maxPrice.trim());
            } catch (NumberFormatException e) {
                max = null;
            }
        }

        model.addAttribute("teachers", teacherRepository.search(query, max));
        model.addAttribute("q", query);
        model.addAttribute("maxPrice", maxPrice);
        return "teachers";
    }

    @GetMapping("/teachers/{id}")
    public String teacherDetails(@PathVariable Long id, Model model) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found with id: " + id));

        model.addAttribute("teacher", teacher);
        model.addAttribute("id", id);
        return "teacher";
    }
}