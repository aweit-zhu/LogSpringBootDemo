package com.example.demo.dto;

public class JwtAuthenticationResponse {
	private String token;

	public JwtAuthenticationResponse(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "JwtAuthenticationResponse [token=" + token + "]";
	}
}
