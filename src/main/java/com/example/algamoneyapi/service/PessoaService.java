package com.example.algamoneyapi.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.algamoneyapi.model.Pessoa;
import com.example.algamoneyapi.repository.PessoaRepository;

@Service
public class PessoaService {

	@Autowired
	private 	PessoaRepository		pessoaRepository;
	
	
	public Pessoa atualizaPessoa(Long codigo, Pessoa PessoaEnviada) {
		
		Pessoa pessoa = buscaPessoaCodigo( codigo);
		
		BeanUtils.copyProperties(PessoaEnviada, pessoa, "codigo");
		
		pessoaRepository.save(pessoa);
		
		return pessoa;
		
	}

	public ResponseEntity<?> atualizarPropriedadeAtivo(Long codigo, boolean ativo) {
		
		Pessoa pessoaSalva = buscaPessoaCodigo( codigo );
		
		if (pessoaSalva.getAtivo() != ativo) {
			
			pessoaSalva.setAtivo(ativo);
			
			pessoaRepository.save(pessoaSalva);
			
		}
		
		return ResponseEntity.status( HttpStatus.CREATED ).body(pessoaSalva) ;
		
	}
	
	
	public Pessoa buscaPessoaCodigo(Long codigo) {
		
		Pessoa 		pessoaSalva		=	 pessoaRepository.findOne(codigo);

		if (pessoaSalva == null) {
			
			throw new EmptyResultDataAccessException(1);
		}
		
		return pessoaSalva;
	}
	
	
	
}
