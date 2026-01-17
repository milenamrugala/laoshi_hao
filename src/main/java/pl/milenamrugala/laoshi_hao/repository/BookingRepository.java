package pl.milenamrugala.laoshi_hao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.milenamrugala.laoshi_hao.entity.Booking;
import pl.milenamrugala.laoshi_hao.entity.Teacher;
import pl.milenamrugala.laoshi_hao.entity.Student;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByTeacherIdOrderByLessonDateAscLessonTimeAsc(Long teacherId);

    List<Booking> findAllByOrderByLessonDateAscLessonTimeAsc();

    void deleteByTeacher(Teacher teacher);

    void deleteByStudent(Student student);

    List<Booking> findByStudentIdOrderByLessonDateAscLessonTimeAsc(Long studentId);
}