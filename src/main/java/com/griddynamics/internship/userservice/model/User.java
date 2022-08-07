package com.griddynamics.internship.userservice.model;


import com.griddynamics.internship.userservice.communication.request.SignupRequest;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import java.util.Objects;

@Getter
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(generator = "sequence-generator")
    @GenericGenerator(
            name = "sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "user_sequence"),
                    @Parameter(name = "initial_value", value = "6"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    @Column(name = "id") private int id;
    @Column(name = "firstname") private String firstName;
    @Column(name = "lastname") private String lastName;
    @Column(name = "email") private String email;
    @Column(name = "password") private String password;

    protected User() {}

    public User(SignupRequest signup, String password) {
        this.firstName = signup.getFirstName();
        this.lastName = signup.getLastName();
        this.email = signup.getEmail();
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(firstName, user.firstName)
                && Objects.equals(lastName, user.lastName)
                && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return 43 * (email == null ? 0 : email.length());
    }
}
