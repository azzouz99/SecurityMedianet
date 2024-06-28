package com.example.securitymedianet.Services.User;

import com.example.securitymedianet.Entites.Role;
import com.example.securitymedianet.Entites.User;
import com.example.securitymedianet.Repositories.RoleRepository;
import com.example.securitymedianet.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServices implements IUserServices {
    @Autowired
private UserRepository userRepository;
@Autowired
private RoleRepository roleRepository;


@Override
public User updateUser(User user) {
    return userRepository.save(user);
}

@Override
public User updatePhoto(Integer userId, String photo) {
    User user = userRepository.findById(userId).orElse(null);
    user.setImage(photo);
    return userRepository.save(user);
}
@Override
public void assignRoleToUser(String username, String roleName) {
    Optional<User> userOptional = userRepository.findByEmail(username);
    Optional<Role> roleOptional = roleRepository.findByName(roleName);

    if (userOptional.isPresent() && roleOptional.isPresent()) {
        User user = userOptional.get();
        Role role = roleOptional.get();
        user.getRoles().add(role);
        userRepository.save(user);
    } else {
        throw new RuntimeException("User or Role not found");
    }
}
@Override
    public Role createRole(String roleName) {
   return roleRepository.save(Role.builder().name(roleName).build());
    }

    public List<User> getUsersByRole(String roleName) {
        Role role = roleRepository.findByName(roleName).orElse(null);
        if (role != null) {
            return userRepository.findAll().stream()
                    .filter(user -> user.getRoles().contains(role))
                    .collect(Collectors.toList());
        }
        return List.of(); // Return an empty list if role not found
    }


}
