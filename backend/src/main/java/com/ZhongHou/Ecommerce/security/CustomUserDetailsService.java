package com.ZhongHou.Ecommerce.security;
import com.ZhongHou.Ecommerce.entity.User;
import com.ZhongHou.Ecommerce.exception.AppException;
import com.ZhongHou.Ecommerce.exception.ErrorCode;
import com.ZhongHou.Ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepo.findByEmail(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return AuthUser.builder()
                .user(user)
                .build();
    }
}
