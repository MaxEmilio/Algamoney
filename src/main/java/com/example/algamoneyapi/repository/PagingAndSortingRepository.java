package com.example.algamoneyapi.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.example.algamoneyapi.model.Lancamento;


public interface PagingAndSortingRepository extends CrudRepository<Lancamento, Long> {

	Page<Lancamento> findAll(Pageable pageable);
	
	Page<Lancamento> findByPessoaNomeContainingIgnoreCase   (String value, Pageable pageable);	//						pesquisa por palavras na Pessoa
	
	Page<Lancamento> findByDescricaoContainingIgnoreCase   	(String value, Pageable pageable);	//						pesquisa por palavras na descrição
	
	Page<Lancamento> findByDataPagamentoAfter   		(LocalDate data, Pageable pageable);	//						pesquisa por datas de pagamento depois do value

	Page<Lancamento> findByDataPagamentoBefore   		(LocalDate data, Pageable pageable);   //						pesquisa por datas de pagamentos antes do value
	
	Page<Lancamento> findByDataVencimentoAfter   		(LocalDate data, Pageable pageable);	//						pesquisa por datas de Vencimento depois do value

	Page<Lancamento> findByDataVencimentoBefore   		(LocalDate data, Pageable pageable);   //						pesquisa por datas de Vencimento antes do value
	
	Page<Lancamento> findByDataVencimentoBetween   		(LocalDate data1, LocalDate data2, Pageable pageable);   //		pesquisa entre datas do Vencimento 		
	
	Page<Lancamento> findByDataPagamentoBetween   		(LocalDate data1, LocalDate data2, Pageable pageable);   //		pesquisa entre datas do pagamento
}
