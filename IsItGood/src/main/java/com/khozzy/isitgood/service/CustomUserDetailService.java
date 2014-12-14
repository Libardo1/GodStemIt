package com.khozzy.isitgood.service;

import com.khozzy.isitgood.domain.User;
import com.khozzy.isitgood.domain.UserRepositoryUserDetails;
import com.khozzy.isitgood.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(username);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("User %s does not exist!", username));
        }

        return new UserRepositoryUserDetails(user);
    }
}
