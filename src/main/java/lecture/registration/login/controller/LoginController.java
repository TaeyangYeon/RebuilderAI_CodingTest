package lecture.registration.login.controller;

import lecture.registration.login.dto.LoginDto;
import lecture.registration.login.service.LoginService;
import lecture.registration.login.service.LoginServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;

/**
 * 로그인 기능을 담당하는 컨트롤러
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

  /**
   * 로그인을 처리하기 위한 기능 클래스를 주입
   */
  private final LoginService loginService;

  /**
   * 로그인을 할 수 있는 페이지로 응답
   * @return 로그인 페이지 : "main"
   */
  @GetMapping("main")
  public String main() {
    return "/main";
  }

  /**
   * 사용자의 id와 사용자 구분값을 받아서 회원 검증
   * 검증 완료되면 사용자 구분에 따라 강의 목록 페이지로 응답
   * @param loginDto 로그인에 필요한 값을 담은 dto클래스 : 'id', 'user'
   * @param response
   * @return
   */
  @PostMapping("login")
  public ResponseEntity<String> login(@RequestBody LoginDto loginDto, HttpServletResponse response) {

    //검증 메서드 매개변수 생성
    Long id = loginDto.getId();
    String user = loginDto.getUser();

    try {
      //사용자 구분과 id에 일치하는 회원 정보 있는지 확인
      loginService.loginMember(user, id, response);
    } catch (LoginServiceImpl.UserNotFoundException e) {
      //일치하는 회원 없을 경우 : 401 응답
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 정보가 일치하지 않음");
    }
    //검증 성공시 성공 응답
    return ResponseEntity.ok("Success");
  }
}
