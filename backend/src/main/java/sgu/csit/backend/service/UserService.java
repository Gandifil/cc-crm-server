package sgu.csit.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sgu.csit.backend.auth.JwtTokenUtil;
import sgu.csit.backend.dto.MetersDataDTO;
import sgu.csit.backend.dto.UserDTO;
import sgu.csit.backend.exception.AuthenticationException;
import sgu.csit.backend.exception.RegistrationException;
import sgu.csit.backend.model.*;
import sgu.csit.backend.repository.AuthorityRepository;
import sgu.csit.backend.repository.UserRepository;
import sgu.csit.backend.security.JwtUser;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Value("${jwt.header}")
    private String tokenHeader;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsService jwtUserDetailsService;

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(
            AuthenticationManager authenticationManager,
            JwtTokenUtil jwtTokenUtil,
            JwtUserDetailsService jwtUserDetailsService,
            UserRepository userRepository,
            AuthorityRepository authorityRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // auth
    public String login(String username, String password) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);
            return jwtTokenUtil.generateToken(userDetails);
        } catch (DisabledException e) {
            throw new AuthenticationException("User is disabled!", e);
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Bad credentials!", e);
        }
    }

    public boolean register(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RegistrationException("Account with such username already exists!");
        }

        if (user.getApartment() == 0)
            if (!userRepository.existsByApartment(0))
                user.setAuthorities(Collections.singletonList(authorityRepository.findByName(AuthorityType.ROLE_ADMIN)));
            else
                throw new RegistrationException("Admin account already exists!");
        else
            user.setAuthorities(Collections.singletonList(authorityRepository.findByName(AuthorityType.ROLE_USER)));

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);

        userRepository.save(user);
        return true;
    }

    public String refresh(String authToken) {
        final String token = authToken.substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser)jwtUserDetailsService.loadUserByUsername(username);

        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
            return jwtTokenUtil.refreshToken(token);
        }
        return null;
    }

    // retrieval
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    private Calendar getActualMinOf(PeriodType periodType) {
        Calendar calendar = Calendar.getInstance();
        switch (periodType) {
            case CURRENT_MONTH:
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
                break;
            case CURRENT_YEAR:
                calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
                break;
            case ALL:
                calendar.set(Calendar.YEAR, 1900);
                break;
            default:
                return null;
        }
        return calendar;
    }
    public Map<Integer, List<MetersDataDTO>> getAllApartments(PeriodType periodType) {
        Calendar calendar = getActualMinOf(periodType);

        Map<Integer, List<MetersDataDTO>> apartments = new HashMap<>();
        for (User user : userRepository.findAll()) {
            Integer apartment = user.getApartment();
            if (apartment == 0)
                continue;
            List<MetersData> metersData = user.getMetersData()
                                                .stream()
                                                .filter(md -> md.getDate().after(calendar.getTime()))
                                                .collect(Collectors.toList());
            if (apartments.containsKey(apartment))
                apartments.get(apartment).addAll(MetersDataDTO.toDTO(metersData));
            else
                apartments.put(apartment, MetersDataDTO.toDTO(metersData));
        }

        return apartments;
    }

    private boolean notActual(List<MetersDataDTO> metersData, Date date) {
        for (MetersDataDTO md : metersData)
            if (md.getDate().after(date))
                return false;
        return true;
    }
    public Map<Integer, List<UserDTO>> getBadApartments(PeriodType periodType) {
        Calendar calendar = getActualMinOf(periodType);

        Map<Integer, List<UserDTO>> badApartments = new HashMap<>();
        Map<Integer, List<MetersDataDTO>> apartments = getAllApartments(PeriodType.ALL);
        for (Integer apartment : apartments.keySet())
            if (notActual(apartments.get(apartment), calendar.getTime()) && apartment != 0) {
                List<User> badUsers = userRepository.findAllByApartment(apartment);
                badApartments.put(apartment, UserDTO.toDTO(badUsers));
            }

        return badApartments;
    }
}
