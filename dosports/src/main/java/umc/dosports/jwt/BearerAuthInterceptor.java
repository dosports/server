package umc.dosports.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class BearerAuthInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(BearerAuthInterceptor.class);

    private AuthorizationExtractor authorizationExtractor;
    @Autowired
    private TokenProvider tokenProvider;


//    @Bean
//    public TokenProvider tokenProvider() {
//        return new TokenProvider();
//    }

    public BearerAuthInterceptor(AuthorizationExtractor authorizationExtractor, TokenProvider tokenProvider) {
        this.authorizationExtractor = authorizationExtractor;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        logger.info(">>> interceptor.preHandle 호출");
        String token = authorizationExtractor.extract(request, "Bearer");
        logger.info("토큰값: " + token);
        //우리는 토큰 비어 있어도 모든 페이지 접근 가능하기 때문에 token.isEmpty() 검증할 필요 없겠지?
//        if (token.isEmpty()) {
//            return true;
//        }

        if (!tokenProvider.isValidToken(token)) {
            throw new IllegalArgumentException("유효하지 않은 토큰");
        }

        System.out.println("토큰 유효함");
        Long userIdx = tokenProvider.getUserIdx(token);
        request.setAttribute("userIdx", userIdx);
        return true;
    }
}