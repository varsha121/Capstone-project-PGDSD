package org.upgrad.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.upgrad.models.Address;
import org.upgrad.services.AddressService;
import org.upgrad.services.UserAuthTokenService;

import java.util.List;

@RestController
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private UserAuthTokenService userAuthTokenService;

  
    public static boolean isZipcodeValid(String zipcode) {
        if (zipcode.matches("\\d{6}")) {
            return true;
        } else {
            return false;
        }
    }

    /*
     * This is the endpoint to get all states.
     */
    @GetMapping("/states")
    @CrossOrigin
    public ResponseEntity<?> allStates() {
        return new ResponseEntity<>(addressService.getAllStates(), HttpStatus.OK);
    }

    /*
     * This endpoint is to add new address.
     */
    @PostMapping("/address")
    @CrossOrigin
    public ResponseEntity<?> addAddress(@RequestParam String flatBuilNo, @RequestParam String locality, @RequestParam String city, @RequestParam String zipcode, String type, @RequestParam Integer stateId, @RequestHeader String accessToken) {
        if (userAuthTokenService.isUserLoggedIn(accessToken) == null) {
            return new ResponseEntity<>("Please Login first to access this endpoint!", HttpStatus.UNAUTHORIZED);
        } else if (userAuthTokenService.isUserLoggedIn(accessToken).getLogoutAt() != null) {
            return new ResponseEntity<>("You have already logged out. Please Login first to access this endpoint!", HttpStatus.UNAUTHORIZED);
        } else {
  
            if (type == null || !type.equals("perm")) {
                type = "temp";
            } else {
                type = "perm";
            }
            // Checking if zip code is valid.
            if (!isZipcodeValid(zipcode)) {
                return new ResponseEntity<>("Invalid zipcode!", HttpStatus.BAD_REQUEST);
            } else {
                addressService.addAddress(flatBuilNo, locality, city, zipcode, stateId);
               
                int latestAddressId = addressService.findIdForLatestAddress();
           
                int userId = userAuthTokenService.getUserId(accessToken);
               
                addressService.userAddressMapping(type, userId, latestAddressId);
                return new ResponseEntity<>("Address has been saved successfully!", HttpStatus.CREATED);
            }
        }
    }

    /*
     * This endpoint is to update a permanent address.
     */
    @PutMapping("/address/{addressId}")
    @CrossOrigin
    public ResponseEntity<?> updatePermAddress(@PathVariable("addressId") int addressId, String flatBuilNo, String locality, String city, String zipcode, Integer stateId, @RequestHeader String accessToken) {
        if (userAuthTokenService.isUserLoggedIn(accessToken) == null) {
            return new ResponseEntity<>("Please Login first to access this endpoint!", HttpStatus.UNAUTHORIZED);
        } else if (userAuthTokenService.isUserLoggedIn(accessToken).getLogoutAt() != null) {
            return new ResponseEntity<>("You have already logged out. Please Login first to access this endpoint!", HttpStatus.UNAUTHORIZED);
        } else { 
            if (zipcode != null && !isZipcodeValid(zipcode)) { 
                return new ResponseEntity<>("Invalid zipcode!", HttpStatus.BAD_REQUEST);
            } else if (!addressService.getAddress(addressId)) { 
                return new ResponseEntity<>("No address with this address id!", HttpStatus.BAD_REQUEST);
            } else if (stateId == null) {
                addressService.updatePermAddressWithoutState(flatBuilNo, locality, city, zipcode, addressId);
            } else { 
                addressService.updatePermAddress(flatBuilNo, locality, city, zipcode, stateId, addressId);
            }
        }
        return new ResponseEntity<>("Address has been updated successfully!", HttpStatus.OK);
    }

    /*
     * This endpoint is to get all permanent address.
     * Requires authentication.
     */
    @GetMapping("/address/user")
    @CrossOrigin
    public ResponseEntity<?> getAllPermAddressByUser(@RequestHeader String accessToken) {
        if (userAuthTokenService.isUserLoggedIn(accessToken) == null) {
            return new ResponseEntity<>("Please Login first to access this endpoint!", HttpStatus.UNAUTHORIZED);
        } else if (userAuthTokenService.isUserLoggedIn(accessToken).getLogoutAt() != null) {
            return new ResponseEntity<>("You have already logged out. Please Login first to access this endpoint!", HttpStatus.UNAUTHORIZED);
        } else {
            int userId = userAuthTokenService.getUserId(accessToken);
            List<Address> allPermAddressIdByUser = addressService.getPermAddress(userId);
            if (allPermAddressIdByUser == null) {
                return new ResponseEntity<>("No permanent address found!", HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>(allPermAddressIdByUser, HttpStatus.OK);
            }
        }
    }

    /*
     * This endpoint is to delete a permanent address.
     */
    @DeleteMapping("/address/{addressId}")
    @CrossOrigin
    public ResponseEntity<?> deletePermAddress(@PathVariable("addressId") int addressId, @RequestHeader String accessToken) {
        if (userAuthTokenService.isUserLoggedIn(accessToken) == null) {
            return new ResponseEntity<>("Please Login first to access this endpoint!", HttpStatus.UNAUTHORIZED);
        } else if (userAuthTokenService.isUserLoggedIn(accessToken).getLogoutAt() != null) {
            return new ResponseEntity<>("You have already logged out. Please Login first to access this endpoint!", HttpStatus.UNAUTHORIZED);
        } else {
            if (!addressService.getAddress(addressId)) {
                return new ResponseEntity<>("No address with this address id!", HttpStatus.BAD_REQUEST);
            } else {
                addressService.deletePermAddressById(addressId);
                return new ResponseEntity<>("Address has been deleted successfully!", HttpStatus.OK);
            }
        }
    }
}
