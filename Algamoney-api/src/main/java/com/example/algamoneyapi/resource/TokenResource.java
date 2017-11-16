package com.example.algamoneyapi.resource;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.AsyncContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.algamoneyapi.config.property.AlgamoneyApiProperty;
import com.example.algamoneyapi.config.property.AlgamoneyApiProperty.Seguranca;
import com.example.algamoneyapi.model.Pessoa;
import com.example.algamoneyapi.repository.LancamentoRepository;
import com.example.algamoneyapi.repository.PessoaRepository;

@RestController
@RequestMapping("/tokens")
public class TokenResource {
	
	
	@Autowired
	private PessoaRepository 			pessoaRepository;
	
	@Autowired
	private AlgamoneyApiProperty 		algamoneyApiProperty;
	
/*
	@DeleteMapping("/revoke")
	public ResponseEntity<?> revoke(HttpServletRequest req, HttpServletResponse resp )  {
	
		List<Pessoa> pessoas = pessoaRepository.findAll();
		
//		resp.setHeader("valor de teste:", req.getLocale() );
		
		
		return  ResponseEntity.status(HttpStatus.OK).body(pessoas);

	}
*/
	

	@DeleteMapping("/revoke")
	public void revoke(HttpServletRequest req, HttpServletResponse resp )  {
	
			Cookie cookie		=	new 	Cookie("refreshToken", null);
			
			cookie.setHttpOnly(true);
			cookie.setSecure(algamoneyApiProperty.getSeguranca().isEnableHttps()); 			
			cookie.setPath(req.getContextPath() + "/oauth/token" );
			cookie.setMaxAge(0);
				
			resp.addCookie(cookie);
			resp.setStatus(HttpStatus.NO_CONTENT.value());

					
	}

	
	
}
