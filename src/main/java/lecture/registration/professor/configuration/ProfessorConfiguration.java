package lecture.registration.professor.configuration;

import lecture.registration.manager.repository.RegistrationManageRepository;
import lecture.registration.professor.repository.ProfessorLectureRepository;
import lecture.registration.professor.repository.ProfessorLectureRepositoryImpl;
import lecture.registration.professor.repository.mapper.ProfessorLectureMapper;
import lecture.registration.professor.service.ProfessorLectureService;
import lecture.registration.professor.service.ProfessorLectureServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ProfessorConfiguration {

  private final ProfessorLectureMapper professorLectureMapper;
  private final RegistrationManageRepository registrationManageRepository;

  @Bean
  public ProfessorLectureRepository professorLectureRepository() {
    return new ProfessorLectureRepositoryImpl(professorLectureMapper);
  }

  @Bean
  public ProfessorLectureService professorLectureService() {
    return new ProfessorLectureServiceImpl(professorLectureRepository(), registrationManageRepository);
  }
}
