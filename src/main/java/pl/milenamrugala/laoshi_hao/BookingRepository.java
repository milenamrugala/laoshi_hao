package pl.milenamrugala.laoshi_hao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByTeacherIdOrderByLessonDateAscLessonTimeAsc(Long teacherId);
    List<Booking> findAllByOrderByLessonDateAscLessonTimeAsc();
}