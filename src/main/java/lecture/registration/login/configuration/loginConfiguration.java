package lecture.registration.login.configuration;

import lecture.registration.login.repository.LoginRepository;
import lecture.registration.login.repository.LoginRepositoryImpl;
import lecture.registration.login.repository.mapper.LoginMapper;
import lecture.registration.login.service.LoginService;
import lecture.registration.login.service.LoginServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class loginConfiguration {

  private final LoginMapper loginMapper;

  @Bean
  public LoginRepository loginRepository() {
    return new LoginRepositoryImpl(loginMapper);
  }

  @Bean
  public LoginService loginService() {
    return new LoginServiceImpl(loginRepository());
  }
}
