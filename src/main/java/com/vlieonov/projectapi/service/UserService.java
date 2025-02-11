package com.vlieonov.projectapi.service;

import com.vlieonov.projectapi.api.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private List<User> userList;

    public UserService() {
        userList = new ArrayList<>();

        User user1 = new User(1, "Test1", "test1@gmail.com");
        User user2 = new User(2, "Test2", "test2@gmail.com");
        User user3 = new User(3, "Test3", "test3@gmail.com");
        User user4 = new User(4, "Test4", "test4@gmail.com");
        User user5 = new User(5, "Test5", "test5@gmail.com");

        userList.addAll(Arrays.asList(user1, user2, user3, user4, user5));
    }

    public Optional<User> getUser(Integer id) {
        Optional optional = Optional.empty();
        for (User user: userList){
            if (user.getId() == id){
                optional = Optional.of(user);
                return optional;
            }
        }
        return optional;
    }
}
