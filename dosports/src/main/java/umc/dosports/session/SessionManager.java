//package umc.dosports.session;
//
//import org.springframework.stereotype.Component;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.Arrays;
//import java.util.Map;
//import java.util.UUID;
//import java.util.concurrent.ConcurrentHashMap;
//import javax.servlet.http.Cookie;
//
///**
// * 세션 관리
// */
//@Component // 스프링 빈으로 자동 등록
//public class SessionManager {
//
//    public static final String SESSION_COOKIE_NAME = "mySessionId"; // 사용할 곳이 많아서 상수로 만듦
//    // 동시성 이슈 있을 때(여러 쓰레드 접근 이슈)
//    // HashMap 은 동시 요청에 안전하지 않다. 동시 요청에 안전한 ConcurrentHashMap 를 사용
//    private Map<String, Object> sessionStore = new ConcurrentHashMap<>();
//
//    /**
//     * 세션 생성
//     */
//    public void createSession(Object value, HttpServletResponse response) {
//
//        //세션 id를 생성하고, 값을 세션에 저장
//        String sessionId = UUID.randomUUID().toString(); // 임의의 토큰
//        sessionStore.put(sessionId, value);
//
//        //쿠키 생성
//        Cookie mySessionCookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
//        response.addCookie(mySessionCookie);
//    }
//
//    /**
//     * 세션 조회
//     */
//    public Object getSession(HttpServletRequest request) {
//        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
//        if (sessionCookie == null) {
//            return null;
//        }
//        return sessionStore.get(sessionCookie.getValue());
//    }
//
//    /**
//     * 세션 만료
//     */
//    public void expire(HttpServletRequest request) {
//        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
//        if (sessionCookie != null) {
//            sessionStore.remove(sessionCookie.getValue());
//        }
//    }
//
//
//    // 쿠키 찾는 부분 분리함
//    public Cookie findCookie(HttpServletRequest request, String cookieName) {
//        if (request.getCookies() == null) {
//            return null;
//        }
//        // Arrays.stream() : 배열을 스트림으로 바꿔줌, 배열의 값을 하나씩 루프 돌림
//        return Arrays.stream(request.getCookies())
//                .filter(cookie -> cookie.getName().equals(cookieName))
//                .findAny()// 순서 상관없이 하나만 찾아서 반환
//                .orElse(null);
//    }
//
//}