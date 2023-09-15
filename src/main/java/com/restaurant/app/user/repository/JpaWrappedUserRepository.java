package com.restaurant.app.user.repository;

import com.restaurant.app.user.controller.dto.UpdateUserRequest;
import com.restaurant.app.user.repository.dto.UserModel;

import java.util.List;
import java.util.Optional;

import static com.restaurant.app.response.ConstantValues.EMPTY_STRING;

public class JpaWrappedUserRepository implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    private static final UserRepositoryMapper userRepositoryMapper = UserRepositoryMapper.INSTANCE;

    public JpaWrappedUserRepository(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public List<UserModel> findAll() {
        return userRepositoryMapper.userEntitiesToUserModels(userJpaRepository.findAll());
    }

    @Override
    public Optional<UserModel> findById(Long userId) {
        return userJpaRepository.findById(userId)
                .map(userRepositoryMapper::userEntityToUserModel);
    }

    @Override
    public UserModel save(UserModel userModel) {
        return userRepositoryMapper.userEntityToUserModel(
                userJpaRepository.save(userRepositoryMapper.userModelToUserEntity(userModel))
        );
    }

    @Override
    public String getCurrentPassword(String email) {
        var password = userJpaRepository.findByEmail(email)
                .map(userRepositoryMapper::userEntityToUserModel)
                .map(userRepositoryMapper::userModelToUserPassword);

        if (password.isPresent()) {
            return password.get().password();
        }

        return EMPTY_STRING;
    }

    @Override
    public void changePassword(String email, String newPassword) {
        if (existsByEmail(email)) {
            userJpaRepository.findByEmail(email)
                    .map(userRepositoryMapper::userEntityToUserModel)
                    .map(user -> user.userWithNewPassword(newPassword))
                    .map(userRepositoryMapper::userModelToUserEntity)
                    .map(userJpaRepository::save);
        }
    }

    @Override
    public Optional<UserModel> changeUser(String email, UpdateUserRequest updateUserRequest) {
        if (existsByEmail(email)) {
            return userJpaRepository.findByEmail(email)
                    .map(userRepositoryMapper::userEntityToUserModel)
                    .map(user -> user.modifiedUser(updateUserRequest))
                    .map(userRepositoryMapper::userModelToUserEntity)
                    .map(userJpaRepository::save)
                    .map(userRepositoryMapper::userEntityToUserModel);
        }

        return Optional.empty();
    }

    @Override
    public void deleteByUserEmail(String email) {
        userJpaRepository.findByEmail(email)
                .ifPresent(userJpaRepository::delete);
    }

    @Override
    public void deleteById(Long userId) {
        userJpaRepository.deleteById(userId);
    }

    @Override
    public boolean existsByUserId(Long userId) {
        return userJpaRepository.existsById(userId);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }
}
