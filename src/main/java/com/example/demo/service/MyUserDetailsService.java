package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

	private List<UserDetails> allUsers() {
		List<UserDetails> users = new ArrayList<>();
		users.add(User.withUsername("user").password(new BCryptPasswordEncoder().encode("password")) // 確保加密
				.roles("USER").build());
		users.add(User.withUsername("admin").password(new BCryptPasswordEncoder().encode("password")).roles("ADMIN")
				.build());
		return users;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDetails userdetails = allUsers().stream()
				.filter(user -> user.getUsername().equalsIgnoreCase(username))
				.findFirst().orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

		return userdetails;
	}

}
