package com.kruten.jarsofttesttask.security.entity;

import lombok.Getter;

@Getter
public enum ERole {
    ROLE_USER ("user"),
    ROLE_ADMIN("admin");

    private String role;

    ERole(String role) {
        this.role = role;
    }

}
