package org.upgrad.controllers;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.upgrad.models.User;
import org.upgrad.services.UserAuthTokenService;
import org.upgrad.services.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserAuthTokenService userAuthTokenService;

    /*
     * Using Regex and pattern matcher to check email validity
     */
    private static boolean isEmailValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    //to check whether contact number is valid
    private static boolean isContactNumberValid(String contactNumber) {
        return contactNumber.matches("\\d{10}");
    }

    /*
     *  To check password strength
     */
    private static boolean isPasswordStrong(String password) {
        String passwordRegex = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[#@$%&*!^]).{8,}$";
        Pattern pat = Pattern.compile(passwordRegex);
        if (password == null)
            return false;

        return pat.matcher(password).matches();
    }

    /*
     * To handle new user signup.
     *
     */
    @PostMapping("/signup")
    @CrossOrigin
    public ResponseEntity<?> signup(@RequestParam String firstName, String lastName, @RequestParam String email, @RequestParam String contactNumber, @RequestParam String password) {
        if (userService.findUser(contactNumber) != null) {
            return new ResponseEntity<>("Try any other contact number, this contact number has already been registered!", HttpStatus.BAD_REQUEST);
        } else if (!isEmailValid(email)) {
            return new ResponseEntity<>("Invalid email-id format!", HttpStatus.BAD_REQUEST);
        } else if (!isContactNumberValid(contactNumber)) {
            return new ResponseEntity<>("Invalid contact number!", HttpStatus.BAD_REQUEST);
        } else if (!isPasswordStrong(password)) {
            return new ResponseEntity<>("Weak password!", HttpStatus.BAD_REQUEST);
        } else {
            String sha256hex = Hashing.sha256()
                    .hashString(password, Charsets.US_ASCII)
                    .toString();
            userService.newUser(firstName, lastName, email, contactNumber, sha256hex);
            return new ResponseEntity<>("User with contact number " + contactNumber + " successfully registered!", HttpStatus.CREATED);
        }
    }

    /*
     * This endpoint is used to login a user.
     */
    @PostMapping("/login")
    @CrossOrigin
    public ResponseEntity<?> login(@RequestParam String contactNumber, @RequestParam String password) {
        String passwordByUser = String.valueOf(userService.findUserPassword(contactNumber));
        String sha256hex = Hashing.sha256()
                .hashString(password, Charsets.US_ASCII)
                .toString();
        if (userService.findUserPassword(contactNumber) == null)
            return new ResponseEntity<>("This contact number has not been registered!", HttpStatus.OK);
        else if (!(passwordByUser.equalsIgnoreCase(sha256hex))) {
            return new ResponseEntity<>("Invalid Credentials", HttpStatus.UNAUTHORIZED);
        } else {
            User user = userService.findUser(contactNumber);
            String accessToken = UUID.randomUUID().toString();
            userAuthTokenService.addAccessToken(user.getId(), accessToken);
            HttpHeaders headers = new HttpHeaders();
            headers.add("access-token", accessToken);
            List<String> header = new ArrayList<>();
            header.add("access-token");
            headers.setAccessControlExposeHeaders(header);
            return new ResponseEntity<>(user, headers, HttpStatus.OK);
        }
    }

    /*
     * This endpoint is used to logout a user.
     */
    @PutMapping("/logout")
    @CrossOrigin
    public ResponseEntity<String> logout(@RequestHeader String accessToken) {
        if (userAuthTokenService.isUserLoggedIn(accessToken) == null) {
            return new ResponseEntity<>("Please Login first to access this endpoint!", HttpStatus.UNAUTHORIZED);
        } else if (userAuthTokenService.isUserLoggedIn(accessToken).getLogoutAt() != null) {
            return new ResponseEntity<>("Invalid Coupon!", HttpStatus.UNAUTHORIZED);
        } else {
            userAuthTokenService.removeAccessToken(accessToken);
            return new ResponseEntity<>("You have logged out successfully!", HttpStatus.OK);
        }
    }

    /*
     * This endpoint will update thefirst name and last name of user
     */
    @PutMapping("")
    @CrossOrigin
    public ResponseEntity<?> updateUserName(@RequestParam String firstName, String lastName, @RequestHeader String accessToken) {
        if (userAuthTokenService.isUserLoggedIn(accessToken) == null) {
            return new ResponseEntity<>("Please Login first to access this endpoint!", HttpStatus.UNAUTHORIZED);
        } else if (userAuthTokenService.isUserLoggedIn(accessToken).getLogoutAt() != null) {
            return new ResponseEntity<>("You have already logged out. Please Login first to access this endpoint!", HttpStatus.UNAUTHORIZED);
        } else {
            int userId = userAuthTokenService.getUserId(accessToken);
            User user = userService.updateUser(firstName, lastName, userId);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
    }

    /*
     * This endpoint will update user password
     */
    @PutMapping("/password")
    @CrossOrigin
    public ResponseEntity<?> updatePassword(@RequestParam String oldPassword, @RequestParam String newPassword, @RequestHeader String accessToken) {
        //Converting oldPassword and newPassword into Sha 256
        String oldPwdSha = Hashing.sha256()
                .hashString(oldPassword, Charsets.US_ASCII)
                .toString();
        String newPwdSha = Hashing.sha256()
                .hashString(newPassword, Charsets.US_ASCII)
                .toString();
        if (userAuthTokenService.isUserLoggedIn(accessToken) == null) {
            return new ResponseEntity<>("Please Login first to access this endpoint!", HttpStatus.UNAUTHORIZED);
        } else if (userAuthTokenService.isUserLoggedIn(accessToken).getLogoutAt() != null) {
            return new ResponseEntity<>("You have already logged out. Please Login first to access this endpoint!", HttpStatus.UNAUTHORIZED);
        } else {
            int userId = userAuthTokenService.getUserId(accessToken);
            if (!userService.getUserById(userId).getPassword().equalsIgnoreCase(oldPwdSha)) {
                return new ResponseEntity<>("Your password did not match to your old password!", HttpStatus.BAD_REQUEST);
            } else if (!isPasswordStrong(newPassword)) { 
                return new ResponseEntity<>("Weak password!", HttpStatus.BAD_REQUEST);
            } else {  
                userService.updatePwd(newPwdSha, userId);
                return new ResponseEntity<>("Password updated successfully!", HttpStatus.OK);
            }
        }
    }
}
