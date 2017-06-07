package com.galvanize.api;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import com.galvanize.model.*;
import com.galvanize.Repository.*;

import java.util.List;

@RestController
public class UsersController {

    private UserRespository repository;

    public UsersController(UserRespository repository) {
        this.repository = repository;
    }


    @GetMapping("/users")
    public AllUsers getall() {

        AllUsers a = new AllUsers();
        a.setUsers((List<User>) this.repository.findAll());
        System.out.println("this.repository.findAll()");
        return a;
    }

    @PostMapping("/users")
    @JsonView(Views.DetailView.class)
    public User createUser(@RequestBody SimpleUser simpleUser) {

        String password = simpleUser.getUser().getPassword();

        String newpassowrd = DigestUtils.md5DigestAsHex(password.getBytes());

        User user = new User();
        user.setEmail(simpleUser.getUser().getEmail());
        user.setPassword(newpassowrd);

        return this.repository.save(user);

    }

    @GetMapping("/users/{id}")
    @JsonView(Views.DetailView.class)
    public SimpleUser getUserbyID(@PathVariable int id) {

        SimpleUser user = new SimpleUser();

        user.setUser(this.repository.findById(id));

        return user;

    }


    @PatchMapping("/users/{id}")
    @JsonView(Views.DetailView.class)
    public User updateUserByID(@PathVariable int id, @RequestBody User user) {

        String newpassword = null;

        if (user.getPassword() != null) {

            newpassword = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
            int count = this.repository.updateUserEmailAndPasswordbyId(user.getEmail(), newpassword, id);
            if (count > 0) {
                return this.repository.findById(id);
            }

        } else {
            int count = this.repository.updateUserEmailbyId(user.getEmail(), id);
            if (count > 0) {
                return this.repository.findById(id);
            }
        }
        return null;
    }

    @DeleteMapping("/users/{id}")
    public Counter deleteUserbyID(@PathVariable int id) {

        this.repository.delete(id);

        Long l = this.repository.count();
        Counter c = new Counter();

        if (l>0) {
            c.setCount(l.intValue());
            return c;
        }
        return null;

    }

    @PostMapping("/users/authenticate")
    @JsonView(Views.DetailView.class)
    public SimpleUser authenticateUser(@RequestBody User user) {

        String password = user.getPassword();

        String newpassword = DigestUtils.md5DigestAsHex(password.getBytes());

        User dbuser = this.repository.findByEmail(user.getEmail());

        if (dbuser.getPassword().equalsIgnoreCase(newpassword)) {
            SimpleUser s = new SimpleUser();
            s.setAuthenticated(true);
            s.setUser(dbuser);
            return s;
        } else {
            SimpleUser s = new SimpleUser();
            s.setAuthenticated(false);
            return s;
        }

    }


}
