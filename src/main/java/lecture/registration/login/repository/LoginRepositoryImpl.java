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

/**
 * 로그안 기능 구현을 위한 기능 repository 클래스
 */
@Repository
@RequiredArgsConstructor
public class LoginRepositoryImpl implements LoginRepository {

  /**
   * 쿼리를 담은 mapper 클래스 주입
   * 토큰 생성을 위한 secretkey 생성
   */
  private final LoginMapper loginMapper;
  private final String secretKey = String.valueOf(SecretKey.ThisTokenIsYourLoginTokenAndItIsYourSecretKeyToKeepItSecure);

  //student에서 조회
  @Override
  public Long student(Long studentId) {
    return loginMapper.student(studentId);
  }

  //professor에서 조회
  @Override
  public Long professor(Long professorId) {
    return loginMapper.professor(professorId);
  }

  //manager에서 조회
  @Override
  public Long manager(Long managerId) {
    return loginMapper.manager(managerId);
  }

  /**
   * 토큰 생성 메서드
   * 사용자 구분값과 id를 토큰으로 생성
   */
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

  //토큰에서 사용자 아이디를 꺼내는 메서드
  @Override
  public Long getUserIdFromToken(HttpServletRequest request) {
    Claims claims = getClaims(request);
    return Long.parseLong(claims.getSubject());
  }

  //토큰에서 사용자 구분값을 꺼내는 메서드
  @Override
  public UserRole getUserRoleFromToken(HttpServletRequest request) {
    Claims claims = getClaims(request);
    return UserRole.valueOf((String) claims.get("role"));
  }

  //저장소에서 'token'이름의 쿠키를 첮아 그 값을 claims으로 선언하여 반환하는 메서드
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
