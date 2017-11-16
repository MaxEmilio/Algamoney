package com.example.algamoneyapi.resource;

import java.time.LocalDate;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.algamoneyapi.event.RecursoCriadoEvent;
import com.example.algamoneyapi.model.Lancamento;
import com.example.algamoneyapi.repository.LancamentoRepository;
import com.example.algamoneyapi.service.LancamentoService;



@RestController
@RequestMapping("/lancamentos")
public class LancamentoResource {

	@Autowired
	private LancamentoRepository 			lancamentoRepository;
	
	
	@Autowired
	private ApplicationEventPublisher 		publiser;
	
	
	@Autowired
	private LancamentoService 				lancamentoService;

	
	//																																	Pesquisa Geral Simples
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<Lancamento> buscaLancamentos() {
		
		List<Lancamento> lancamentos = lancamentoRepository.findAll();
		
		return lancamentos;
	}
	
	
	
	
	
	
	
	
	// 																																	Salva Lançamento
	@PostMapping
	@PreAuthorize("hasAuthority( 'ROLE_CADASTRAR_CATEGORIA' ) and #oauth2.hasScope('write') ")
	public ResponseEntity<Lancamento> salvarLancamento(@Valid  @RequestBody Lancamento lancamento , HttpServletResponse httpResposta  ) {
		
		
		// regra sobre usuário ativo ou não.
		Lancamento lancamentoSalvo = lancamentoService.salvaLancamento(lancamento);	

		publiser.publishEvent(new RecursoCriadoEvent(this, httpResposta, lancamentoSalvo.getCodigo() ) );
		
		return ResponseEntity.status(HttpStatus.CREATED).body(lancamento);
		
	}
	
	////////////////////////////////////////////////////////////////////////////				((	PESQUISAS ))
	
	

	@RequestMapping( params = "pesquisaNome", method = RequestMethod.GET )			//																				Pesquisa por Nome Pessoa
	@PreAuthorize("hasAuthority( 'ROLE_PESQUISAR_LANCAMENTO' ) ")
	public List<Lancamento> pesquisaLancamentoPessoaNome(@RequestParam("valor") String valor) {

		return lancamentoRepository.findByPessoaNomeContainingIgnoreCase(valor);
	}

	// revisada
	//																																 	Pesquisa lançamento por código
	@GetMapping("/{codigo}")
	@PreAuthorize("hasAuthority( 'ROLE_PESQUISAR_LANCAMENTO' ) and #oauth2.hasScope('read') ")
	public ResponseEntity<?>  buscaLancamentos(@PathVariable long codigo) {

		return lancamentoRepository.exists(codigo)? ResponseEntity.ok(lancamentoRepository.findOne(codigo)) : ResponseEntity.noContent().build();
	}
	
	@RequestMapping(params = "pesquisaDescricao", method = RequestMethod.GET)			//																				Pesquisa por Descricao -> lancamento
	@PreAuthorize("hasAuthority( 'ROLE_PESQUISAR_LANCAMENTO' ) ")
	public List<Lancamento> pesquisaLancamentoDescricao(@RequestParam("valor") String valor) {

		return lancamentoRepository.findByDescricaoContainingIgnoreCase(valor);
	}
	
	
	
	
	/*
	 *metodos de teste 
	 */
	@GetMapping( params = "pessoa")
	public String getitem(@RequestParam("data") String itemid){

	   
	    return itemid;
	}
	
	@GetMapping( params = "lancamento")
	public String getitem2(@RequestParam("data") String itemid){

	   
	    return itemid;
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	
	
	
	
	//																									( Pesquisa a parti de uma data )
	
	@RequestMapping("pesquisaPagamento/dataDepoisDe={value1}" )//												pesquisa por datas de pagamento depois do value		
	@PreAuthorize("hasAuthority( 'ROLE_PESQUISAR_LANCAMENTO' ) ")
    public List<Lancamento> pesquisaLancamentoPagamentoDepoisDaData(@PathVariable String value1) {
		
		LocalDate 		data			=		LocalDate.parse(value1);
		
		List<Lancamento> 	livroLista		=		lancamentoRepository.findByDataPagamentoAfter(data);
		
        return livroLista ;
    }
	
	@RequestMapping("pesquisaPagamento/dataAntesDe={value1}" )//												pesquisa por datas de pagamentos antes do value
	@PreAuthorize("hasAuthority( 'ROLE_PESQUISAR_LANCAMENTO' ) ")
    public List<Lancamento> pesquisaLancamentoPagamentoAntesDaData(@PathVariable String value1) {
		
		LocalDate 		data			=		LocalDate.parse(value1);
		
		List<Lancamento> 	livroLista		=		lancamentoRepository.findByDataPagamentoBefore(data);
		
        return livroLista ;
    }
	
	
	
	
	
	@RequestMapping("pesquisaVencimento/dataAntesDe={value1}" )//												pesquisa por datas de Vencimento antes do value
	@PreAuthorize("hasAuthority( 'ROLE_PESQUISAR_LANCAMENTO' ) ")
    public List<Lancamento> pesquisaLancamentoVencimentoAntesDaData(@PathVariable String value1) {
		
		LocalDate 		data			=		LocalDate.parse(value1);
		
		List<Lancamento> 	livroLista		=		lancamentoRepository.findByDataVencimentoBefore(data);
		
        return livroLista ;
    }

	@RequestMapping("pesquisaVencimento/dataDepoisDe={value1}" )//												pesquisa por datas de Vencimento depois do value
	@PreAuthorize("hasAuthority( 'ROLE_PESQUISAR_LANCAMENTO' ) ")
    public List<Lancamento> pesquisaLancamentoVencimentoDepoisDaData(@PathVariable String value1) {
		
		LocalDate 		data			=		LocalDate.parse(value1);
		
		List<Lancamento> 	livroLista		=		lancamentoRepository.findByDataVencimentoAfter(data);
		
        return livroLista ;
    }

	
	@RequestMapping("pesquisaLancamentoPagamento/dataEntre={value1}/{value2}" )//													pesquisa Data entre dois valores
	@PreAuthorize("hasAuthority( 'ROLE_PESQUISAR_LANCAMENTO' ) ")
    public List<Lancamento> pesquisaLancamentoPagamentoEntreDuasDatas(@PathVariable String value1, @PathVariable String value2) {
		
		LocalDate data1	=	LocalDate.parse(value1);
		
		LocalDate data2	=	LocalDate.parse(value2);
		
		List<Lancamento> livroLista		=		lancamentoRepository.findByDataPagamentoBetween(data1, data2);
		
        return livroLista ;
    }
	
	
	@RequestMapping("pesquisaLancamentoVencimento/dataEntre={value1}/{value2}" )//													pesquisa entre datas do Vencimento
	@PreAuthorize("hasAuthority( 'ROLE_PESQUISAR_LANCAMENTO' ) ")
    public List<Lancamento> pesquisaLancamentoPVencimentoEntreDuasDatas(@PathVariable String value1, @PathVariable String value2) {
		
		LocalDate data1	=	LocalDate.parse(value1);
		
		LocalDate data2	=	LocalDate.parse(value2);
		
		List<Lancamento> livroLista		=		lancamentoRepository.findByDataVencimentoBetween(data1, data2);

        return livroLista ;
    }

	
	////////////////////////////////////////////////////////////////////////////			((	Delete ))
	
	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority( 'ROLE_REMOVER_LANCAMENTO' ) ") 
	public void apagarLancamento(@PathVariable Long codigo) {

			lancamentoRepository. delete(codigo);
	
	}
	
	
	
	
}
