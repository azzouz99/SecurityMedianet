package com.example.securitymedianet.Controllers;

import com.example.securitymedianet.Entites.Role;
import com.example.securitymedianet.Entites.User;
import com.example.securitymedianet.Services.Role.IRoleServices;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {
    private final IRoleServices roleService;
    @PostMapping("/create")
    public ResponseEntity<Role> createRole(@RequestBody String roleName) {
        Role role = roleService.createRole(roleName);
        return ResponseEntity.ok(role);}
    @PostMapping("/assign")
    public ResponseEntity<Void> assignRoleToUser(@RequestParam String username, @RequestParam String roleName) {
        roleService.assignRoleToUser(username, roleName);
        return ResponseEntity.ok().build();}
    @GetMapping("/role/{name}")
    public List<User> getUsersByRole(@PathVariable String name) {
        if ("USER".equals(name)){
            return roleService.findUsersWithOnlyUserRol();
        }else {
           return  roleService.getUsersByRole(name);}}
    @GetMapping("/role/u")
    public List<User> getUsersRole() {
        return roleService.findUsersWithOnlyUserRol();
    }
}
