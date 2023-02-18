package com.griddynamics.internship.userservice.model.user;

import com.griddynamics.internship.userservice.model.child.ChildAccount;
import com.griddynamics.internship.userservice.model.role.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

import java.util.List;
import java.util.Objects;

@Getter @Setter
@Entity(name = "user")
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class User {
    @Id
    @GeneratedValue(generator = "sequence-generator")
    @GenericGenerator(
            name = "sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "user_sequence"),
                    @Parameter(name = "initial_value", value = "11"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    @Column(name = "id") private int id;
    @Column(name = "firstname") @NonNull private String firstName;
    @Column(name = "lastname") @NonNull private String lastName;
    @Column(name = "email") @NonNull private String email;
    @Column(name = "password") @NonNull private String password;
    @ManyToOne @JoinColumn(name = "role_id") @NonNull private Role role;
    @ManyToMany
    @JoinTable(name = "user_child",
            inverseJoinColumns = @JoinColumn(name = "child_id"),
            joinColumns = @JoinColumn(name = "user_id")) private List<ChildAccount> children;

    public User(int id, @NonNull String email, @NonNull String password, @NonNull Role role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
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
        return 43 * email.length();
    }
}
