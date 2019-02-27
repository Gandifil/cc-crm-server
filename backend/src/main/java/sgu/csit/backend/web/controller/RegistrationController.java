package sgu.csit.backend.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sgu.csit.backend.domain.User;
import sgu.csit.backend.repository.UserRepository;

@Controller
@RequestMapping("/register")
public class RegistrationController {
    @Autowired
    private UserRepository userRepository;

    public @ResponseBody
    String registerNewUser(
            @RequestParam String email,
            @RequestParam String surname,
            @RequestParam String name,
            @RequestParam String patronymic,
            @RequestParam String city,
            @RequestParam String street,
            @RequestParam String homeNumber,
            @RequestParam int apartmentNumber) {
        User user = new User();
        user.setEmail(email);
        user.setSurname(surname);
        user.setName(name);
        user.setPatronymic(patronymic);
        user.setCity(city);
        user.setStreet(street);
        user.setHomeNumber(homeNumber);
        user.setApartmentNumber(apartmentNumber);

        return "OK";
    }

    @GetMapping(path = "/users")
    public @ResponseBody
    Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }
}