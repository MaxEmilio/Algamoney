package com.example.algamoneyapi.resource;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.algamoneyapi.event.RecursoCriadoEvent;
import com.example.algamoneyapi.model.Pessoa;
import com.example.algamoneyapi.repository.PessoaRepository;
import com.example.algamoneyapi.service.PessoaService;

@RestController
@RequestMapping("/pessoas")
public class PessoaResource {

	@Autowired
	private PessoaRepository 			pessoaRepository;
	
	@Autowired
	private ApplicationEventPublisher 	publisher;

	@Autowired
	private PessoaService 				pessoaService;
	
	
	//---------------------------------------------------------------------------------------------------------------------------------------  pesquisa 
	@GetMapping
	@PreAuthorize("hasAuthority( 'ROLE_PESQUISAR_PESSOA' ) and #oauth2.hasScope('read') ")
	public List<Pessoa> ConsultarPessoas() {		

		List<Pessoa> pessoas = pessoaRepository.findAll();
		
		return pessoas;
		
	}
	
	@GetMapping( params= "porNome")
	@PreAuthorize("hasAuthority( 'ROLE_PESQUISAR_PESSOA' ) and #oauth2.hasScope('read') ")
	public ResponseEntity<?> ConsultarPessoasPorNome(@RequestParam("porNome") String valor) {		

		List<Pessoa> pessoas = pessoaRepository.findByNomeContainingIgnoreCase(valor);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(pessoas) ;
		
	}
	
	//----------------------------------------------------
	
	@PreAuthorize("hasAuthority( 'ROLE_PESQUISAR_PESSOA' ) and #oauth2.hasScope('read') ")
	@PostMapping//																														(( Salvar pessoa  ))
	public ResponseEntity<Pessoa>	SalvarPessoa(@RequestBody @Valid Pessoa pessoa, HttpServletResponse response ) {
		
		Pessoa pessoaSalvar		=	pessoaRepository.save(pessoa);
		
		publisher.publishEvent( new RecursoCriadoEvent(this, response, pessoa.getCodigo()) );
		
		return ResponseEntity.status( HttpStatus.CREATED ).body(pessoaSalvar) ;
		
	}
	
	
	@PreAuthorize("hasAuthority( 'ROLE_PESQUISAR_PESSOA' ) and #oauth2.hasScope('read') ")
	@GetMapping("/{codigo}")//																												(( Pesquisa por codigo ))
	public ResponseEntity<?> LocalizarCodigo( @PathVariable Long codigo ) {
		
		return pessoaRepository.exists(codigo)? ResponseEntity.ok(pessoaRepository.findOne(codigo)) : ResponseEntity.notFound().build()  ;
		
	}
	
	@PreAuthorize("hasAuthority( 'ROLE_REMOVER_PESSOA' ) and #oauth2.hasScope('write') ")
	@DeleteMapping("/{codigo}")//																											(( apagando uma pessoa ))
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void apagarPessoa(@PathVariable Long codigo) {
		
		pessoaRepository.delete(codigo);
		
	}
	
	@PreAuthorize("hasAuthority( 'ROLE_CADASTRAR_PESSOA' ) and #oauth2.hasScope('write') ")
	@PutMapping("/{codigo}")//																												(( 	atualizando Pessoa  ))
	public ResponseEntity<Pessoa> atualizaPessoa(@PathVariable Long codigo, @Valid @RequestBody Pessoa PessoaEnviada ) {
		
		return ResponseEntity.ok().body(pessoaService.atualizaPessoa(codigo, PessoaEnviada)) ;
		
	}
	
	
	@PreAuthorize("hasAuthority( 'ROLE_CADASTRAR_PESSOA' ) and #oauth2.hasScope('write') ")
	@PutMapping("/{codigo}/ativo")//																													
	public ResponseEntity<?> atualizaPessoaPropriedadeAtivo(@PathVariable Long codigo, @RequestBody Boolean ativo) {

		return  pessoaService.atualizarPropriedadeAtivo(codigo, ativo); 
		
	}

}
