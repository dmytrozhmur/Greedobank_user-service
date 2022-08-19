package com.griddynamics.internship.userservice.model.user;

import com.griddynamics.internship.userservice.model.role.Role;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import java.util.Objects;

@Getter
@Entity(name = "user")
@RequiredArgsConstructor
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
    @Column(name = "firstname") @NonNull private String firstName;
    @Column(name = "lastname") @NonNull private String lastName;
    @Column(name = "email") @NonNull private String email;
    @Column(name = "password") @NonNull private String password;
    @ManyToOne @JoinColumn(name = "role_id") @NonNull private Role role;

    protected User() {}

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
        return 43 * email.length();
    }
}
