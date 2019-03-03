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
import sgu.csit.backend.model.Authority;
import sgu.csit.backend.model.AuthorityName;
import sgu.csit.backend.model.User;
import sgu.csit.backend.repository.UserRepository;
import sgu.csit.backend.security.JwtUser;

import java.util.*;

@Service
public class UserService {
    @Value("${jwt.header}")
    private String tokenHeader;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenUtil jwtTokenUtil;

    private final JwtUserDetailsService jwtUserDetailsService;

    private final UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(
            AuthenticationManager authenticationManager,
            JwtTokenUtil jwtTokenUtil,
            JwtUserDetailsService jwtUserDetailsService,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.userRepository = userRepository;
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

    public boolean register(User newUser) {
        if (!userRepository.existsByUsername(newUser.getUsername())) {
            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
            newUser.setEmail(newUser.getUsername());
            newUser.setEnabled(true);
            // TODO: add authority to new user
            userRepository.save(newUser);
            return true;
        }
        throw new RegistrationException("Account already exists");
    }

    public String refresh(String authToken) {
        final String token = authToken.substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) jwtUserDetailsService.loadUserByUsername(username);

        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
            return jwtTokenUtil.refreshToken(token);
        }
        return null;
    }
}
