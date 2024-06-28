package com.example.securitymedianet.Services.Role;

import com.example.securitymedianet.Entites.Role;
import com.example.securitymedianet.Entites.User;

import java.util.List;

public interface IRoleServices {
    Role createRole(String roleName);

    void assignRoleToUser(String username, String roleName);

    List<User> findUsersWithOnlyUserRol();

    List<User> getUsersByRole(String roleName);
}
