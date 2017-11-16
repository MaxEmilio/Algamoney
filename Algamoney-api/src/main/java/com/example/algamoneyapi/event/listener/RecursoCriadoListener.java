package com.example.algamoneyapi.event.listener;

import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.algamoneyapi.event.RecursoCriadoEvent;

@Component
public class RecursoCriadoListener implements ApplicationListener<RecursoCriadoEvent> {

	@Override
	public void onApplicationEvent(RecursoCriadoEvent recursoCriadoEvent) {
		
		HttpServletResponse response = recursoCriadoEvent.getResponse();

		Long 				codigo		=	recursoCriadoEvent.getCodigo();	
		
		adicionarHeaderLocation(response, codigo ); 
	
	}
	
	
	private void adicionarHeaderLocation( HttpServletResponse reponse, Long codigo) {
		
		//Envio da location
				URI		uri		=	ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}")
						.buildAndExpand(codigo).toUri();
				
				reponse.setHeader("Location", uri.toASCIIString());
		
	}
	

}
