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

    registry.addInterceptor(roleInterceptor).addPathPatterns("/**")
        .excludePathPatterns("/main", "/login");
  }
}
