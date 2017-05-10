package com.sjd.security;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

    private static final Logger logger = LoggerFactory.getLogger(JWTLoginFilter.class);

  public JWTLoginFilter(String url, AuthenticationManager authManager) {
    super(new AntPathRequestMatcher(url));
    setAuthenticationManager(authManager);
  }

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest req, HttpServletResponse res)
      throws AuthenticationException, IOException, ServletException {

    AccountCredentials creds = new ObjectMapper().readValue(req.getInputStream(), AccountCredentials.class);

    //List<GrantedAuthority> grants = new ArrayList<GrantedAuthority> ();
    //grants.add(new SimpleGrantedAuthority ("ROLE_ADMIN"));
    //grants.add(new SimpleGrantedAuthority ("ROLE_USER"));

    return getAuthenticationManager().authenticate(
        new UsernamePasswordAuthenticationToken(
            creds.getUsername(),
            creds.getPassword(),
            Collections.emptyList()
        )
    );
  }

  @Override
  protected void successfulAuthentication(
      HttpServletRequest req,
      HttpServletResponse res, FilterChain chain,
      Authentication auth) throws IOException, ServletException {

    //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    //String currentPrincipalName = authentication.getName();
    if (auth != null)
        logger.debug("AUTH DETAILS : " + auth.toString());

    TokenAuthenticationService.addAuthentication(res, auth); // auth.getName());

    logger.debug("successful authentication!! - " + auth.getAuthorities());
  }
}