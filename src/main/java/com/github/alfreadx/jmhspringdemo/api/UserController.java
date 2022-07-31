package com.github.alfreadx.jmhspringdemo.api;

import com.github.alfreadx.jmhspringdemo.model.User;
import com.github.alfreadx.jmhspringdemo.model.UserCreateIO;
import com.github.alfreadx.jmhspringdemo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController

public class UserController extends AbstractApiController {

    private final UserService userSvc;

    @Autowired
    public UserController(UserService userSvc) {
        this.userSvc = userSvc;
    }

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable Long id) {
        Optional<User> optUser = userSvc.findUser(id);
        return optUser.orElse(null);
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userSvc.getUsers();
    }

    @PostMapping("/user")
    public void addUser(@RequestBody UserCreateIO createIO) {
        userSvc.createUser(createIO);
    }
}
