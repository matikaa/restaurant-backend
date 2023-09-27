package com.restaurant.user.repository;

import com.restaurant.common.ConstantValues;
import com.restaurant.user.controller.dto.UpdateUserRequest;
import com.restaurant.user.repository.dto.UserModel;

import java.util.List;
import java.util.Optional;

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
    public Optional<UserModel> findByEmail(String email) {
        return userJpaRepository.findByEmail(email)
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

        return ConstantValues.EMPTY_STRING;
    }

    @Override
    public void changePassword(String email, String newPassword) {
        userJpaRepository.findByEmail(email)
                .map(userRepositoryMapper::userEntityToUserModel)
                .map(user -> user.userWithNewPassword(newPassword))
                .map(userRepositoryMapper::userModelToUserEntity)
                .map(userJpaRepository::save);
    }

    @Override
    public Optional<UserModel> changeUser(String email, UpdateUserRequest updateUserRequest) {
        return userJpaRepository.findByEmail(email)
                .map(userRepositoryMapper::userEntityToUserModel)
                .map(user -> user.modifiedUser(updateUserRequest))
                .map(userRepositoryMapper::userModelToUserEntity)
                .map(userJpaRepository::save)
                .map(userRepositoryMapper::userEntityToUserModel);
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
    public void payForOrder(Long userId, Double cartValue) {
        userJpaRepository.findById(userId)
                .map(user -> deleteFromBalance(cartValue, user))
                .map(userJpaRepository::save);
    }

    @Override
    public void updateAccountBalance(Long userId, Double money) {
        userJpaRepository.findById(userId)
                .map(user -> updateBalance(money, user))
                .map(userJpaRepository::save);
    }

    @Override
    public boolean existsByUserId(Long userId) {
        return userJpaRepository.existsById(userId);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }

    private UserEntity deleteFromBalance(Double cartValue, UserEntity userEntity) {
        var money = userEntity.getMoney();
        userEntity.setMoney(ConstantValues.my_format(money - cartValue));
        return userEntity;
    }

    private UserEntity updateBalance(Double money, UserEntity userEntity) {
        var balance = userEntity.getMoney();
        userEntity.setMoney(ConstantValues.my_format(balance + money));

        return userEntity;
    }
}
