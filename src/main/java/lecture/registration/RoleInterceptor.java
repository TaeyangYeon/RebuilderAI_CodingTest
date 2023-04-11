package lecture.registration;

import lecture.registration.login.model.SecretKey;
import lecture.registration.login.model.UserRole;
import lecture.registration.login.repository.LoginRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 로그인 상태 검증 및 사용자 구분에 맞는 요청인지 검증하는 인터셉터
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RoleInterceptor implements HandlerInterceptor {

  //저장된 토큰에서 사용자 구분 값을 가져오기 위한 클래스 주입
  private final LoginRepository loginRepository;
  private final String secretKey = String.valueOf(SecretKey.ThisTokenIsYourLoginTokenAndItIsYourSecretKeyToKeepItSecure);

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler) throws Exception {

    //쿠키 안에 토큰이 있는지 확인 후 없으면 "/main"으로
    boolean out = false;
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals("token")) {
          out = true;
        }
      }
      if (!out) {
        response.sendRedirect("/main");
        return false;
      }
    } else {
      response.sendRedirect("/main");
      return false;
    }

    UserRole userRole = loginRepository.getUserRoleFromToken(request);
    String requestPage = getRequestURI(request);

    //사용자 구분값에 맞는 요청인지 확인
    boolean acess = false;
    switch (userRole) {
      case STUDENT:
        acess = requestPage.startsWith("/student");
        break;
      case PROFESSOR:
        acess = requestPage.startsWith("/professor");
        break;
      case MANAGER:
        acess = requestPage.startsWith("/manager");
        break;
      default:
        break;
    }

    //사용자 구분값에 맞지 않은 요청일 경우 "/main"으로 응답
    if (!acess) {
      response.sendRedirect("/main");
      return false;
    }

    //전체 성공시 요청한 uri로 응답
    return true;
  }

  private String getRequestURI(HttpServletRequest request) {
    String uri = request.getRequestURI();
    String contextPath = request.getContextPath();
    return uri.substring(contextPath.length());
  }

}
