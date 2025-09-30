package com.ZhongHou.Ecommerce.controller;

import com.ZhongHou.Ecommerce.dto.LoginRequest;
import com.ZhongHou.Ecommerce.dto.Response;
import com.ZhongHou.Ecommerce.dto.UserDto;
import com.ZhongHou.Ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ResponseEntity<Response> loginUser(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(userService.loginUser(loginRequest));
    }

    @PostMapping("/logout")
    void logout(@RequestHeader("Authorization") String authHeader){
        String token = authHeader.replace("Bearer ", "");
        log.info("token {}", token);
        userService.logout(token);

    }
}
