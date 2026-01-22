package pl.milenamrugala.laoshi_hao.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.milenamrugala.laoshi_hao.repository.TeacherRepository;
import pl.milenamrugala.laoshi_hao.entity.Teacher;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(TeacherRepository teacherRepository) {
        return args -> {
            if (teacherRepository.count() == 0) {

                Teacher t1 = new Teacher("Magdalena", "Nowak", "English", "Warsaw");
                t1.setUsername("magda.eng");
                t1.setEmail("magda@example.com");
                t1.setPhone("+48123456789");
                t1.setNationality("Polish");
                t1.setNativeLanguage("Polish");
                t1.setCapacity(3);
                t1.setCreatedAt();
                teacherRepository.save(t1);

                Teacher t2 = new Teacher("Rodrigo", "Lopez", "Spanish", "Cracow");
                t2.setUsername("rodrigo.spanish");
                t2.setEmail("rodrigo@example.com");
                t2.setPhone("+34987654321");
                t2.setNationality("Spanish");
                t2.setNativeLanguage("Spanish");
                t2.setCapacity(3);
                t2.setCreatedAt();
                teacherRepository.save(t2);

                Teacher t3 = new Teacher("Dongming", "Li", "Chinese", "Online");
                t3.setUsername("dongming.cn");
                t3.setEmail("dongming@example.com");
                t3.setPhone("+86555555555");
                t3.setNationality("Chinese");
                t3.setNativeLanguage("Chinese");
                t3.setCapacity(3);
                t3.setCreatedAt();
                teacherRepository.save(t3);
            }
        };
    }
}