package com.xodid.xolar.user.service;

import com.xodid.xolar.global.exception.CustomException;
import com.xodid.xolar.global.exception.ErrorCode;
import com.xodid.xolar.global.jwt.TokenProvider;
import com.xodid.xolar.user.domain.User;
import com.xodid.xolar.user.dto.TokenResponseDto;
import com.xodid.xolar.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * AccessToken 재발급
     *
     * 클라이언트가 전달한 리프레시 토큰을 검증해 유효한 경우 새로운 액세스토큰을 발급한다.
     *
     * @param refreshToken 클라이언트에서 전달된 리프레시 토큰
     * @return 새로운 AccessToken을 포함한 TokenResponseDto 객체
     */
    public TokenResponseDto reissueAccessToken(String refreshToken){
        // 전달받은 리프레시 토큰에서 이메일을 추출하여 사용자 정보 가져오기
        String email = tokenProvider.extractEmail(refreshToken);
        User user = getUserByEmail(email);

        // Redis에서 해당 사용자 Id를 키로 하는 리프래시 토큰 가져오기
        String storedRefreshToken = redisTemplate.opsForValue().get(user.getUserId().toString());

        // 전달받은 리프레시 토큰과 Redis에 저장된 리프레시 토큰이 일치하는지 확인
        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)){
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        // 일치한다면 새로운 AccessToken 생성
        String accessToken = tokenProvider.createAccessToken(user);

        return TokenResponseDto.builder()
                .accessToken(accessToken)
                .build();
    }

    /* Transaction 함수들 */

    // id로 User 조회
    @Transactional(readOnly = true)
    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                ()-> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    // Email로 사용자 객체 가져오기
    @Transactional(readOnly = true)
    public User getUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_EXIST));
    }
}
