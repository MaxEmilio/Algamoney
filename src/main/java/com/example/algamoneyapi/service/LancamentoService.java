package com.example.algamoneyapi.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.algamoneyapi.model.Lancamento;
import com.example.algamoneyapi.model.Pessoa;
import com.example.algamoneyapi.repository.LancamentoRepository;
import com.example.algamoneyapi.repository.PessoaRepository;
import com.example.algamoneyapi.service.exception.PessoaInexistenteOuInativaException;

@Service
public class LancamentoService {

	@Autowired
	private PessoaRepository 			pessoaRepository;
	
	@Autowired
	private LancamentoRepository		lancamentoRepository;
	
	
	public Lancamento salvaLancamento(Lancamento lancamento) {
		
		Pessoa pessoa = null;
		
		if (lancamento.getPessoa().getCodigo() != null) {
			pessoa = pessoaRepository.findOne(lancamento.getPessoa().getCodigo());
		}
		
		if ( pessoa.isInativo() || pessoa == null ) {
			
			throw new PessoaInexistenteOuInativaException();
			
		}
		
		return lancamentoRepository.save(lancamento);
		
	}

	/*  
	 * Este método realiza algumas regras de negócio no caso de atualização de um lançamento
	 * 
	 * dentre as regras estão:
	 * 
	 *  1 - verifica se o lançamento existe e retorna 
	 *
	 *  2 - verifica se o lançamento está ativo
	 *  
	 *  O método retorna uma ResponseEntity conténdo no caso de sucesso o lançamento salvo ou no caso de falha o motivo da falha.
	 * 
	 */
	public ResponseEntity<?> atualiza(Lancamento lancamentoEnviado) {
		// A validação básica dos campos de lançamento e da pessoa foi relizada no próprio método com o @Valid
		
		
		if(lancamentoRepository.exists(lancamentoEnviado.getCodigo())    ) { // 															verificar se o lançamento existe

			if (!lancamentoEnviado.getPessoa().isInativo()) {			// 																	verificar se o lançamento é ativo

				Lancamento lancamentoASalvar = lancamentoRepository.findOne(lancamentoEnviado.getCodigo()); // 								Localiza o lançamento com base no codigo
				
				BeanUtils.copyProperties(lancamentoEnviado, lancamentoASalvar, "codigo");				// 									realizar a atualização do lançamento
				
				lancamentoRepository.save(lancamentoASalvar);//																				salva e enviar para a resposta.
				
				return ResponseEntity.ok().body(lancamentoRepository.findOne( lancamentoEnviado.getCodigo() ));
			}

				return ResponseEntity.badRequest().body("Falha ao tentar atualizar o lançamento: Pessoa inativa (somente pessoas ativa podem ser atualizadas");

		}else {
			
			return ResponseEntity.notFound().build();
		}
			
	}
	
}
