package com.example.algamoneyapi.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.algamoneyapi.model.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

	List<Lancamento> findByPessoaNomeContainingIgnoreCase   (String value);	//						pesquisa por palavras na Pessoa
	
	List<Lancamento> findByDescricaoContainingIgnoreCase   	(String value);	//						pesquisa por palavras na descrição
	
	List<Lancamento> findByDataPagamentoAfter   		(LocalDate data);	//						pesquisa por datas de pagamento depois do value

	List<Lancamento> findByDataPagamentoBefore   		(LocalDate data);   //						pesquisa por datas de pagamentos antes do value
	
	List<Lancamento> findByDataVencimentoAfter   		(LocalDate data);	//						pesquisa por datas de Vencimento depois do value

	List<Lancamento> findByDataVencimentoBefore   		(LocalDate data);   //						pesquisa por datas de Vencimento antes do value
	
	List<Lancamento> findByDataVencimentoBetween   		(LocalDate data1, LocalDate data2);   //	pesquisa entre datas do Vencimento 		
	
	List<Lancamento> findByDataPagamentoBetween   		(LocalDate data1, LocalDate data2);   //	pesquisa entre datas do pagamento
}
