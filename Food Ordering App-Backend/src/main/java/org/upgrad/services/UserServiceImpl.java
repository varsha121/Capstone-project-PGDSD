package org.upgrad.services;

import org.springframework.stereotype.Service;
import org.upgrad.models.User;
import org.upgrad.repositories.UserRepository;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String findUserPassword(String contactNumber) {
        return userRepository.findUserPassword(contactNumber);
    }

    @Override
    public User findUser(String contactNumber) {
        return userRepository.findUser(contactNumber);
    }

    @Override
    public Boolean newUser(String firstName, String lastName, String email, String contactNumber, String password) {
        Boolean success = false;
        userRepository.newUser(firstName, lastName, email, contactNumber, password);
        if (userRepository.findUser(contactNumber) != null) {
            success = true;
        }
        return success;
    }

    @Override
    public User updateUser(String firstName, String lastName, int id) {
        int updateUser = userRepository.updateUser(firstName, lastName, id);
        User user = userRepository.getUserById(id);
        return user;
    }

    @Override
    public User getUserById(int id) {
        return userRepository.getUserById(id);
    }

    @Override
    public String findUserPwdById(int id) {
        return userRepository.findUserPwdById(id);
    }

    @Override
    public void updatePwd(String newPassword, int id) {
        userRepository.updatePwd(newPassword, id);
    }
}
