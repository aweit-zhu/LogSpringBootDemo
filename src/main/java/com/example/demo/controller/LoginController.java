package com.example.demo.controller;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.annotation.LogAction;
import com.example.demo.dto.JwtAuthenticationResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.service.JwtTokenService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class LoginController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtTokenService tokenService;

	@GetMapping(value = "/login")
	@LogAction(name = "請使用POST請求登入")
	public String login() {
		return "請使用POST請求登入";
	}

	@PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
	@LogAction(name = "登入")
    public ResponseEntity<?> authenticate(@RequestBody LoginRequest loginRequest) {
        try {
        	SecurityContextHolder.clearContext();
        	UsernamePasswordAuthenticationToken user =  new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());	
        	Authentication authentication = authenticationManager.authenticate(user);
        	SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = tokenService.generateToken(loginRequest.getUsername());
            return ResponseEntity.ok(token);
        } catch (AuthenticationException e) {
        	e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

	@GetMapping(value = "/logout")
	@LogAction(name = "登出")
	public String logout(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");
        if (!Strings.isEmpty(authHeader)){
            String accessToken = authHeader.replace("Bearer ", "");
            tokenService.addTokenToBlacklist(accessToken);
            SecurityContextHolder.clearContext();
        }
		return "使用者登出";
	}
}
