package demo.login.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import demo.login.data.ERole;
import demo.login.data.Role;
import demo.login.payload.response.UserResponse;
import demo.login.repository.RoleRepository;
import demo.login.repository.UserRepository;

@RestController
@RequestMapping("/admin/api")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommonService commonService;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/users")
    public List<UserResponse> getUsers() {
        Set<Role> roles = Arrays.asList(ERole.ROLE_USER).stream().map(eRole -> roleRepository.findByName(eRole).get())
                .collect(Collectors.toSet());
        return userRepository.findAllByRolesIn(roles).stream().map(commonService::mapUserToUserResponse)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable(name = "id") Long id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
