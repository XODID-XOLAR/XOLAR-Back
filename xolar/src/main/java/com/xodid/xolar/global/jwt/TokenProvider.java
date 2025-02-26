package com.xodid.xolar.global.jwt;

import com.xodid.xolar.global.exception.CustomException;
import com.xodid.xolar.global.exception.ErrorCode;
import com.xodid.xolar.user.domain.User;
import com.xodid.xolar.user.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SecurityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenProvider {

    // application.yml에 저장한 jwt값 가져오기
    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.issuer}")
    private String issuer;

    // 토큰 만료시간 설정
    private static Long accessTokenExpiration = 1000*60*60L; // 1시간 = 1000(ms->s) * 60(s->m) * 60(m->h)
    private static Long refreshTokenExpiration = 1000*60*60*25*14L; // 2주 = 1000(ms->s) * 60(s->m) * 60(m->h) * 24(h->하루) * 14(2주)

    // 토큰에 포함할 기본 정보와 클레임 키값 설정
    private static final String AUTH_CLAIM = "auth";

    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * AccessToken 생성 메소드
     * 사용자의 이메일과 권한 정보를 포함해 AccessToken을 생성
     *
     * @param user 토큰에 포함할 사용자 정보
     * @return 생성된 액세스 토큰
     */
    public String createAccessToken(User user){
        Date now = new Date();
        return Jwts.builder()
                // 헤더 - 토큰 타입: JWT
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                // 내용 - 토큰 발급자
                .setIssuer(issuer)
                // 내용 - 토큰이 발급된 시간: 현재 시간
                .setIssuedAt(now)
                // 내용 - 토큰 만료 시간: expiredMs 변수값
                .setExpiration(new Date(now.getTime() + accessTokenExpiration))
                // 내용 - 토큰 제목: 사용자 이메일
                .setSubject(user.getEmail())
                // 서명 - 시크릿키와 함께 해시값을 HS256 방식으로 암호화
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * RefreshToken 생성 메소드
     * RefreshToken에는 권한이 필요하지 않으므로 제외
     *
     * @return 생성된 리프레시 토큰
     */
    public String createRefreshToken(User user){
        Date now = new Date();
        return Jwts.builder()
                // 헤더 - 토큰 타입: JWT
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                // 내용 - 토큰 발급자
                .setIssuer(issuer)
                // 내용 - 토큰이 발급된 시간: 현재 시간
                .setIssuedAt(now)
                // 내용 - 토큰 만료 시간: expiredMs 변수값
                .setExpiration(new Date(now.getTime() + refreshTokenExpiration))
                // 내용 - 토큰 제목: 사용자 이메일
                .setSubject(user.getEmail())
                // 서명 - 시크릿키와 함께 해시값을 HS256 방식으로 암호화
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * Redis에 리프레시 토큰을 저장하는 메소드
     * key: 사용자 ID, alue: 리프레시 토큰
     * 리프레시토큰 만료 시간(refreshTokenExpiration)을 만료시간으로 정해 자동으로 삭제되도록 설정
     *
     * @param userId 저장할 사용자 Id (Redis 키로 사용)
     * @param refreshToken 저장할 리프레시 토큰
     */
    public void saveRefreshToken(Long userId, String refreshToken){
        redisTemplate.opsForValue().set(userId.toString(),refreshToken, Duration.ofMillis(refreshTokenExpiration));
    }

    /**
     * AccessToken에서 email 추출
     * 토큰이 유효한지 확인 후 이메일 클레임 값을 추출
     *
     * @param accessToken 이메일을 추출할 액세스 토큰
     * @return 추출된 이메일 또는 null (유효하지 않은 경우)
     */
    public String extractEmail(String accessToken){
        if(isValidToken(accessToken)){
            return getClaims(accessToken).getSubject();
        }
        return null;
    }

    /**
     * 유효한 토큰인지 검증
     * 유효하지 않은 경우 CustomException을 발생시킴
     *
     * @param token 검증할 토큰
     * @return 유효성
     */
    public boolean isValidToken(String token){
        try{
            // secretKey를 사용해 토큰 복호화
            Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            log.info("Validate token success");
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT token", e);
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token", e);
            throw new CustomException(ErrorCode.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token", e);
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty", e);
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }

    /**
     * 토큰에서 사용자 인증 정보를 꺼내 반환
     * JWT 토큰의 클레임에서 사용자 이메일과 권한 정보를 추출하여 Authentication 객체를 생성
     *
     * @param token 인증 정보를 추출할 토큰
     * @return 생성된 Authentication 객체
     */
    public Authentication getAuthentication(String token){
        // 토큰 복호화
        Claims claims = getClaims(token);

        // 토큰에서 정보를 꺼냄
        Set<SimpleGrantedAuthority> authorities = Collections
                .singleton(new SimpleGrantedAuthority("ROLE_"+claims.get(AUTH_CLAIM)));

        return new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails
                .User(claims.getSubject(), "", authorities), token, authorities);
    }

    /**
     * 토큰을 복호화한 후 페이로드 반환
     *
     * @param token 복호화할 토큰
     * @return 토큰의 페이로드
     */
    private Claims getClaims(String token){
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getPayload();
    }
}
