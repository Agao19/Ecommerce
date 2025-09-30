package com.ZhongHou.Ecommerce.service.impl;

import com.ZhongHou.Ecommerce.dto.LoginRequest;
import com.ZhongHou.Ecommerce.dto.Response;
import com.ZhongHou.Ecommerce.dto.UserDto;
import com.ZhongHou.Ecommerce.entity.User;
import com.ZhongHou.Ecommerce.enums.UserRole;
import com.ZhongHou.Ecommerce.exception.InvalidCredentialsException;
import com.ZhongHou.Ecommerce.exception.NotFoundException;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final EntityDtoMapper entityDtoMapper;
    private final RedisRepository redisRepository;


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

        User user = userRepo.findByEmail(loginRequest.getEmail()).orElseThrow(()-> new NotFoundException("Email not found"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            throw new InvalidCredentialsException("Password does not match");
        }
        String token = jwtUtils.generateToken(user);

        return Response.builder()
                .status(200)
                .message("User Successfully Logged In")
                .token(token)
                .expirationTime("6 Month")
                .role(user.getRole().name())
                .build();
    }


    @Override
    public void logout(String token){
        if (token == null) return;

        String jwtId=  jwtUtils.extractClaims(token,Claims::getId);
        Date issuedTime = jwtUtils.extractClaims(token,Claims::getIssuedAt);
        Date expirationTime = jwtUtils.extractClaims(token,Claims::getExpiration);

        if (expirationTime.before(new Date())){ // TTL end => kh xu ly token
            return;
        }

       //Luu token vao redis
        RedisToken redisToken  = RedisToken.builder()
                .jwtId(jwtId)
                .expiredRedisTime(expirationTime.getTime() - issuedTime.getTime())
                .build();

        redisRepository.save(redisToken);
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
        log.info("User Email is: " + email);
        return userRepo.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User Not found"));
    }

    @Override
    public Response getUserInfoAndOrderHistory() {
        User user = getLoginUser();
        UserDto userDto = entityDtoMapper.mapUserToDtoPlusAddressAndOrderHistory(user);

        return Response.builder()
                .status(200)
                .user(userDto)
                .build();
    }
}
