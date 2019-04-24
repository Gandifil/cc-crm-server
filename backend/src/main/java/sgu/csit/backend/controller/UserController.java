package sgu.csit.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sgu.csit.backend.auth.JwtTokenUtil;
import sgu.csit.backend.dto.UserDTO;
import sgu.csit.backend.security.JwtUser;
import sgu.csit.backend.service.UserService;

@RestController
public class UserController {
    private final UserDetailsService userDetailsService;
    private final UserService userService;

    @Autowired
    public UserController(@Qualifier("jwtUserDetailsService") UserDetailsService userDetailsService,
                          UserService userService) {
        this.userDetailsService = userDetailsService;
        this.userService = userService;
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public ResponseEntity getProfile(@AuthenticationPrincipal JwtUser jwtUser) {
        UserDTO userDTO = new UserDTO(userService.getUserById(jwtUser.getId()));
        return ResponseEntity.ok(userDTO);
    }
}
