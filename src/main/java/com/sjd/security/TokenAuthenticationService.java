package com.sjd.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security
            .authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.ArrayList;
import java.util.Collections;

class TokenAuthenticationService {
    
  static final long EXPIRATIONTIME = 864_000_000; // 10 days
  static final String SECRET = "ThisIsASecret";
  static final String TOKEN_PREFIX = "Bearer";
  static final String HEADER_STRING = "Authorization";
  static final String TOKEN_HEADER_ROLES = "ROLES";

  private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationService.class);


  static void addAuthentication(HttpServletResponse res, Authentication auth) {

    logger.debug("Username : " + auth.getName());
    logger.debug("Authorities : " + auth.getAuthorities());

    // Add the authorities to the claims
    Map<String, Object> userRoles = new HashMap<String, Object> ();

    List<String> roles = new ArrayList<String> ();
    if (auth.getAuthorities() != null)
    {
        // Add the roles
        for (GrantedAuthority ga : auth.getAuthorities())
        {
            // Add the role
            roles.add(ga.getAuthority());
        }

        userRoles.putIfAbsent(TOKEN_HEADER_ROLES, roles);
    }

    // SJD - Authorities don't seem to be being passed across, and I can't add them into the Claims
    // as that's what the io framework seems to add under the covers so we instead add them as header
    // params and deserialize them when we unpack the token...

    String JWT = Jwts.builder()
        .setSubject(auth.getName())
        .setHeaderParam("ROLES", roles)
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
        .signWith(SignatureAlgorithm.HS512, SECRET)
        .compact();

    logger.debug("returning the Jwt bearer token in the header");
    res.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + JWT);
  }

  static Authentication getAuthentication(HttpServletRequest request) {
    String token = request.getHeader(HEADER_STRING);

    logger.debug("Token : " + token);

    if (token != null) {

      logger.debug("validating the Jwt token");

      // parse the token.
      Jws<Claims> parsed = Jwts.parser()
          .setSigningKey(SECRET)
          .parseClaimsJws(token.replace(TOKEN_PREFIX, ""));

      Claims claims = parsed.getBody();

      logger.debug("claims : " + claims);

      JwsHeader header = parsed.getHeader ();

      List<String> userRoles = (List<String>) header.getOrDefault (TOKEN_HEADER_ROLES, Collections.<String>emptyList ());
      logger.debug("userRoles = " + userRoles);

      String user = claims.getSubject();

      // SJD - Get the specific header we added and add in the granted authorities that were serialized
      // when we created the token above

      List<GrantedAuthority> grants = new ArrayList<GrantedAuthority> ();
      if (userRoles != null)
      {
        // Add them
        for (String r : userRoles)
            grants.add (new SimpleGrantedAuthority(r));
      }

      return user != null ?
          new UsernamePasswordAuthenticationToken (user, null, grants) : //emptyList()) :
          null;
    }
    return null;
  }
}