package pl.milenamrugala.laoshi_hao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.milenamrugala.laoshi_hao.entity.Teacher;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Optional<Teacher> findByUsername(String username);
    Optional<Teacher> findByEmail(String email);

    @Query("""
        SELECT t FROM Teacher t
        WHERE
            (
                :q IS NULL OR :q = ''
                OR LOWER(t.language) LIKE LOWER(CONCAT('%', :q, '%'))
                OR LOWER(t.city) LIKE LOWER(CONCAT('%', :q, '%'))
                OR LOWER(t.nationality) LIKE LOWER(CONCAT('%', :q, '%'))
            )
            AND (
                :maxPrice IS NULL
                OR (t.pricePerHour IS NOT NULL AND t.pricePerHour <= :maxPrice)
            )
        ORDER BY t.lastName ASC, t.firstName ASC
    """)
    List<Teacher> search(@Param("q") String q,
                         @Param("maxPrice") BigDecimal maxPrice);
}