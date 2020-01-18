package de.hska.iwi.vslab.authorizationservice.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import de.hska.iwi.vslab.authorizationservice.model.User;
import de.hska.iwi.vslab.authorizationservice.repository.UserRepository;

@Service("userService")
public class WebshopUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public WebshopUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOpt = userRepository.findByUsername(username);
        return userOpt.orElseThrow(() -> new UsernameNotFoundException("User with name '" + username + "' not found"));
    }

}
