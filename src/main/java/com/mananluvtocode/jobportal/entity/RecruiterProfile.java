package com.mananluvtocode.jobportal.entity;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.SQLException;

@Entity
@Table(name = "recruiter_profile")
public class RecruiterProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_account_id")
    private Integer userAccountId;
    @Column(name = "city")
    private String city;
    @Column(name = "company")
    private String company;
    @Column(name = "country")
    private String country;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "profile_photo")
    @Lob
    private Blob profilePhoto;

    @Column(name = "state")
    private String state;


    @OneToOne
    @JoinColumn(name = "user_account_id")
    @MapsId
    private Users userid;

    // generating empty constructor

    public RecruiterProfile() {

    }


    public RecruiterProfile(String city, String company, String country, String firstName, String lastName, Blob profilePhoto, String state, Users userid) {
        this.city = city;
        this.company = company;
        this.country = country;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePhoto = profilePhoto;
        this.state = state;
    }

    public RecruiterProfile(Users users) {
        this.userid = users;
    }

    public Integer getUserAccountId() {
        return userAccountId;
    }

    public void setUserAccountId(Integer userAccountId) {
        this.userAccountId = userAccountId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Transient
    public ResponseEntity<byte[]> getPhotosImagePath() throws SQLException {
        if (profilePhoto == null) {
            return null;
        }
        byte[] profilePhotoBytes = null;
        profilePhotoBytes = profilePhoto.getBytes(1, (int) profilePhoto.length());
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(profilePhotoBytes);
    }

    public Blob getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(Blob profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public Users getUserid() {
        return userid;
    }

    public void setUserid(Users userid) {
        this.userid = userid;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "RecruiterProfile{" +
                "userAccountId=" + userAccountId +
                ", city='" + city + '\'' +
                ", company='" + company + '\'' +
                ", country='" + country + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", profilePhoto='" + profilePhoto + '\'' +
                ", userid=" + userid +
                '}';
    }
}
