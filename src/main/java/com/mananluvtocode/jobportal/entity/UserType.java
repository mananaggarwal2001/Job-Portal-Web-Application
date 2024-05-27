package com.mananluvtocode.jobportal.entity;

import jakarta.persistence.*;

import java.lang.annotation.Target;
import java.util.List;

@Entity
@Table(name = "users_type")
public class UserType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_type_id")
    private Long userTypeId;

    @Column(name = "user_type_name")
    private String userTypeName;


    @OneToMany(targetEntity = Users.class, mappedBy = "userTypeId", cascade = CascadeType.ALL)
    private List<Users> users;

    public UserType() {

    }

    public UserType(String userTypeName) {
        this.userTypeName = userTypeName;
    }

    public Long getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(Long userTypeId) {
        this.userTypeId = userTypeId;
    }

    public String getUserTypeName() {
        return userTypeName;
    }

    public void setUserTypeName(String userTypeName) {
        this.userTypeName = userTypeName;
    }

    public List<Users> getUsers() {
        return users;
    }

    public void setUsers(List<Users> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "UserType{" +
                "userTypeId=" + userTypeId +
                ", userTypeName='" + userTypeName + '\'' +
                '}';
    }
}
