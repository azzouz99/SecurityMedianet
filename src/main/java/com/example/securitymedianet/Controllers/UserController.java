package com.example.securitymedianet.Controllers;
import com.example.securitymedianet.Entites.Role;
import com.example.securitymedianet.Entites.User;

import com.example.securitymedianet.Repositories.UserRepository;
import com.example.securitymedianet.Services.FileStorageService;
import com.example.securitymedianet.Services.User.IUserServices;
import com.example.securitymedianet.Services.User.UserServices;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IUserServices userServices;
    @Autowired
    private FileStorageService fileStorageService;
    @PostMapping("/upload")
    public ResponseEntity<?> uploadUserImage(@RequestParam("file") MultipartFile file, Authentication authentication) {
     //   String filePath = "src/main/resources/static/assets/image/UserPhoto/" + file.getOriginalFilename();
        User user = (User) authentication.getPrincipal();
        String fileName = fileStorageService.storeFile(file, user.getId());
        user.setImage(fileName);
        userRepository.save(user);
        return ResponseEntity.ok("File uploaded successfully");
    }
    @PostMapping("/new/role/{name}")
    public ResponseEntity<?> createNewRole(@PathVariable String name) {
        Role role=userServices.createRole(name);
        return ResponseEntity.ok("role added successfully");
    }
    @PutMapping("/assign/role/{email}/{role}")
    public ResponseEntity<Void> assignRoleToUser(@PathVariable String email, @PathVariable String role) {
        userServices.assignRoleToUser(email, role);
        return ResponseEntity.ok().build();}


}
