package com.restaurant.user.service;

import com.restaurant.user.controller.dto.*;
import com.restaurant.common.ConstantValues;
import com.restaurant.jwt.service.JwtService;
import com.restaurant.user.controller.dto.*;
import com.restaurant.user.repository.UserRepository;
import com.restaurant.user.service.dto.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseUserService implements UserService {

    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private static final UserServiceMapper userServiceMapper = UserServiceMapper.INSTANCE;

    public BaseUserService(UserRepository userRepository,
                           JwtService jwtService,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public List<User> getAll() {
        return userServiceMapper.userModelsToUsers(userRepository.findAll());
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId)
                .map(userServiceMapper::userModelToUser);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userServiceMapper::userModelToUser);
    }

    @Override
    public User insert(UserRequest userRequest) {
        var fixedUserRequest = new UserRequest(
                userRequest.email().toLowerCase(), userRequest.name(),
                userRequest.password(), userRequest.address(), userRequest.phoneNumber());

        var userModelToSave = userServiceMapper.userRequestToUserModel(fixedUserRequest)
                .newUser(passwordEncoder.encode(fixedUserRequest.password()));

        return userServiceMapper.userModelToUser(
                userRepository.save(userModelToSave));
    }

    @Override
    public boolean verifyAndLogUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password())
        );

        return authentication.isAuthenticated();
    }

    @Override
    public Optional<User> changeUserDetails(String email, UpdateUserRequest updateUserRequest) {
        return userRepository.changeUser(email, updateUserRequest)
                .map(userServiceMapper::userModelToUser);
    }

    @Override
    public void deleteByEmail(String email) {
        userRepository.deleteByUserEmail(email);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public String generateToken(String email) {
        return jwtService.generateToken(email);
    }

    @Override
    public void logout(String token) {
        var tokenWithoutPrefix = token.substring(ConstantValues.TOKEN_PREFIX_LENGTH);
        jwtService.deleteToken(tokenWithoutPrefix);
    }

    @Override
    public boolean changePassword(String email, ChangePasswordRequest changePasswordRequest) {
        if (userRepository.existsByEmail(email) && validPasswords(email, changePasswordRequest)) {
            userRepository.changePassword(email, passwordEncoder.encode(changePasswordRequest.newPassword()));
            return true;
        }

        return false;
    }

    @Override
    public boolean changeUserPassword(Long userId, UserChangePasswordRequest userChangePasswordRequest) {
        if (existsByUserId(userId)) {
            var foundUser = userRepository.findById(userId);
            if (foundUser.isPresent()) {
                userRepository.changePassword(
                        foundUser.get().email(),
                        passwordEncoder.encode(userChangePasswordRequest.newPassword()));
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean verifyPassword(String email, String currentPassword) {
        var password = userRepository.getCurrentPassword(email);
        if (!password.isEmpty() && passwordEncoder.matches(currentPassword, password)) {
            return true;
        }

        return false;
    }

    @Override
    public boolean existsUserByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    private boolean validPasswords(String email, ChangePasswordRequest changePasswordRequest) {
        var password = userRepository.getCurrentPassword(email);

        if (verifyPassword(email, changePasswordRequest.currentPassword()) &&
                !passwordEncoder.matches(changePasswordRequest.newPassword(), password)) {
            return true;
        }

        return false;
    }

    @Override
    public boolean existsByUserId(Long userId) {
        return userRepository.existsByUserId(userId);
    }

    @Override
    public void completeOrder(Long userId, Double cartValue) {
        userRepository.payForOrder(userId, cartValue);
    }

    @Override
    public void updateUserBalance(Long userId, UserMoney userMoney) {
        userRepository.updateAccountBalance(userId, userMoney.money());
    }

    @Override
    public boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
