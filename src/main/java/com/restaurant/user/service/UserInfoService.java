package com.restaurant.user.service;

import com.restaurant.user.repository.UserJpaRepository;
import com.restaurant.user.service.dto.UserInfoDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public class UserInfoService implements UserDetailsService {

    private final UserJpaRepository userJpaRepository;

    public UserInfoService(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        return userJpaRepository.findByEmail(email)
                .map(UserInfoDetails::new)
                .orElse(null);
    }
}
