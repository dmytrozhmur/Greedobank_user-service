package com.griddynamics.internship.userservice.model.token;

import com.griddynamics.internship.userservice.model.user.User;
import lombok.Getter;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.time.Instant;
import java.util.Objects;

@Getter
@Entity(name = "refreshment")
public class Refreshment {
    @Id
    @GeneratedValue private long id;
    @Column(nullable = false, unique = true) private String token;
    @Column private Instant expiration;
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id") private User user;

    protected Refreshment() {}

    public Refreshment(String token) {
        this(token, Instant.MAX);
    }

    public Refreshment(String token, Instant expiration) {
        this.token = token;
        this.expiration = expiration;
    }

    public Refreshment(String token, User user) {
        this(token, Instant.MAX, user);
    }

    public Refreshment(String token, Instant expiration, User user) {
        this.token = token;
        this.expiration = expiration;
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Refreshment that = (Refreshment) o;
        return token != null && Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return 43 * token.length();
    }
}
