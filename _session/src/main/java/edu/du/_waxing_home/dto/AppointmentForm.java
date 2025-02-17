package edu.du._waxing_home.dto;

import java.time.LocalDate;

public class AppointmentForm {
    private String name;
    private String email;
    private String phonenumber;
    private String gender;
    private LocalDate reservationDate;
    private int hourselect;
    private int minutesselect;
    private String treatment1;
    private String treatment2;
    private String additionalText;

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonumber) {
        this.phonenumber = phonumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }

    public int getHourselect(){return hourselect = hourselect; }

    public void setHourselect(int hourselect) {this.hourselect = hourselect; }

    public int getMinutesselect(){return minutesselect; }

    public void setMinutesselect(int minutesselect) {this.minutesselect = minutesselect; }

    public String getTreatment1() {
        return treatment1;
    }

    public void setTreatment1(String treatment1) {
        this.treatment1 = treatment1;
    }

    public String getTreatment2() {
        return treatment2;
    }

    public void setTreatment2(String treatment2) {
        this.treatment2 = treatment2;
    }

    public String getAdditionalText() {
        return additionalText;
    }

    public void setAdditionalText(String additionalText) {
        this.additionalText = additionalText;
    }
}