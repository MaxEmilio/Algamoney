package com.example.algamoneyapi.resource;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;

import com.example.algamoneyapi.event.RecursoCriadoEvent;
import com.example.algamoneyapi.model.Categoria;
import com.example.algamoneyapi.repository.CategoriaRepository;

@RestController
@RequestMapping("/categorias")
public class CategoriaResource {

	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;

	//																										------					Pesquisa
	@GetMapping
	public List<Categoria> listar(){

		return categoriaRepository.findAll();
	}
	
	
	//																										---------				Salvar Categoria
	@PostMapping
	@PreAuthorize("hasAuthority( 'ROLE_CADASTRAR_CATEGORIA' ) and #oauth2.hasScope('write') ")
	public ResponseEntity<Categoria> salvar(@Valid @RequestBody Categoria categoria, HttpServletResponse response ) {
		
		Categoria	categoriaSalva	=	categoriaRepository.save(categoria);
		
		publisher.publishEvent(new RecursoCriadoEvent(this, response, categoriaSalva.getCodigo() ));
		
		// HttpStatus será criado no created direto
		return ResponseEntity.status(HttpStatus.CREATED).body(categoriaSalva);
		
	}
	
	@PreAuthorize("hasAuthority( 'ROLE_PESQUISAR_CATEGORIA' ) and #oauth2.hasScope('read') ")
	@GetMapping("/{codigo}")
	public ResponseEntity<?> buscarPeloCodigo(@PathVariable  Long codigo) {
	
		return categoriaRepository.exists(codigo)? ResponseEntity.ok(categoriaRepository.findOne(codigo)) : ResponseEntity.noContent().build();
	}
	
	
	
}
