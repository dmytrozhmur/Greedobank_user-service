package com.griddynamics.internship.userservice.model.child;

import com.griddynamics.internship.userservice.model.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class ChildAccount {
    @Id
    @GeneratedValue private Integer id;

    @OneToOne private User createdForUser;

    private Instant createdAt = Instant.now();

    @ManyToMany private List<User> parents;
}
