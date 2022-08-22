package umc.dosports.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import umc.dosports.jwt.BearerAuthInterceptor;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);

    private final BearerAuthInterceptor bearerAuthInterceptor;

    public AppConfig(BearerAuthInterceptor bearerAuthInterceptor) { // 인터셉터 구현체를 만들어 등록함
        this.bearerAuthInterceptor = bearerAuthInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        logger.info(">>> 인터셉터 등록");
        registry.addInterceptor(bearerAuthInterceptor)
//                .addPathPatterns("/like*")
//                .addPathPatterns("/comment*")
//                .addPathPatterns("/review*")
                .addPathPatterns("/user/mypage")
                .addPathPatterns("/user/updateAccessToken")
                .addPathPatterns("/user/delete")
                .addPathPatterns("/user/name")
                .addPathPatterns("/user/profileImg")
                .addPathPatterns("/user/info");
    }
}