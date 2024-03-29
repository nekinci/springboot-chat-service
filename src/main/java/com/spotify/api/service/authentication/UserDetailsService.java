package com.spotify.api.service.authentication;

import com.spotify.api.model.User;
import com.spotify.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByEmailAndEndDateNull(s);
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getId(), new ArrayList<>());
    }
}
