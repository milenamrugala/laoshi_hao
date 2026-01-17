package pl.milenamrugala.laoshi_hao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.milenamrugala.laoshi_hao.repository.MessageRepository;
import pl.milenamrugala.laoshi_hao.repository.TeacherRepository;
import pl.milenamrugala.laoshi_hao.entity.Message;
import pl.milenamrugala.laoshi_hao.entity.Teacher;

import java.util.List;

@Controller
public class AdminMessageController {

    private final MessageRepository messageRepository;
    private final TeacherRepository teacherRepository;

    public AdminMessageController(MessageRepository messageRepository,
                                  TeacherRepository teacherRepository) {
        this.messageRepository = messageRepository;
        this.teacherRepository = teacherRepository;
    }

    @GetMapping("/admin/messages")
    public String listMessages(Model model) {
        List<Message> messages = messageRepository.findAllByOrderByCreatedAtDesc();
        model.addAttribute("messages", messages);
        return "admin/messages";
    }

    @GetMapping("/admin/teachers/{id}/messages")
    public String listMessagesForTeacher(@PathVariable Long id, Model model) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));

        List<Message> messages = messageRepository.findByTeacherIdOrderByCreatedAtDesc(id);

        model.addAttribute("teacher", teacher);
        model.addAttribute("messages", messages);

        return "admin/teacher-messages";
    }

    @PostMapping("/admin/messages/{id}/delete")
    public String deleteMessage(@PathVariable Long id,
                                @RequestParam(required = false) Long teacherId) {

        if (messageRepository.existsById(id)) {
            messageRepository.deleteById(id);
        }

        if (teacherId != null) {
            // came from "messages for specific teacher"
            return "redirect:/admin/teachers/" + teacherId + "/messages";
        }

        // default: came from "all messages"
        return "redirect:/admin/messages";
    }
}