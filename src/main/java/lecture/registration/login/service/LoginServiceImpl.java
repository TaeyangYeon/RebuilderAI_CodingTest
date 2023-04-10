package lecture.registration.login.service;

import lecture.registration.login.model.UserRole;
import lecture.registration.login.repository.LoginRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * 회원 검증 및 로그인 기능을 담은 service 클래스
 * 회원 검증 섬공시 토큰 생성하여 쿠키에 저장
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService{


  /**
   * 로그인에 필요한 기능을 담은 respository 클래스 주입
   */
  private final LoginRepository loginRepository;

  //회원 검증 및 토큰 생성 메서드
  @Override
  public void loginMember(String user, Long id, HttpServletResponse response) {

    Long userId = null;
    UserRole role = null;

    /**
     * 사용자 구분에 따라 회원 조회 진행 : 해당 구분의 테이블에서 데이터 조회
     * 토큰에 저장할 role값은 해당 사용자 구분값으로
     */
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

    //위에 검증에서 성공시 토큰 생성 후 토큰을 쿠키에 저장. (성공시, userId = id)
    if (userId != null) {
      String token = loginRepository.generateToken(id, role);
      Cookie cookie = new Cookie("token", token);
      cookie.setPath("/");
      response.addCookie(cookie);
    } else {
      //회원 검증 실패 (사용가 입력값 오류). exception 전달
      throw new UserNotFoundException("일치하는 사용자가 없습니다.");
    }
  }

  //회원 검증 실패시 (사용자 입력값 오류)에 대한 exception 클래스
  public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
      super(message);
    }
  }
}
