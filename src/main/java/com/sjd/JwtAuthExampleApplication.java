package com.sjd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableAutoConfiguration
@RestController
public class JwtAuthExampleApplication {

    @RequestMapping ("/") 
    String helloWorld ()
    {
        return "Hello World";
    }

	public static void main(String[] args) {
		SpringApplication.run(JwtAuthExampleApplication.class, args);
	}
}
