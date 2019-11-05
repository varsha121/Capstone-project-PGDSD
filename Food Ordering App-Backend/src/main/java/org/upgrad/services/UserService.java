package org.upgrad.services;

import org.upgrad.models.User;

/* Author: Mohan
 * This UserService interface has the list of all services in the User Service Implementation Class.
 */
public interface UserService {

    String findUserPassword(String contactNumber);

    User findUser(String contactNumber);

    Boolean newUser(String firstName, String lastName, String email, String contactNumber, String password);

    User updateUser(String firstName, String lastName, int id);

    User getUserById(int id);

    String findUserPwdById(int id);

    void updatePwd(String newPassword, int id);
}
