package org.upgrad.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.upgrad.models.UserAuthToken;

//  author: mohan 

@Repository
public interface UserAuthTokenRepository extends CrudRepository<UserAuthToken, Integer> {

    @Query(nativeQuery = true, value = "SELECT * FROM USER_AUTH_TOKEN WHERE access_token=?1")
    UserAuthToken isUserLoggedIn(String accessToken);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE USER_AUTH_TOKEN SET logout_at=NOW() WHERE access_token=?1")
    void removeAuthToken(String accessToken);

    @Query(nativeQuery = true, value = "select user_id from user_auth_token where access_token=?1")
    int getUserId(String accessToken);
}
