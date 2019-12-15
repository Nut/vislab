package de.hska.iwi.vslab.userservice;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import de.hska.iwi.vslab.userservice.datamodels.NewUser;
import de.hska.iwi.vslab.userservice.datamodels.Role;
import de.hska.iwi.vslab.userservice.datamodels.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ResponseEntity<User> createNewUser(@RequestBody NewUser user) {

        // check if Role exists, otherwise return 404
        // 1 -> regular User, 2-> Admin
        Role role = roleRepository.findByLevel(user.getRolelevel())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Role level " + user.getRolelevel() + " not found!"));

        try {
            User newUser = new User(user.getUsername(), user.getFirstname(), user.getLastname(), user.getPassword(),
                    role);
            userRepository.save(newUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @RequestMapping(value = "/users/{username}", method = RequestMethod.GET)
    public User getUserByUsername(@PathVariable String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));
    }

    @RequestMapping(value = "/roles/{level}", method = RequestMethod.GET)
    public Role getRoleByLevel(@PathVariable int level) {
        return roleRepository.findByLevel(level).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role level " + level + " not found!"));
    }
}