package com.example.securitymedianet.Services.Role;

import com.example.securitymedianet.Entites.Role;
import com.example.securitymedianet.Entites.User;
import com.example.securitymedianet.Repositories.RoleRepository;
import com.example.securitymedianet.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServices implements IRoleServices{

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Override
    public Role createRole(String roleName) {
        Role role = new Role();
        role.setName(roleName);
        return roleRepository.save(role);
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
    public List<User> findUsersWithOnlyUserRol(){
        return userRepository.findUsersWithOnlyUserRole();
    }
    @Override
    public List<User> getUsersByRole(String roleName) {


        Role role = roleRepository.findByName(roleName).orElse(null);
        if (roleName=="USER"){
            return userRepository.findUsersWithOnlyUserRole();
        }
        if (role != null) {



                return userRepository.findAll().stream()
                        .filter(user -> user.getRoles().contains(role))
                        .collect(Collectors.toList());

        }
        return List.of(); // Return an empty list if role not found
    }

}
