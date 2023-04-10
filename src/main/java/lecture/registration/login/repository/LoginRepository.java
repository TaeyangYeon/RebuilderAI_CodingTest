package lecture.registration.login.repository;

import lecture.registration.login.model.UserRole;

import javax.servlet.http.HttpServletRequest;

public interface LoginRepository {

  Long student(Long studentId);

  Long professor(Long professorId);

  Long manager(Long managerId);

  String generateToken(Long id, UserRole role);

  Long getUserIdFromToken(HttpServletRequest request);

  UserRole getUserRoleFromToken(HttpServletRequest request);
}
