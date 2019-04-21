package sgu.csit.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import sgu.csit.backend.auth.JwtTokenUtil;
import sgu.csit.backend.model.MetersData;
import sgu.csit.backend.model.PeriodType;
import sgu.csit.backend.model.User;
import sgu.csit.backend.service.MetersDataService;
import sgu.csit.backend.service.UserService;

@RestController
@PreAuthorize("hasRole({'ADMIN'})")
@RequestMapping("/manage/")
public class ManageController {
    @Value("${jwt.header}")
    private String tokenHeader;

    private final JwtTokenUtil jwtTokenUtil;

    private final MetersDataService metersDataService;

    private final UserDetailsService userDetailsService;

    private final UserService userService;

    @Autowired
    public ManageController(
            JwtTokenUtil jwtTokenUtil,
            MetersDataService metersDataService,
            @Qualifier("jwtUserDetailsService") UserDetailsService userDetailsService,
            UserService userService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.metersDataService = metersDataService;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
    }

    @RequestMapping(value = "/meters/", method = RequestMethod.GET)
    public ResponseEntity getAllMetersData(@RequestParam("periodType") PeriodType periodType) {
        Iterable<MetersData> metersData = metersDataService.getAllMetersData(periodType);
        return ResponseEntity.ok(metersData);
    }

    @RequestMapping(value = "/meters/{userId}", method = RequestMethod.GET)
    public ResponseEntity getAllMetersData(
            @RequestParam("periodType") PeriodType periodType,
            @PathVariable("userId") Long userId
    ) {
        Iterable<MetersData> metersData = metersDataService.getAllMetersDataByUserId(periodType, userId);
        return ResponseEntity.ok(metersData);
    }

    @RequestMapping(value = "/users/", method = RequestMethod.GET)
    public ResponseEntity getUsers(@RequestParam("periodType") PeriodType periodType) {
        Iterable<User> users = userService.getUsers(periodType);
        return ResponseEntity.ok(users);
    }

}
