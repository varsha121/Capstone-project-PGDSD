package org.upgrad.services;

import org.upgrad.models.UserAuthToken;

/**
 * This UserAuthTokenService interface contains the list of all services in the UserAuthToken Service Implementation Class.
 */
public interface UserAuthTokenService {

    void addAccessToken(Integer userId, String accessToken);

    void removeAccessToken(String accessToken);

    UserAuthToken isUserLoggedIn(String accessToken);

    int getUserId(String accessToken);
}
