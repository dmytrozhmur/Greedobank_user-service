package com.griddynamics.internship.userservice.model;


import javax.persistence.*;

import java.util.Objects;

import static com.griddynamics.internship.userservice.utils.Encryptor.encrypt;

@Entity
@Table(name = "user")
public class User {
    @Id @GeneratedValue(strategy= GenerationType.AUTO) @Column(name = "id") private int id;
    @Column(name = "firstname") private String firstName;
    @Column(name = "lastname") private String lastName;
    @Column(name = "email") private String email;
    @Column(name = "password") private String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        this.password = encrypt(password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName)
                && Objects.equals(email, user.email) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        int hash = 43 * id;
        hash += email == null ? 0 : email.length();
        return hash;
    }
}
