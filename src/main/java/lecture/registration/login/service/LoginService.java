package lecture.registration.login.service;

import javax.servlet.http.HttpServletResponse;

public interface LoginService {

  void loginMember(String user, Long id, HttpServletResponse response);

}
