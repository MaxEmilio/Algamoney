package com.example.algamoneyapi.security.util;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeradorSenha {
	
	@Bean
	public static	void		main (String[] args) {
		
		BCryptPasswordEncoder	encoder	=	new	BCryptPasswordEncoder();
		
		System.out.println( encoder.encode("admin") );
		
	}
	
}
