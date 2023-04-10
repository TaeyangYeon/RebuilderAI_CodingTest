package lecture.registration.login.repository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lecture.registration.login.model.SecretKey;
import lecture.registration.login.model.UserRole;
import lecture.registration.login.repository.mapper.LoginMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Date;

@Repository
@RequiredArgsConstructor
public class LoginRepositoryImpl implements LoginRepository {

  private final LoginMapper loginMapper;
  private final String secretKey = String.valueOf(SecretKey.ThisTokenIsYourLoginTokenAndItIsYourSecretKeyToKeepItSecure);

  @Override
  public Long student(Long studentId) {
    return loginMapper.student(studentId);
  }

  @Override
  public Long professor(Long professorId) {
    return loginMapper.professor(professorId);
  }

  @Override
  public Long manager(Long managerId) {
    return loginMapper.manager(managerId);
  }

  @Override
  public String generateToken(Long id, UserRole role) {
    Instant now = Instant.now();
    return Jwts.builder()
        .setSubject(id.toString())
        .claim("role", role)
        .setIssuedAt(Date.from(now))
        .setExpiration(Date.from(now.plusSeconds(3600)))
        .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
        .compact();
  }

  @Override
  public Long getUserIdFromToken(HttpServletRequest request) {
    Claims claims = getClaims(request);
    return Long.parseLong(claims.getSubject());
  }

  @Override
  public UserRole getUserRoleFromToken(HttpServletRequest request) {
    Claims claims = getClaims(request);
    return UserRole.valueOf((String) claims.get("role"));
  }

  private Claims getClaims(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    String token = null;

    for (Cookie cookie : cookies) {
      if (cookie.getName().equals("token")) {
        token = cookie.getValue();
        break;
      }
    }

    Claims claims = Jwts.parserBuilder()
        .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
        .build()
        .parseClaimsJws(token)
        .getBody();
    return claims;
  }

}
