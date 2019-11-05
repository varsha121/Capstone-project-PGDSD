package org.upgrad.repositories;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.upgrad.models.User;

// author:Mohan;This repository interface is responsible for the interaction between the user service with the user database

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    @Query(nativeQuery = true, value = "SELECT PASSWORD FROM USERS WHERE contact_number=?1")
    String findUserPassword(String contactNumber);

    @Query(nativeQuery = true, value = "SELECT * FROM USERS WHERE contact_number=?1")
    User findUser(String contactNumber);

    @Query(nativeQuery = true, value = "SELECT * FROM USERS WHERE id=?1")
    User getUserById(int id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO USERS(firstname,lastname,email,contact_number,password) VALUES(?1,?2,?3,?4,?5)")
    void newUser(String firstName, String lastName, String email, String contactNumber, String password);

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(nativeQuery = true, value = "UPDATE USERS SET firstname=?1, lastname=?2 WHERE id=?3")
    int updateUser(String firstName, String lastName, int id);

    @Query(nativeQuery = true, value = "SELECT PASSWORD FROM USERS WHERE id=?1")
    String findUserPwdById(int id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE USERS SET password=?1 WHERE id=?2")
    void updatePwd(String password, int id);
}
