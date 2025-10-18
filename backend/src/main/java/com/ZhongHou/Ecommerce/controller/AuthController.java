package com.ZhongHou.Ecommerce.controller;

import com.ZhongHou.Ecommerce.dto.LoginRequest;
import com.ZhongHou.Ecommerce.dto.Response;
import com.ZhongHou.Ecommerce.dto.UserDto;
import com.ZhongHou.Ecommerce.entity.User;
import com.ZhongHou.Ecommerce.security.AuthUser;
import com.ZhongHou.Ecommerce.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Response> registerUser(@RequestBody UserDto registrationRequest){
        return ResponseEntity.ok(userService.registerUser(registrationRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<Response> loginUser(@RequestBody LoginRequest loginRequest , HttpServletResponse response){

        Response result  = userService.loginUser(loginRequest);
        // set refresh token vào HttpOnly cookie (Secure=true)
        ResponseCookie cookie = ResponseCookie.from("rt", result.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/auth/refresh")
                .maxAge(result.getExpirationTime())
                .sameSite("Strict") // hoặc "Lax"/"None" nếu cần cross-site with credentials
                .build();
        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message("User Successfully Logged In")
                .token(result.getToken())
                        .refreshToken(result.getRefreshToken())
                .role(result.getRole())
                .build());
    }


    @PostMapping("/logout")
    void logout(@RequestHeader("Authorization") String authHeader){
        String token = authHeader.replace("Bearer ", "");
        log.info("token {}", token);
        userService.logout(token);

    }

    @PostMapping("/refresh")
    ResponseEntity<Response> newAccessToken(@RequestHeader("Authorization") String authHeader){
        String refreshToken = authHeader.replace("Bearer ", "");
        log.info("new accessToken {}", refreshToken);
        return ResponseEntity.ok(userService.sendNewAccessToken(refreshToken));

    }
}
