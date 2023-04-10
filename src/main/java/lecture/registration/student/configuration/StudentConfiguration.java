package lecture.registration.student.configuration;

import lecture.registration.manager.repository.RegistrationManageRepository;
import lecture.registration.student.repository.RegistrationLectureRepository;
import lecture.registration.student.repository.RegistrationLectureRepositoryImpl;
import lecture.registration.student.repository.mapper.RegistrationLectureMapper;
import lecture.registration.student.service.RegistrationLectureService;
import lecture.registration.student.service.RegistrationLectureServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class StudentConfiguration {

  private final RegistrationLectureMapper registrationLectureMapper;
  private final RegistrationManageRepository registrationDateRepository;

  @Bean
  public RegistrationLectureRepository registrationLectureRepository() {
    return new RegistrationLectureRepositoryImpl(registrationLectureMapper);
  }

  @Bean
  public RegistrationLectureService registrationLectureService() {
    return new RegistrationLectureServiceImpl(registrationLectureRepository(), registrationDateRepository);
  }
}
