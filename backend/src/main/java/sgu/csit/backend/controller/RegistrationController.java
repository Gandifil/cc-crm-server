package sgu.csit.backend.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sgu.csit.backend.exception.RegistrationException;
import sgu.csit.backend.model.User;
import sgu.csit.backend.service.UserService;

@RestController
public class RegistrationController {
    @Value("${jwt.header}")
    private String tokenHeader;

    private final UserService userService;

    private ModelMapper modelMapper;

    @Autowired
    public RegistrationController(
            UserService userService,
            ModelMapper modelMapper
    ) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @RequestMapping(value = "${jwt.route.registration.path}", method = RequestMethod.POST)
    public ResponseEntity registerAndCreateAuthenticationToken(@RequestBody User user) throws RegistrationException {
        if (userService.register(user)) {
            return ResponseEntity.ok("Account created");
        }
        return ResponseEntity.unprocessableEntity().body("Account already exists");
    }

    @ExceptionHandler({RegistrationException.class})
    public ResponseEntity<String> handleRegistrationException(RegistrationException e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
    }
}
