package lecture.registration.login.service;

import lecture.registration.login.model.SecretKey;
import lecture.registration.login.model.UserRole;
import lecture.registration.login.repository.LoginRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService{

  private final LoginRepository loginRepository;
  private final String secretKey = String.valueOf(SecretKey.ThisTokenIsYourLoginTokenAndItIsYourSecretKeyToKeepItSecure);

  @Override
  public void loginMember(String user, Long id, HttpServletResponse response) {

    Long userId = null;
    UserRole role = null;

    switch(user) {
      case "student" :
        userId = loginRepository.student(id);
        role = UserRole.STUDENT;
        break;
      case "professor" :
        userId = loginRepository.professor(id);
        role = UserRole.PROFESSOR;
        break;
      case "manager" :
        userId = loginRepository.manager(id);
        role = UserRole.MANAGER;
        break;
    }

    if (userId != null) {
      String token = loginRepository.generateToken(id, role);
      Cookie cookie = new Cookie("token", token);
      cookie.setPath("/");
      response.addCookie(cookie);
    } else {
      throw new UserNotFoundException("일치하는 사용자가 없습니다.");
    }
  }

  public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
      super(message);
    }
  }
}
