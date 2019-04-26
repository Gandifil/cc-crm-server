package sgu.csit.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import sgu.csit.backend.auth.JwtTokenUtil;
import sgu.csit.backend.dto.MetersDataDTO;
import sgu.csit.backend.dto.UserDTO;
import sgu.csit.backend.model.PeriodType;
import sgu.csit.backend.service.MetersDataService;
import sgu.csit.backend.service.UserService;

import java.util.Date;
import java.util.List;
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
    @RequestMapping(value = "/aparts/{userApart}", method = RequestMethod.GET)
    public ResponseEntity getApartment(
            @RequestParam("periodType") PeriodType periodType,
            @PathVariable("userApart") Integer userApart
    ) {
        List<MetersDataDTO> metersData = metersDataService.getApartment(userApart, periodType);
        return ResponseEntity.ok(metersData);
    }

    // all apartments with meters by standard period
    @RequestMapping(value = "/aparts/", method = RequestMethod.GET)
    public ResponseEntity getAllApartments(@RequestParam("periodType") PeriodType periodType) {
        Map<Integer, List<MetersDataDTO>> apartments = userService.getAllApartments(periodType);
        return ResponseEntity.ok(apartments);
    }
    // all apartments with meters by range period
    @RequestMapping(value = "/aparts/range", method = RequestMethod.GET)
    public ResponseEntity getAllApartmentsByRange(
            @RequestParam("fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fromDate,
            @RequestParam("toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date toDate
    ) {
        System.out.println("Looking up for aparts by range...");
        System.out.println("From date: " + fromDate);
        System.out.println("To date: " + toDate);
        Map<Integer, List<MetersDataDTO>> apartments = userService.getAllApartmentsByRange(fromDate, toDate);
        return ResponseEntity.ok(apartments);
    }

    // bad apartments with users by standard period
    @RequestMapping(value = "/bad_aparts/", method = RequestMethod.GET)
    public ResponseEntity getBadApartments(@RequestParam("periodType") PeriodType periodType) {
        Map<Integer, List<UserDTO>> badApartments = userService.getBadApartments(periodType);
        return ResponseEntity.ok(badApartments);
    }
    // bad apartments with users by range period
    @RequestMapping(value = "/bad_aparts/range", method = RequestMethod.GET)
    public ResponseEntity getBadApartmentsByRange(
            @RequestParam("fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fromDate,
            @RequestParam("toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date toDate
    ) {
        System.out.println("Looking up for bad aparts by range...");
        System.out.println("From date: " + fromDate);
        System.out.println("To date: " + toDate);
        Map<Integer, List<UserDTO>> badApartments = userService.getBadApartmentsByRange(fromDate, toDate);
        return ResponseEntity.ok(badApartments);
    }

}
