package de.hska.iwi.vslab.userservice;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ResponseEntity<User> createNewProduct(@RequestBody User user) {
        try {
            User newUser = userRepository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // @RequestMapping(value = "/users/{username}", method = RequestMethod.GET)
    // public User getProductById(@PathVariable String username) {
    //     return userRepository.findByUsername(username)
    //             .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));
    // }
}