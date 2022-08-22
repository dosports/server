package umc.dosports.jwt;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import umc.dosports.User.User;
import java.util.Date;

public class TokenProvider {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final String issuer = "dosports";
    private String secretKey = "";
    private String tokenPrefix = "Bearer";
    // 액세스 토큰 유효시간 60분
    private long accessTokenValidTime = 60 * 60 * 1000L;
    // 리프레쉬 토큰 유효시간 15일
    private long refreshTokenValidTime = 60 * 60 * 24 * 15 * 1000L;

    public String createAccessToken(Long userIdx) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(userIdx));
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer(issuer)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String createRefreshToken() {
        Date now = new Date();

        return Jwts.builder()
                .setIssuer(issuer)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean isValidToken(String accessToken) {
        Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(accessToken);
        return !claims.getBody().getExpiration().before(new Date());
    }

    // 토큰으로 사용자 인덱스 조회
    public Long getUserIdx(String token) {
        Long userIdx = Long.valueOf(Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject());
        return userIdx;
    }

    // refresh Token 만료 체크 후 재발급
    public Boolean reGenerateRefreshToken(User user) throws Exception {
        String refreshToken = user.getRefreshToken();

        if (refreshToken == null) {
            log.info("refreshToken 정보가 존재하지 않습니다.");
            return false;
        }

        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(refreshToken.substring(7));
            log.info("refresh token이 만료되지 않았습니다.");

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}