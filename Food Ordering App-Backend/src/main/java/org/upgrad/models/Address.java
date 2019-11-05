package org.upgrad.models;

import javax.persistence.*;

@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String flatBuilNumber;
    private String locality;
    private String city;
    private String zipcode;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "state_id", nullable = false)
    private States state;

    public Address() {
    }

    public Address(Integer id, String flatBuilNumber, String locality, String city, String zipcode, States state) {
        this.id = id;
        this.flatBuilNumber = flatBuilNumber;
        this.locality = locality;
        this.city = city;
        this.zipcode = zipcode;
        this.state = state;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFlatBuilNumber() {
        return flatBuilNumber;
    }

    public void setFlatBuilNumber(String flatBuilNumber) {
        this.flatBuilNumber = flatBuilNumber;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public States getState() {
        return state;
    }

    public void setState(States state) {
        this.state = state;
    }
}
