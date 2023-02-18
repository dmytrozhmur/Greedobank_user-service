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

    @ManyToMany
    @JoinTable(name = "user_child",
            inverseJoinColumns = @JoinColumn(name = "user_id"),
            joinColumns = @JoinColumn(name = "child_id")) private List<User> parents;
}
