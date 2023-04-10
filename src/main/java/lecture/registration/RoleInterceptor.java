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

@Slf4j
@Component
@RequiredArgsConstructor
public class RoleInterceptor implements HandlerInterceptor {

  private final LoginRepository loginRepository;
  private final String secretKey = String.valueOf(SecretKey.ThisTokenIsYourLoginTokenAndItIsYourSecretKeyToKeepItSecure);

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler) throws Exception {

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

    if (!acess) {
      response.sendRedirect("/main");
      return false;
    }

    return true;
  }

  private String getRequestURI(HttpServletRequest request) {
    String uri = request.getRequestURI();
    String contextPath = request.getContextPath();
    return uri.substring(contextPath.length());
  }

}
