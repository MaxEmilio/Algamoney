package com.example.algamoneyapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.example.algamoneyapi.model.Pessoa;

public interface PessoaRepositoryPage extends CrudRepository<Pessoa, Long> {

	Page<Pessoa> findAll(Pageable pageable);
	
	Page<Pessoa> findByNomeContainingIgnoreCase   (String value, Pageable pageable);	//						pesquisa por palavras na Pessoa
	
}
