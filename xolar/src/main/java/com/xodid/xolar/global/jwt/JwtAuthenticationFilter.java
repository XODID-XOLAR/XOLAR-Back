package com.xodid.xolar.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xodid.xolar.global.exception.CustomException;
import com.xodid.xolar.global.exception.ErrorDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;

    private static final String BEARER = "Bearer ";
    private static final String HEADER = "Authorization";

    /**
     * JWT 인증 필터
     *
     * HTTP 요청을 가로채 JWT 토큰을 검사하고, 유효한 경우 인증 정보를 설정한다.
     * 토큰이 유효하지 않을 경우, CustomException을 던져 에러 응답을 반환한다.
     *
     * @param request HTTP 요청 객체
     * @param response HTTP 요청 객체
     * @param filterChain 필터 체인 객체
     * @throws ServletException 서블릿 예외 발생 시
     * @throws IOException 입출력 예외 발생 시
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 요청 헤더의 Authorization 키 값 조회
        String authorizationHeader = request.getHeader(HEADER);

        // Bearer 접두사를 제거하여 토큰 추출
        String token = getAccessToken(authorizationHeader);

        try{
            // 토큰이 유효한지 확인하고, 유효하다면 인증정보 설정
            if(!ObjectUtils.isEmpty(token) && tokenProvider.isValidToken(token)){
                Authentication authentication = tokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (CustomException e){
            // CustomException이 발생하면 에러 응답을 설정하여 클라이언트에 전송
            ErrorDto errorResponse = ErrorDto.builder()
                    .status(e.getErrorCode().getHttpStatus().value())
                    .code(e.getErrorCode().getCode())
                    .msg(e.getErrorCode().getMessage())
                    .build();

            // 응답의 상태 코드와 Content-Type 및 인코딩 설정
            response.setStatus(e.getErrorCode().getHttpStatus().value());
            response.setContentType("application/json; charset=UTF-8"); // UTF-8 인코딩 설정
            response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
            return;
        }

        // 다음 필터로 요청과 응답 전달
        filterChain.doFilter(request, response);
    }

    /**
     * Authorization 헤더에서 Bearer 접두사를 제거해 토큰 추출
     *
     * @param authorizationHeader Authorization 헤더 값
     * @return Bearer 접두사를 제외한 JWT 토큰 또는 null (유효하지 않은 경우)
     */
    private String getAccessToken(String authorizationHeader){
        // Token이 null이 아니고 Bearer로 시작해야지 정상적인 Token
        if(authorizationHeader != null && authorizationHeader.startsWith(BEARER)){
            // 정상적인 토큰이라면 앞에 Bearer 제거 후 리턴
            return authorizationHeader.substring(BEARER.length());
        }
        return null;
    }
}