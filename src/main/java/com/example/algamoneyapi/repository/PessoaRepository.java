package com.example.algamoneyapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.algamoneyapi.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

	List<Pessoa> findByNomeContainingIgnoreCase   (String value);	//						pesquisa por palavras na Pessoa
	
}
