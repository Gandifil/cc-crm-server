package sgu.csit.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import sgu.csit.backend.auth.JwtTokenUtil;
import sgu.csit.backend.model.MetersData;
import sgu.csit.backend.model.PeriodType;
import sgu.csit.backend.security.JwtUser;
import sgu.csit.backend.service.MetersDataService;
import sgu.csit.backend.service.UserService;

import java.util.Date;

@RestController
@PreAuthorize("hasRole({'USER'})")
public class MetersDataController {
    @Value("${jwt.header}")
    private String tokenHeader;

    private final JwtTokenUtil jwtTokenUtil;

    private final MetersDataService metersDataService;

    private final UserService userService;

    private final UserDetailsService userDetailsService;

    @Autowired
    public MetersDataController(
            JwtTokenUtil jwtTokenUtil,
            MetersDataService metersDataService,
            UserService userService, @Qualifier("jwtUserDetailsService") UserDetailsService userDetailsService
    ) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.metersDataService = metersDataService;
        this.userService = userService;
        this.userDetailsService = userDetailsService;
    }

    //@CrossOrigin(origins = "**", maxAge = 3600)
    @RequestMapping(value = "/meters/send", method = RequestMethod.POST)
    public ResponseEntity sendMetersData(
            @AuthenticationPrincipal JwtUser jwtUser,
            @RequestBody MetersData metersData
    ) {
        metersData.setDate(new Date());
        metersData.setUser(userService.getUserById(jwtUser.getId()));
        metersDataService.addMetersData(metersData);
        return ResponseEntity.ok("Meters data have been sent successfully!");
    }

    @RequestMapping(value = "/meters/", method = RequestMethod.GET)
    public ResponseEntity getAllMetersData(
            @AuthenticationPrincipal JwtUser user,
            @RequestParam("periodType") PeriodType periodType
    ) {
        Iterable<MetersData> metersData =
                metersDataService.getAllMetersDataByUserId(periodType, user.getId());
        return ResponseEntity.ok(metersData);
    }
}
