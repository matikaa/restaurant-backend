package com.restaurant.app.user.service;

import com.restaurant.app.user.repository.UserJpaRepository;
import com.restaurant.app.user.service.dto.UserInfoDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static com.restaurant.app.response.ConstantValues.USER_NOT_EXISTS;

public class UserInfoService implements UserDetailsService {

    private final UserJpaRepository userJpaRepository;

    public UserInfoService(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        return userJpaRepository.findByEmail(email)
                .map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_EXISTS));
    }
}
