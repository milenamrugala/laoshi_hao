package pl.milenamrugala.laoshi_hao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.milenamrugala.laoshi_hao.entity.Teacher;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findByUsername(String username);
    Optional<Teacher> findByEmail(String email);


}
