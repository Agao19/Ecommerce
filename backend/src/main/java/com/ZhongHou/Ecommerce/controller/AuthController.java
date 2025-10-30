package com.ZhongHou.Ecommerce.controller;

import com.ZhongHou.Ecommerce.dto.LoginRequest;
import com.ZhongHou.Ecommerce.dto.response.Response;
import com.ZhongHou.Ecommerce.dto.UserDto;
import com.ZhongHou.Ecommerce.service.UserService;
import com.ZhongHou.Ecommerce.service.impl.OutboundService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;
    private final OutboundService outboundService;

    @PostMapping("/register")
    public ResponseEntity<Response> registerUser(@RequestBody UserDto registrationRequest){
        return ResponseEntity.ok(userService.registerUser(registrationRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<Response> loginUser(@RequestBody @Valid LoginRequest loginRequest , HttpServletResponse response){

        Response result  = userService.loginUser(loginRequest);

        // set refresh token vào HttpOnly cookie (Secure=true)
        ResponseCookie cookie = ResponseCookie.from("rt", result.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/auth/refresh")
                .maxAge(result.getExpirationTime())
                .sameSite("Strict") // hoặc "Lax"/"None" nếu cần cross-site with credentials
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message("User Successfully Logged In")
                .token(result.getToken())
                       // .refreshToken(result.getRefreshToken())
                .role(result.getRole())
                .build());
    }

    @PostMapping("/refresh")
    ResponseEntity<Response> newAccessToken(@CookieValue(name="rt", required = false) String refreshToken, HttpServletResponse response){
        //Check cookie exist?
        if (refreshToken == null || refreshToken.isBlank())
            return ResponseEntity.status(401).body(
                    Response.builder().
                            status(401).message("Refresh token is missing.").
                            build());


        Response result = userService.sendNewAccessToken(refreshToken);
        //Nếu có xoay vòng (newRefreshToken được trả về), set lại cookie MỚI
        if (result.getRefreshToken() != null) {
            ResponseCookie newCookie = ResponseCookie.from("rt", result.getRefreshToken())
                    .httpOnly(true)
                    .secure(true)
                    .path("/auth/refresh")
                    .maxAge(result.getExpirationTime())
                    .sameSite("Strict")
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, newCookie.toString());
        }
        log.info("role" + result.getRole());
        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message("Successfully with new access token")
                .token(result.getToken()) // AccessToken MỚI
                .build());

    }

    @PostMapping("/logout")
    void logout(@RequestHeader("Authorization") String authHeader){
        String token = authHeader.replace("Bearer ", "");
        log.info("token {}", token);
        userService.logout(token);

    }

    //Fe -> BE
    @PostMapping("/outbound/authentication")
    public Response outboundAuthenticate(@RequestParam("code") String code){
        var res = outboundService.outboundAuthenticate(code);
        log.info("Outbound token from google  {}", res);
        return res;
    }


    //Create password for onboard user (from Google)
    @PostMapping ("/outbound/create-password")
    ResponseEntity<Response> createPassword(@RequestBody UserDto createPasswordRequest){
        return ResponseEntity.ok(outboundService.createPassword(createPasswordRequest));
    }

}