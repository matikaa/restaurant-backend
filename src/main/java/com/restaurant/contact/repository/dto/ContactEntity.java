package com.restaurant.contact.repository.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class ContactEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long contactId;

    private String contactPhoneNumber;

    private String contactMail;

    private String openingHoursDays;

    private String openingHoursOpenTime;

    private String openingHoursCloseTime;

    private String addressCity;

    private String addressStreet;

    private Integer addressNumber;

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long id) {
        this.contactId = id;
    }

    public String getContactPhoneNumber() {
        return contactPhoneNumber;
    }

    public void setContactPhoneNumber(String contactPhoneNumber) {
        this.contactPhoneNumber = contactPhoneNumber;
    }

    public String getContactMail() {
        return contactMail;
    }

    public void setContactMail(String contactMail) {
        this.contactMail = contactMail;
    }

    public String getOpeningHoursDays() {
        return openingHoursDays;
    }

    public void setOpeningHoursDays(String openingHoursDays) {
        this.openingHoursDays = openingHoursDays;
    }

    public String getOpeningHoursOpenTime() {
        return openingHoursOpenTime;
    }

    public void setOpeningHoursOpenTime(String openingHoursOpenTime) {
        this.openingHoursOpenTime = openingHoursOpenTime;
    }

    public String getOpeningHoursCloseTime() {
        return openingHoursCloseTime;
    }

    public void setOpeningHoursCloseTime(String openingHoursCloseTime) {
        this.openingHoursCloseTime = openingHoursCloseTime;
    }

    public String getAddressCity() {
        return addressCity;
    }

    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    public String getAddressStreet() {
        return addressStreet;
    }

    public void setAddressStreet(String addressStreet) {
        this.addressStreet = addressStreet;
    }

    public Integer getAddressNumber() {
        return addressNumber;
    }

    public void setAddressNumber(Integer addressNumber) {
        this.addressNumber = addressNumber;
    }
}
