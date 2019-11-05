package org.upgrad.services;

import org.upgrad.models.Address;
import org.upgrad.models.States;

import java.util.List;

/**
 * This AddressService interface gives the list of all the services in the address service implementation class.
 */
public interface AddressService {

    List<States> getAllStates();

    void addAddress(String flatBuilNumber, String locality, String city, String zipcode, Integer stateId);

    void userAddressMapping(String type, Integer userId, Integer addressId);

    int findIdForLatestAddress();

    Boolean getAddress(int addressId);

    void updatePermAddress(String flatBuilNumber, String locality, String city, String zipcode, Integer stateId, Integer addressId);

    List<Address> getPermAddress(int userId);

    void updatePermAddressWithoutState(String flatBuilNumber, String locality, String city, String zipcode, Integer addressId);

    Boolean deletePermAddressById(int addressId);

}
