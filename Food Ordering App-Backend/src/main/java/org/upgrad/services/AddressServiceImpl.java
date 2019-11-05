package org.upgrad.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.upgrad.models.Address;
import org.upgrad.models.States;
import org.upgrad.repositories.AddressRepository;
import org.upgrad.repositories.StateRepository;

import java.util.List;

@Service
@Transactional
public class AddressServiceImpl implements AddressService {

    private final StateRepository stateRepository;
    private final AddressRepository addressRepository;

    public AddressServiceImpl(StateRepository stateRepository, AddressRepository addressRepository) {
        this.stateRepository = stateRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public List<States> getAllStates() {
        return stateRepository.getAllStates();
    }

    @Override
    public void addAddress(String flatBuilNumber, String locality, String city, String zipcode, Integer stateId) {
        addressRepository.addAddress(flatBuilNumber, locality, city, zipcode, stateId);
    }

    @Override
    public void userAddressMapping(String type, Integer userId, Integer addressId) {
        addressRepository.userAddressMapping(type, userId, addressId);
    }

   
    @Override
    public int findIdForLatestAddress() {
        return addressRepository.findIdForLatestAddress();
    }

    @Override
    public Boolean getAddress(int addressId) {
        Boolean success = false;
        if (addressRepository.getAddress(addressId) != null) {
            success = true;
        }
        return success;
    }

    @Override
    public void updatePermAddress(String flatBuilNumber, String locality, String city, String zipcode, Integer stateId, Integer addressId) {
        if (stateId == null) {
            addressRepository.updatePermAddress(flatBuilNumber, locality, city, zipcode, addressId);

        } else {
            addressRepository.updatePermAddressWithOutStateId(flatBuilNumber, locality, city, zipcode, stateId, addressId);

        }
    }

    @Override
    public List<Address> getPermAddress(int userId) {
        return addressRepository.getPermAddress(userId);
    }

    @Override
    public void updatePermAddressWithoutState(String flatBuilNumber, String locality, String city, String zipcode, Integer addressId) {
        addressRepository.updatePermAddressWithoutState(flatBuilNumber, locality, city, zipcode, addressId);
    }

    @Override
    public Boolean deletePermAddressById(int addressId) {
        Boolean success = false;
        addressRepository.deletePermAddressById(addressId);
        if (addressRepository.getAddress(addressId) == null) {
            success = true;
        }
        return success;
    }
}
