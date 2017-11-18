package com.example.algamoneyapi.service.exception;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.algamoneyapi.exceptionhandler.AlgamoneyExceptionHandler.Erro;

@ControllerAdvice
public class PessoaInexistenteOuInativaException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Autowired
	private MessageSource messageSource;
	
	
	
	@ExceptionHandler( { PessoaInexistenteOuInativaException.class } )
	public ResponseEntity<?> handlerPessoaInexistenteOuInativaException( PessoaInexistenteOuInativaException ex) {
		
		String 	mensagemUsuario				=	messageSource.getMessage( "pessoa.inexistente-ou-inativa", null, LocaleContextHolder.getLocale() );
		String	mensagemDesenvolverdor		=	ex.toString();
		
		List<Erro>	erros	=	Arrays.asList( new Erro( mensagemUsuario, mensagemDesenvolverdor ) );
		
		return ResponseEntity.badRequest().body(erros);
		
	}
}
