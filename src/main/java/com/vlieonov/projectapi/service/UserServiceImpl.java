package com.vlieonov.projectapi.service;

import com.vlieonov.projectapi.api.model.User;
import com.vlieonov.projectapi.api.secure.UserPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public User getUser(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));
    }


    @Override
    public String createUser(User user) {
        userRepository.save(user);
        return "1";
    }

    @Override
    public String updateUser(User user) {
        userRepository.save(user);
        return "1";
    }

    @Override
    public String deleteUser(int id) {
        userRepository.deleteById(id);
        return "1";
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            System.out.println("User not found");
            throw new UsernameNotFoundException("User not found");
        }
        return new UserPrincipal(user);
    }
}
