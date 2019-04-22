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

import java.util.Map;
import java.util.Set;

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

    // meters by user's apartment
    @RequestMapping(value = "/meters/{userApart}", method = RequestMethod.GET)
    public ResponseEntity getAllMetersData(
            @RequestParam("periodType") PeriodType periodType,
            @PathVariable("userApart") Integer userApart
    ) {
        Iterable<MetersData> metersData = metersDataService.getAllMetersDataByUserApart(periodType, userApart);
        return ResponseEntity.ok(metersData);
    }

    // all apartments with meters
    @RequestMapping(value = "/meters/", method = RequestMethod.GET)
    public ResponseEntity getAllApartments(@RequestParam("periodType") PeriodType periodType) {
        Map<Integer, Set<MetersData>> apartments = userService.getAllApartments(periodType);
        return ResponseEntity.ok(apartments);
    }

    // bad apartments with users
    @RequestMapping(value = "/users/", method = RequestMethod.GET)
    public ResponseEntity getBadApartments(@RequestParam("periodType") PeriodType periodType) {
        Map<Integer, Set<User>> badApartments = userService.getBadApartments(periodType);
        return ResponseEntity.ok(badApartments);
    }

}
