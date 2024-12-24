package com.xodid.xolar.user.controller;

import com.xodid.xolar.user.dto.TokenRequestDto;
import com.xodid.xolar.user.dto.TokenResponseDto;
import com.xodid.xolar.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("auth")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * 액세스토큰 재발급 처리 메서드
     */
    @PostMapping("/token")
    public ResponseEntity<TokenResponseDto> reissuedAccessToken(@RequestBody TokenRequestDto requestDto){
        return ResponseEntity.status(HttpStatus.OK).body(userService.reissueAccessToken(requestDto.getRefreshToken()));
    }
}
