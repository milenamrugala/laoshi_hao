package pl.milenamrugala.laoshi_hao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByTeacherIdOrderByCreatedAtDesc(Long teacherId);
    List<Message> findAllByOrderByCreatedAtDesc();
}