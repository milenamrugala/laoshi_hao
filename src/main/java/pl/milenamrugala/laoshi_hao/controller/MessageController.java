package pl.milenamrugala.laoshi_hao.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.milenamrugala.laoshi_hao.form.MessageForm;
import pl.milenamrugala.laoshi_hao.repository.MessageRepository;
import pl.milenamrugala.laoshi_hao.repository.TeacherRepository;
import pl.milenamrugala.laoshi_hao.entity.Message;
import pl.milenamrugala.laoshi_hao.entity.Teacher;
import pl.milenamrugala.laoshi_hao.entity.Student;
import pl.milenamrugala.laoshi_hao.repository.StudentRepository;


@Controller
public class MessageController {

    private final TeacherRepository teacherRepository;
    private final MessageRepository messageRepository;
    private final StudentRepository studentRepository;


    public MessageController(TeacherRepository teacherRepository,
                             MessageRepository messageRepository,
                             StudentRepository studentRepository) {
        this.teacherRepository = teacherRepository;
        this.messageRepository = messageRepository;
        this.studentRepository = studentRepository;
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

// find or create Student by username
        Student student = studentRepository.findByUsername(messageForm.getStudentUsername())
                .orElseGet(() -> {
                    Student s = new Student(
                            messageForm.getStudentUsername(),
                            messageForm.getStudentEmail(),
                            messageForm.getStudentFirstName(),
                            messageForm.getStudentLastName(),
                            messageForm.getStudentPhone()
                    );
                    return studentRepository.save(s);
                });

        Message message = new Message(
                teacher,
                messageForm.getStudentFirstName(),
                messageForm.getStudentLastName(),
                messageForm.getStudentUsername(),
                messageForm.getStudentEmail(),
                messageForm.getStudentPhone(),
                messageForm.getContent()
        );

// link to Student entity
        message.setStudent(student);

        messageRepository.save(message);

        // later we can add a "message sent" page; for now go back to teacher profile
        return "redirect:/teachers/" + id;
    }
}
