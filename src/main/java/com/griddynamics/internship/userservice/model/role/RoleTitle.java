package com.griddynamics.internship.userservice.model.role;

public enum RoleTitle {
    ROLE_ADMIN,
    ROLE_USER;

    public static RoleTitle defaultTitle() {
        return ROLE_USER;
    }
}
