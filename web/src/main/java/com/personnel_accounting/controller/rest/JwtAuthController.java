package com.personnel_accounting.controller.rest;

import com.personnel_accounting.configuration.security.jwt.JwtTokenUtil;
import com.personnel_accounting.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class JwtAuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService jwtInMemoryUserDetailsService;

    @Autowired
    private MessageSource messageSource;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody User user)
            throws Exception {

        authenticate(user.getUsername(), user.getPassword());
        final UserDetails userDetails = jwtInMemoryUserDetailsService
                .loadUserByUsername(user.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(token);
    }

    private void authenticate(String username, String password){
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (DisabledException e) {
            throw new DisabledException(
                    messageSource.getMessage("login.user.disabled", null, LocaleContextHolder.getLocale()), e);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(
                    messageSource.getMessage("login.invalid_credentials", null, LocaleContextHolder.getLocale()), e);
        }
    }
}