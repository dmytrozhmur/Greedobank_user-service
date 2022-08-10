package com.griddynamics.internship.userservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity @Table(name = "role")
public class Role {
    @Id @GeneratedValue private int id;
    @Enumerated(EnumType.STRING) @Column private RoleTitle title;
}
