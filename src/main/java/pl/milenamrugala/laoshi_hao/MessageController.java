package pl.milenamrugala.laoshi_hao;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MessageController {

    private final TeacherRepository teacherRepository;
    private final MessageRepository messageRepository;

    public MessageController(TeacherRepository teacherRepository,
                             MessageRepository messageRepository) {
        this.teacherRepository = teacherRepository;
        this.messageRepository = messageRepository;
    }

    @GetMapping("/teachers/{id}/message")
    public String showMessageForm(@PathVariable Long id, Model model) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));

        MessageForm form = new MessageForm();

        model.addAttribute("teacher", teacher);
        model.addAttribute("messageForm", form);

        return "message-form";
    }

    @PostMapping("/teachers/{id}/message")
    public String sendMessage(@PathVariable Long id,
                              @Valid MessageForm messageForm,
                              BindingResult bindingResult,
                              Model model) {

        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));

        if (bindingResult.hasErrors()) {
            model.addAttribute("teacher", teacher);
            return "message-form";
        }

        Message message = new Message(
                teacher,
                messageForm.getStudentFirstName(),
                messageForm.getStudentLastName(),
                messageForm.getStudentUsername(),
                messageForm.getStudentEmail(),
                messageForm.getStudentPhone(),
                messageForm.getContent()
        );

        messageRepository.save(message);

        // later we can add a "message sent" page; for now go back to teacher profile
        return "redirect:/teachers/" + id;
    }
}
