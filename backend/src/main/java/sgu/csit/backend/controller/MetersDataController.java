package sgu.csit.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sgu.csit.backend.auth.JwtTokenUtil;
import sgu.csit.backend.model.MetersData;
import sgu.csit.backend.model.PeriodType;
import sgu.csit.backend.security.JwtUser;
import sgu.csit.backend.service.MetersDataService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestController
public class MetersDataController {
    @Value("${jwt.header}")
    private String tokenHeader;

    private final JwtTokenUtil jwtTokenUtil;

    private final MetersDataService metersDataService;

    private final UserDetailsService userDetailsService;

    @Autowired
    public MetersDataController(JwtTokenUtil jwtTokenUtil,
                                MetersDataService metersDataService,
                                @Qualifier("jwtUserDetailsService") UserDetailsService userDetailsService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.metersDataService = metersDataService;
        this.userDetailsService = userDetailsService;
    }

    @RequestMapping(value = "/meters/send", method = RequestMethod.POST)
    public ResponseEntity sendMetersData(@RequestBody MetersData metersData,
                                         HttpServletRequest httpServletRequest) {
        metersData.setDate(LocalDateTime.now());
        metersData.setUserId(getCurrentUser(httpServletRequest).getId());
        metersDataService.addMeterdsData(metersData);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/meters", method = RequestMethod.GET)
    public ResponseEntity getAllMetersData() {
        Iterable<MetersData> metersData = metersDataService.getAll(PeriodType.ALL);
        return new ResponseEntity(metersData, HttpStatus.OK);
    }

    private JwtUser getCurrentUser(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader(tokenHeader).substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        return (JwtUser)userDetailsService.loadUserByUsername(username);
    }
}
