package pl.milenamrugala.laoshi_hao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.milenamrugala.laoshi_hao.entity.Message;
import pl.milenamrugala.laoshi_hao.entity.Teacher;
import pl.milenamrugala.laoshi_hao.entity.Student;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByTeacherIdOrderByCreatedAtDesc(Long teacherId);

    List<Message> findAllByOrderByCreatedAtDesc();

    void deleteByTeacher(Teacher teacher);

    void deleteByStudent(Student student);

    List<Message> findByStudentIdOrderByCreatedAtDesc(Long studentId);
}