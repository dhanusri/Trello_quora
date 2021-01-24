package com.upgrad.quora.service.entity;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@SuppressWarnings("all")
@Entity
@Table(name = "users")
/**
 * this is UserEntity class used for authorised user validation

 * */
@NamedQueries(
        {
                @NamedQuery(name = "userByUserName", query = "select u from UserEntity u where u.userName = :userName"),
                @NamedQuery(name = "userByEmail", query = "select u from UserEntity u where u.email = :email"),
                @NamedQuery(name = "userByUserUuid", query = "select u from UserEntity u where u.uuid = :uuid")
        }
)
public class UserEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "uuid")
    @Size(max = 64)
    private String uuid;

    @Column(name = "firstname")
    @NotNull
    @Size(max = 30)
    private String firstName;

    @Column(name = "lastname")
    @NotNull
    @Size(max = 30)
    private String lastName;

    @Column(name = "username")
    @NotNull
    @Size(max = 30)
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    @NotNull
    @Size(max = 200)
    private String email;

    @Column(name = "contactnumber")
    @NotNull
    @Size(max = 50)
    private String contactnumber;

    @Column(name = "country")
    @Size(max = 30)
    private String country;

    @Column(name = "aboutme")
    @Size(max = 50)
    private String aboutMe;

    @Column(name = "dob")
    @Size(max = 30)
    private String dob;

    @Column(name = "role")
    private String role;

    @Column(name = "salt")
    @NotNull
    @Size(max = 200)
    private String salt;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }


    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getContactNumber() {
        return contactnumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactnumber = contactNumber;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }



}
