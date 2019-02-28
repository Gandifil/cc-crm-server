package sgu.csit.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sgu.csit.backend.domain.User;
import sgu.csit.backend.repository.UserRepository;

@RestController
public class UserController {
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test() {
        userRepository.save(new User("Test 1"));
        userRepository.save(new User("Test 2"));
        userRepository.save(new User("Test 3"));
        return "Ok";
    }
}
