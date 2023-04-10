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

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

  private final LoginService loginService;

  @GetMapping("main")
  public String main() {
    return "/main";
  }

  @PostMapping("login")
  public ResponseEntity<String> login(@RequestBody LoginDto loginDto, HttpServletResponse response) {

    Long id = loginDto.getId();
    String user = loginDto.getUser();

    try {
      loginService.loginMember(user, id, response);
    } catch (LoginServiceImpl.UserNotFoundException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 정보가 일치하지 않음");
    }

    return ResponseEntity.ok("Success");
  }
}
