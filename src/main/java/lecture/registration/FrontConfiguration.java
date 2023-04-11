package lecture.registration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class FrontConfiguration implements WebMvcConfigurer {

  private final RoleInterceptor roleInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    /**
     * 모든 요청에 대해서 interceptor 실행
     * "/main"과 "/login" 제외
     * 생성된 토큰 값이 있는지 검사 및 사용자 구분에 맞는 요청인지 확인
     * false시 "/main"으로 응답
     */
    registry.addInterceptor(roleInterceptor).addPathPatterns("/**")
        .excludePathPatterns("/main", "/login");
  }
}
