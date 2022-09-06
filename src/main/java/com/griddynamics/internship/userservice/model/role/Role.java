package com.griddynamics.internship.userservice.model.role;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "role")
public class Role {
    @JsonProperty("role_id")
    @Id @GeneratedValue private int id;
    @JsonProperty("title")
    @Enumerated(EnumType.STRING) @Column private RoleTitle title;
}
