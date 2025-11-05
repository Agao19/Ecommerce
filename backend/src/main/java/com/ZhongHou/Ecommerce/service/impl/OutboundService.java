package com.ZhongHou.Ecommerce.service.impl;


import com.ZhongHou.Ecommerce.dto.UserDto;
import com.ZhongHou.Ecommerce.dto.request.ExchangeTokenRequest;
import com.ZhongHou.Ecommerce.dto.response.Response;
import com.ZhongHou.Ecommerce.entity.User;
import com.ZhongHou.Ecommerce.enums.UserRole;
import com.ZhongHou.Ecommerce.exception.AppException;
import com.ZhongHou.Ecommerce.exception.ErrorCode;
import com.ZhongHou.Ecommerce.repository.UserRepository;
import com.ZhongHou.Ecommerce.repository.httpClient.OutBoundIdentityClient;
import com.ZhongHou.Ecommerce.repository.httpClient.OutboundUserClient;
import com.ZhongHou.Ecommerce.security.JwtUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OutboundService {

    UserRepository userRepository;
    OutBoundIdentityClient outboundIdentityClient;
    OutboundUserClient outboundUserClient;
    JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;


    @NonFinal
    //@Value("${outbound.identity.client-id}")
    protected String CLIENT_ID = "50781803266-3l5qghg0rdsa5l93pe009s3homaihepc.apps.googleusercontent.com";

    @NonFinal
    //@Value("${outbound.identity.client-secret}")
    protected String CLIENT_SECRET = "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";

    @NonFinal
    //@Value("${outbound.identity.redirect-uri}")
    protected String REDIRECT_URI ="http://localhost:4200/authenticate";

    @NonFinal
    protected final String GRANT_TYPE = "authorization_code";

    public Response outboundAuthenticate(String code){

        var response = outboundIdentityClient.exchangeToken(ExchangeTokenRequest.builder()
                .code(code)
                .clientId(CLIENT_ID)
                .clientSecret(CLIENT_SECRET)
                .redirectUri(REDIRECT_URI)
                .grantType(GRANT_TYPE)
                .build());


        log.info("TOKEN RESPONSE {}", response);

        var userInfo = outboundUserClient.getUserInfoFromOutBound("json", response.getAccessToken());
        log.info("USER INFO {}", userInfo);

        var user = userRepository.findByEmail(userInfo.getEmail()).orElseGet(
                ()-> userRepository.save(User.builder()
                        .email(userInfo.getEmail())
                        .name(userInfo.getName())
                        .role(UserRole.USER)
                        .build())
        );

        String tokenInSystem = jwtUtils.generateToken(user);

        log.info("JWT TOKEN {}", tokenInSystem);

        return Response.builder()
                .token(tokenInSystem)
                .build();
    }

    public Response createPassword(UserDto createPasswordRequest) {
        var context = SecurityContextHolder.getContext();

        String  username = context.getAuthentication().getName();

        User user = userRepository.findByEmail(username)
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));


        if (StringUtils.hasText(user.getPassword())) {
            throw new AppException(ErrorCode.PASSWORD_EXISTED);
        }

        user.setPassword(passwordEncoder.encode(createPasswordRequest.getPassword()));
        userRepository.save(user);

        return Response.builder()
                .status(200)
                .message("Password created!")
                .build();

    }
}
