package com.ZhongHou.Ecommerce.service.impl;

import com.ZhongHou.Ecommerce.dto.LoginRequest;
import com.ZhongHou.Ecommerce.dto.response.Response;
import com.ZhongHou.Ecommerce.dto.UserDto;
import com.ZhongHou.Ecommerce.entity.User;
import com.ZhongHou.Ecommerce.enums.UserRole;
import com.ZhongHou.Ecommerce.exception.AppException;
import com.ZhongHou.Ecommerce.exception.ErrorCode;
import com.ZhongHou.Ecommerce.mapper.EntityDtoMapper;
import com.ZhongHou.Ecommerce.repository.UserRepository;
import com.ZhongHou.Ecommerce.security.JwtUtils;
import com.ZhongHou.Ecommerce.security.RedisToken;
import com.ZhongHou.Ecommerce.service.RedisRepository;
import com.ZhongHou.Ecommerce.service.UserService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final EntityDtoMapper entityDtoMapper;
    private final RedisRepository redisRepository;

    private static final boolean ROTATE_REFRESH = true;

    @Override
    public Response registerUser(UserDto registrationRequest) {
        UserRole role = UserRole.USER;

        if (registrationRequest.getRole() != null && registrationRequest.getRole().equalsIgnoreCase("admin")) {
            role = UserRole.ADMIN;
        }

        User user = User.builder()
                .name(registrationRequest.getName())
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .phoneNumber(registrationRequest.getPhoneNumber())
                .role(role)
                .build();

        User savedUser = userRepo.save(user);
        System.out.println(savedUser);

        UserDto userDto = entityDtoMapper.mapUserToDtoBasic(savedUser);
        return Response.builder()
                .status(200)
                .message("User Successfully Added")
                .user(userDto)
                .build();
    }



    @Override
    public Response loginUser(LoginRequest loginRequest) {

        User user = userRepo.findByEmail(loginRequest.getEmail())
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        String token = jwtUtils.generateToken(user);
        String refreshToken = jwtUtils.generateRefreshToken(user);

        String refreshJti = jwtUtils.getJtiFromRefreshToken(refreshToken);
        Date refreshExp = jwtUtils.getExpirationTimeFromFreshToken(refreshToken);
        long refreshTTL = refreshExp.getTime() - System.currentTimeMillis();


        //Saving refresh_jwtID vao whitelist
        RedisToken redisFreshToken  = RedisToken.builder()
                .jwtId("refresh:"+refreshJti)
                .expiredRedisTime(refreshTTL)
                .build();

        redisRepository.save(redisFreshToken);



        return Response.builder()
                .status(200)
                .message("User Successfully Logged In")
                .token(token)
                .refreshToken(refreshToken)
                .expirationTime(Math.toIntExact(TimeUnit.MILLISECONDS.toSeconds(refreshTTL)))
                .role(user.getRole().name())
                .build();
    }


    //NEW ACCESSTOKEN
    @Override
    public Response sendNewAccessToken(String refreshToken) {

        if(refreshToken == null || refreshToken.isBlank()){
            throw new AppException(ErrorCode.INVALID_KEY);
        }

        String type = jwtUtils.getType(refreshToken);
        if (!"refresh".equals(type)) throw new AppException(ErrorCode.TOKEN_INVALID);

        Date exp = jwtUtils.getExpirationTimeFromFreshToken(refreshToken);
        if (exp == null || exp.before(new Date())) throw new AppException(ErrorCode.TOKEN_EXPIRED);

        String jti = jwtUtils.getJtiFromRefreshToken(refreshToken);
        String key = "refresh:" + jti;
        boolean whitelisted = redisRepository.findById(key).isPresent();

        if (!whitelisted) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        String subject  = jwtUtils.getSubjectFromRefreshToken(refreshToken);
        User user = userRepo.findByEmail(subject)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));


        //delete refresh_jwtID khi phat lai access token
        redisRepository.deleteById(key);
        String newRefreshToken = null;
        int newRefreshTtlSeconds = 0;


        if (ROTATE_REFRESH) {
            newRefreshToken = jwtUtils.generateRefreshToken(user);
            String newJti = jwtUtils.getJtiFromRefreshToken(newRefreshToken);
            Date newExp = jwtUtils.getExpirationTimeFromFreshToken(newRefreshToken);
            long newRefreshTTL = newExp.getTime() - System.currentTimeMillis();
            RedisToken newRedis = RedisToken.builder()
                    .jwtId("refresh:" + newJti)
                    .expiredRedisTime(newRefreshTTL)
                    .build();
            redisRepository.save(newRedis);
            newRefreshTtlSeconds = Math.toIntExact(TimeUnit.MILLISECONDS.toSeconds(newRefreshTTL));
        }

        //create newAccesstoken
        String newAccessToken = jwtUtils.generateToken(user);


        return Response.builder()
                .status(200)
                .message("Successfully with new access token")
                .token(newAccessToken)
                .refreshToken(newRefreshToken)
                .expirationTime(newRefreshTtlSeconds)
                .build();
    }


    @Override
    public void logout(String token){
        if (token == null) return;

        String jwtId=  jwtUtils.extractClaims(token,Claims::getId);
        Date issuedTime = jwtUtils.extractClaims(token,Claims::getIssuedAt);
        Date expirationTime = jwtUtils.extractClaims(token,Claims::getExpiration);
        long remainingMs = expirationTime.getTime() - System.currentTimeMillis();
        String type =  jwtUtils.getType(token);

        if (remainingMs <= 0) {
            // token đã hết hạn -> không cần lưu
            log.info("Token already expired, nothing to revoke: jti={}", jwtId);
            return;
        }

        if (expirationTime.before(new Date())){ // TTL end => kh xu ly token
            return;
        }

        if ("refresh".equals(type)){
            String key ="refresh:" + jwtId;
            try {
                redisRepository.deleteById(key);
            }catch (Exception e){
                throw new AppException(ErrorCode.TOKEN_INVALID) ;
            }
            return;
        } else {
            String key ="blacklistAc:access" + jwtId;
            //Luu token da logout vao redis
            RedisToken redisToken = RedisToken.builder()
                    .jwtId(key)
                    .expiredRedisTime(remainingMs)
                    .build();
            redisRepository.save(redisToken);
        }

        log.info("logout successfully");

    }

    @Override
    public Response getAllUsers() {

        List<User> users = userRepo.findAll();
        List<UserDto> userDtos = users.stream()
                .map(entityDtoMapper::mapUserToDtoBasic)
                .toList();

        return Response.builder()
                .status(200)
                .userList(userDtos)
                .build();
    }

    @Override
    public User getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String  email = authentication.getName();
        return userRepo.findByEmail(email)
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    @Override
    public Response getUserInfoAndOrderHistory() {
        User user = getLoginUser();

        UserDto userDto = entityDtoMapper.mapUserToDtoPlusAddressAndOrderHistory(user);

        userDto.setNoPassword(!StringUtils.hasText(user.getPassword()));

        return Response.builder()
                .status(200)
                .user(userDto)
                .build();
    }
}
