package com.example.securitymedianet.Services.User;

import com.example.securitymedianet.Entites.Role;
import com.example.securitymedianet.Entites.User;

public interface IUserServices {
    User updateUser(User user);

    User updatePhoto(Integer userId, String photo);

    void assignRoleToUser(String username, String roleName);

    Role createRole(String roleName);
}
