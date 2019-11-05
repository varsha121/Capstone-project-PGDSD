package org.upgrad.repositories;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.upgrad.models.Address;

import java.util.List;

@Repository
public interface AddressRepository extends CrudRepository<Address, Integer> {

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO ADDRESS(flat_buil_number, locality, city, zipcode, state_id) VALUES(?1,?2,?3,?4,?5)")
    void addAddress(String flatBuilNumber, String locality, String city, String zipcode, Integer stateId);

    @Query(nativeQuery = true, value = "SELECT ID FROM ADDRESS ORDER BY ID DESC LIMIT 1")
    int findIdForLatestAddress();

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO USER_ADDRESS(type, user_id, address_id) VALUES(?1,?2,?3)")
    void userAddressMapping(String type, Integer userId, Integer addressId);

    @Query(nativeQuery = true, value = "SELECT * FROM ADDRESS WHERE ID=?1")
    Integer getAddress(int addressId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE ADDRESS SET flat_buil_number=?1, locality=?2, city=?3, zipcode=?4 WHERE id=?5")
    void updatePermAddress(String flatBuilNumber, String locality, String city, String zipcode, Integer addressId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE ADDRESS SET flat_buil_number=?1, locality=?2, city=?3, zipcode=?4 WHERE id=?5")
    void updatePermAddressWithoutState(String flatBuilNumber, String locality, String city, String zipcode, Integer addressId);

    @Query(nativeQuery = true, value = "SELECT * FROM ADDRESS A INNER JOIN USER_ADDRESS U ON U.ADDRESS_ID = A.ID WHERE TYPE LIKE '%perm%' AND USER_ID=?1")
    List<Address> getPermAddress(int userId);

    @Query(nativeQuery = true, value = "DELETE FROM ADDRESS WHERE ID=?1 RETURNING *")
    void deletePermAddressById(int addressId);


    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE ADDRESS SET flat_buil_number=?1, locality=?2, city=?3, zipcode=?4, state_id=?5 WHERE id=?6")
    void updatePermAddressWithOutStateId(String flatBuilNumber, String locality, String city, String zipcode, Integer stateId, Integer addressId);

}
