package pl.milenamrugala.laoshi_hao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.milenamrugala.laoshi_hao.entity.Student;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByUsername(String username);
    Optional<Student> findByEmail(String email);
}