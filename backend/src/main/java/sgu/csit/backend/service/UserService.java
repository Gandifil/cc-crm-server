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
            throw new RegistrationException("Account with that username already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEmail(user.getUsername());
        user.setEnabled(true);
        user.setAuthorities(Collections.singletonList(authorityRepository.findByName(AuthorityType.ROLE_USER)));
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

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    private boolean isResponsibleUser(User user, Date date) {
        for (MetersData metersData : user.getMetersData()) {
            if (metersData.getDate().after(date)) {
                return true;
            }
        }
        return false;
    }

    public Iterable<User> getUsers(PeriodType periodType) {
        List<User> users;
        switch (periodType) {
            case CURRENT_MONTH:
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum((Calendar.DAY_OF_MONTH)));
                // TODO: заменить findAll на выборку jpa
                users = new ArrayList<>(userRepository.findAll())
                        .stream()
                        .filter(u -> !isResponsibleUser(u, calendar.getTime()))
                        .collect(Collectors.toList());
                break;
            case CURRENT_YEAR:
                calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMinimum((Calendar.DAY_OF_MONTH)));
                // TODO: заменить findAll на выборку jpa
                users = new ArrayList<>(userRepository.findAll())
                        .stream()
                        .filter(u -> !isResponsibleUser(u, calendar.getTime()))
                        .collect(Collectors.toList());
                break;
            case ALL:
                users = userRepository.findAll();
                break;
            default:
                return null;
        }
        return users;
    }
}
