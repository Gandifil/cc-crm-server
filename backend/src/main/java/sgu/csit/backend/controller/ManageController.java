package sgu.csit.backend.controller;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import sgu.csit.backend.auth.JwtTokenUtil;
import sgu.csit.backend.model.MetersData;
import sgu.csit.backend.model.PeriodType;
import sgu.csit.backend.service.MetersDataService;

@Repository
@PreAuthorize("hasRole({'ADMIN'})")
public class ManageController {
    @Value("${jwt.header}")
    private String tokenHeader;

    private final JwtTokenUtil jwtTokenUtil;

    private final MetersDataService metersDataService;

    private final UserDetailsService userDetailsService;

    @Autowired
    public ManageController(JwtTokenUtil jwtTokenUtil,
                                MetersDataService metersDataService,
                                @Qualifier("jwtUserDetailsService") UserDetailsService userDetailsService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.metersDataService = metersDataService;
        this.userDetailsService = userDetailsService;
    }

    @RequestMapping(value = "/manage/meters", method = RequestMethod.GET)
    public ResponseEntity getAllMetersData(@RequestParam("periodType") PeriodType periodType) {
        Iterable<MetersData> metersData = metersDataService.getAllMetersData(periodType);
        return ResponseEntity.ok(metersData);
    }

    @RequestMapping(value = "/manage/meters", method = RequestMethod.GET)
    public ResponseEntity getAllMetersData(@RequestParam("periodType") PeriodType periodType,
                                           @RequestParam("userId") Long userId) {
        Iterable<MetersData> metersData = metersDataService.getAllMetersDataByUserId(periodType, userId);
        return ResponseEntity.ok(metersData);
    }

    @RequestMapping(value = "/manage/old_meters/", method = RequestMethod.GET)
    public ResponseEntity getAllMetersDataFromIrresponsibleUsers(@RequestParam("periodType") PeriodType periodType) {
        Iterable<MetersData> metersData = metersDataService.getAllMetersDataFromIrresponsibleUsers(periodType);
        return ResponseEntity.ok(metersData);
    }

}