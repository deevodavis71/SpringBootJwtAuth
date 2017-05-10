package com.sjd;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping ("/api")
public class UserController
{
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    @RequestMapping ("/getusers")
    public ResponseEntity<List<User>> getUsers ()
    {
        logger.debug("Called getUsers");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        logger.debug(authentication.toString());

        logger.debug("dumped roles... " + currentPrincipalName);

        User u = new User ();
        u.setFirstname("Steve");
        u.setSurname("Davis");

        User v = new User ();
        v.setFirstname("Robbie");
        v.setSurname("Whiting");

        List<User> ret = new ArrayList<User> ();
        ret.add (u);
        ret.add (v);

        return new ResponseEntity<List<User>> (ret, HttpStatus.OK);
    }    

    @RequestMapping ("/getsupersecret")
    @Secured ({ "ROLE_SPECIALAGENT" })
    public ResponseEntity<User> superSecret ()
    {
        User u = new User ();
        u.setFirstname("James");
        u.setSurname("Bond");
       
        return new ResponseEntity<User> (u, HttpStatus.OK);
    }
}