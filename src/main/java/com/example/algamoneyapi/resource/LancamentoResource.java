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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

	
	//----------------------------------------------------------------------------------------------------------------   Pesquisa    ---------------------------------------------------------------------------------
	
	//ok																																								Pesquisa Geral Simples
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public List<Lancamento> buscaLancamentos() {
		
		List<Lancamento> lancamentos = lancamentoRepository.findAll();
		
		return lancamentos;
	}
	
	
	@GetMapping( params = "lancamentoNome" )//	ok																													Pesquisa por Nome Pessoa
	@PreAuthorize("hasAuthority( 'ROLE_PESQUISAR_LANCAMENTO' ) ")
	public List<Lancamento> pesquisaLancamentoPessoaNome(@RequestParam("lancamentoNome") String valor) {

		return lancamentoRepository.findByPessoaNomeContainingIgnoreCase(valor);
	}
	
	
	@GetMapping("/{codigo}")// 	ok																																	Pesquisa lançamento por código
	@PreAuthorize("hasAuthority( 'ROLE_PESQUISAR_LANCAMENTO' ) and #oauth2.hasScope('read') ")
	public ResponseEntity<?>  buscaLancamentos(@PathVariable long codigo) {
	
	return lancamentoRepository.exists(codigo)? ResponseEntity.ok(lancamentoRepository.findOne(codigo)) : ResponseEntity.noContent().build();
	}
	
	//		ok																																		             Pesquisa por data depois do pagamento
	@GetMapping( params="pagamentoDepoisDe"  )
	@PreAuthorize("hasAuthority( 'ROLE_PESQUISAR_LANCAMENTO' ) ")
    public List<Lancamento> pesquisaLancamentoPagamentoDepoisDaData(@RequestParam("pagamentoDepoisDe") String dataDepoisDe) {
		
		LocalDate 			data			=		LocalDate.parse(dataDepoisDe);
		
		List<Lancamento> 	livroLista		=		lancamentoRepository.findByDataPagamentoAfter(data);
		
        return livroLista ;
    }
	
	//		ok																	                             													Pesquisa por Descricao  (lancamento)
	@GetMapping( params = "lancamentoDescricao" )		
	@PreAuthorize("hasAuthority( 'ROLE_PESQUISAR_LANCAMENTO' ) ")
	public List<Lancamento> pesquisaLancamentoDescricao(@RequestParam("lancamentoDescricao") String valor) {

		return lancamentoRepository.findByDescricaoContainingIgnoreCase(valor);
	}
	
    //  ok 																																					    pesquisa por datas de pagamentos antes do value
	@GetMapping(params= "pagamentoDataAntesDe" )
	@PreAuthorize("hasAuthority( 'ROLE_PESQUISAR_LANCAMENTO' ) ")
    public List<Lancamento> pesquisaLancamentoPagamentoAntesDaData(@RequestParam("pagamentoDataAntesDe") String value1) {
		
		LocalDate 			data			=		LocalDate.parse(value1);
		
		List<Lancamento> 	livroLista		=		lancamentoRepository.findByDataPagamentoBefore(data);
		
        return livroLista ;
    }
	
	// ok  																																						pesquisa por datas de Vencimento antes do value
	@GetMapping( "vencimentoDataAntesDe" )
	@PreAuthorize("hasAuthority( 'ROLE_PESQUISAR_LANCAMENTO' ) ")
    public List<Lancamento> pesquisaLancamentoVencimentoAntesDaData(@RequestParam("vencimentoDataAntesDe") String value1) {
		
		LocalDate 		data			=		LocalDate.parse(value1);
		
		List<Lancamento> 	livroLista		=		lancamentoRepository.findByDataVencimentoBefore(data);
		
        return livroLista ;
    }
	
    //ok																																						   pesquisa por datas de Vencimento depois do value
	@GetMapping( "vencimentoDataDepoisDe" )
	@PreAuthorize("hasAuthority( 'ROLE_PESQUISAR_LANCAMENTO' ) ")
    public List<Lancamento> pesquisaLancamentoVencimentoDepoisDaData(@RequestParam("vencimentoDataDepoisDe") String value1) {
		
		LocalDate 			data			=		LocalDate.parse(value1);
		
		List<Lancamento> 	livroLista		=		lancamentoRepository.findByDataVencimentoAfter(data);
		
        return livroLista ;
    }
	
    // ok																																						   pesquisa Data entre dois valores
	@GetMapping(params= "lancamentoPagamentoEntre" )
	@PreAuthorize("hasAuthority( 'ROLE_PESQUISAR_LANCAMENTO' ) ")
    public List<Lancamento> pesquisaLancamentoPagamentoEntreDuasDatas(@RequestParam("lancamentoPagamentoEntre") String value1 , @RequestParam("valor") String value2) {
		
		LocalDate data1	=	LocalDate.parse(value1);
		
		LocalDate data2	=	LocalDate.parse(value2);
		
		List<Lancamento> livroLista		=		lancamentoRepository.findByDataPagamentoBetween(data1, data2);
		
        return livroLista;
    }
	
    //  ok																																							pesquisa entre datas do Vencimento
	@RequestMapping( params="lancamentoPagamentoEntre" )
	@PreAuthorize("hasAuthority( 'ROLE_PESQUISAR_LANCAMENTO' ) ")
    public List<Lancamento> pesquisaLancamentoPVencimentoEntreDuasDatas(@RequestParam("lancamentoVencimentoEntre") String value1, @RequestParam("valor") String value2) {
		
		LocalDate data1	=	LocalDate.parse(value1);
		
		LocalDate data2	=	LocalDate.parse(value2);
		
		List<Lancamento> livroLista		=		lancamentoRepository.findByDataVencimentoBetween(data1, data2);

        return livroLista ;
    }
	
	
	
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	
	
	
	//------------------------------------------------------------------------------------------        Salvar           -------------------------------------------------------------------------------------------
	
	// 																																							Salva Lançamento
	@PostMapping
	@PreAuthorize("hasAuthority( 'ROLE_CADASTRAR_CATEGORIA' ) and #oauth2.hasScope('write') ")
	public ResponseEntity<Lancamento> salvarLancamento(@Valid  @RequestBody Lancamento lancamento , HttpServletResponse httpResposta  ) {
		
		
		// regra sobre usuário ativo ou não.
		Lancamento lancamentoSalvo = lancamentoService.salvaLancamento(lancamento);	

		publiser.publishEvent(new RecursoCriadoEvent(this, httpResposta, lancamentoSalvo.getCodigo() ) );
		
		return ResponseEntity.status(HttpStatus.CREATED).body(lancamento);
	}
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


	
	//-----------------------------------------------------------------------------------------		Delete        ------------------------------------------------------------------------------------------------
	
	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority( 'ROLE_REMOVER_LANCAMENTO' ) ") 
	public void apagarLancamento(@PathVariable Long codigo) {

			lancamentoRepository. delete(codigo);
	}
	//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

	
	
	//-----------------------------------------------------------------------------------    Atualiza      ---------------------------------------------------------------------------------------------------------
	
	@PutMapping()
	public ResponseEntity<?> atualizarLancamento(@Valid @RequestBody  Lancamento lancamentoEnviado ) {
	
		return	lancamentoService.atualiza(lancamentoEnviado);
	}
	
	//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	
	
}
