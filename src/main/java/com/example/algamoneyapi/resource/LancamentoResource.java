package com.example.algamoneyapi.resource;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
import com.example.algamoneyapi.repository.PagingAndSortingRepository;
import com.example.algamoneyapi.service.LancamentoService;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoResource {

	@Autowired
	private LancamentoRepository 				lancamentoRepository;
	
	
	@Autowired
	private ApplicationEventPublisher 			publiser;
	
	
	@Autowired
	private LancamentoService 					lancamentoService;
	
	@Autowired
	private PagingAndSortingRepository			pagingAndSortingRepository;
	


	
	//------------------------------------------------------------------------------   Pesquisa    ---------------------------------------------------------------------------------
	
	@GetMapping( "/resumo" )//	ok pesquisa composta																						Pesquisa por Nome Pessoa
	@PreAuthorize("hasAuthority( 'ROLE_PESQUISAR_LANCAMENTO' ) ")
	public Page<Lancamento> pesquisaLancamentoResumo(@PageableDefault  Pageable pageable) {

		return pagingAndSortingRepository.findAll(pageable);
	}
	
	@GetMapping    //ok																															Pesquisa Geral Simples
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public Page<Lancamento> buscaLancamentos(@PageableDefault  Pageable pageable) {
		
		return  pagingAndSortingRepository.findAll(pageable);
	}
	
	@GetMapping( "/lista" )    //ok																															Pesquisa Geral Simples
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public List<Lancamento> buscaLancamentosLista() {
		
		return  lancamentoRepository.findAll();
	}

	
	@GetMapping( params= "lancamentoNome" )//	ok pesquisa composta																						Pesquisa por Nome Pessoa
	@PreAuthorize("hasAuthority( 'ROLE_PESQUISAR_LANCAMENTO' ) ")
	public Page<Lancamento> pesquisaLancamentoPessoaNome(@RequestParam("lancamentoNome") String valor, @PageableDefault Pageable pageable) {
		
		return pagingAndSortingRepository.findByPessoaNomeContainingIgnoreCase(valor, pageable);
	}
	
	
	@GetMapping("/{codigo}")// 	ok																															Pesquisa lançamento por código
	@PreAuthorize("hasAuthority( 'ROLE_PESQUISAR_LANCAMENTO' ) and #oauth2.hasScope('read') ")
	public ResponseEntity<?>  buscaLancamentos(@PathVariable long codigo) {
	
	return lancamentoRepository.exists(codigo)? ResponseEntity.ok(lancamentoRepository.findOne(codigo)) : ResponseEntity.noContent().build();
	}
	
	@GetMapping( params="pagamentoDepoisDe"  )//	pesquisa composta ok																		            Pesquisa por data depois do pagamento
	@PreAuthorize("hasAuthority( 'ROLE_PESQUISAR_LANCAMENTO' ) ")
    public List<Lancamento> pesquisaLancamentoPagamentoDepoisDaData(@RequestParam("pagamentoDepoisDe") String dataDepoisDe) {
		
		LocalDate 			data			=		LocalDate.parse(dataDepoisDe);
		
		List<Lancamento> 	livroLista		=		lancamentoRepository.findByDataPagamentoAfter(data);
		
        return livroLista ;
    }
	
	
	@GetMapping( params = "lancamentoDescricao" )//		pesquisa composta  ok  																		Pesquisa por Descricao  (lancamento)
	@PreAuthorize("hasAuthority( 'ROLE_PESQUISAR_LANCAMENTO' ) ")
	public Page<Lancamento> pesquisaLancamentoDescricao(@RequestParam("lancamentoDescricao") String valor, @PageableDefault Pageable pageable) {

		return pagingAndSortingRepository.findByDescricaoContainingIgnoreCase(valor, pageable);
	}
	
   
	@GetMapping(params= "pagamentoDataAntesDe" ) //  pesquisa composta ok									    								pesquisa por datas de pagamentos antes do value
	@PreAuthorize("hasAuthority( 'ROLE_PESQUISAR_LANCAMENTO' ) ")
    public List<Lancamento> pesquisaLancamentoPagamentoAntesDaData(@RequestParam("pagamentoDataAntesDe") String value1) {
		
		LocalDate 			data			=		LocalDate.parse(value1);
		
		List<Lancamento> 	livroLista		=		lancamentoRepository.findByDataPagamentoBefore(data);
		
        return livroLista ;
    }
	
	
	@GetMapping( "vencimentoDataAntesDe" )// Pesquisa composta ok																						pesquisa por datas de Vencimento antes do value
	@PreAuthorize("hasAuthority( 'ROLE_PESQUISAR_LANCAMENTO' ) ")
    public List<Lancamento> pesquisaLancamentoVencimentoAntesDaData(@RequestParam("vencimentoDataAntesDe") String value1) {
		
		LocalDate 		data			=		LocalDate.parse(value1);
		
		List<Lancamento> 	livroLista		=		lancamentoRepository.findByDataVencimentoBefore(data);
		
        return livroLista ;
    }
	
    
	@GetMapping( "vencimentoDataDepoisDe" )//    Pesquisa composta ok													   pesquisa por datas de Vencimento depois do value
	@PreAuthorize("hasAuthority( 'ROLE_PESQUISAR_LANCAMENTO' ) ")
    public List<Lancamento> pesquisaLancamentoVencimentoDepoisDaData(@RequestParam("vencimentoDataDepoisDe") String value1) {
		
		LocalDate 			data			=		LocalDate.parse(value1);
		
		List<Lancamento> 	livroLista		=		lancamentoRepository.findByDataVencimentoAfter(data);
		
        return livroLista ;
    }
    
	@GetMapping(params= "lancamentoPagamentoEntre" )// Pesquisa composta ok																		   pesquisa Data entre dois valores
	@PreAuthorize("hasAuthority( 'ROLE_PESQUISAR_LANCAMENTO' ) ")
    public List<Lancamento> pesquisaLancamentoPagamentoEntreDuasDatas(@RequestParam("lancamentoPagamentoEntre") String value1 , @RequestParam("valor") String value2) {
		LocalDate data1	=	LocalDate.parse(value1);
		
		LocalDate data2	=	LocalDate.parse(value2);
		
		List<Lancamento> livroLista		=		lancamentoRepository.findByDataPagamentoBetween(data1, data2);
		
        return livroLista;
    }
	
    
	@RequestMapping( params="lancamentoVencimentoEntre" )//  pesquisa composta ok																pesquisa entre datas do Vencimento
	@PreAuthorize("hasAuthority( 'ROLE_PESQUISAR_LANCAMENTO' ) ")
    public List<Lancamento> pesquisaLancamentoPVencimentoEntreDuasDatas(@RequestParam("lancamentoVencimentoEntre") String value1, @RequestParam("valor") String value2) {
		
		LocalDate data1	=	LocalDate.parse(value1);
		
		LocalDate data2	=	LocalDate.parse(value2);
		
		List<Lancamento> livroLista		=		lancamentoRepository.findByDataVencimentoBetween(data1, data2);

        return livroLista ;
    }
	
	
	// 																																		   Pesquisa Lançamento Avançada composta
		@GetMapping(params= "pesquisaComposta" )
		@PreAuthorize("hasAuthority( 'ROLE_PESQUISAR_LANCAMENTO' ) ")
	    public Page<Lancamento> pesquisaLancamentoComposta(@RequestParam("pesquisaComposta") String nome1,
	    			@PathParam("nome2") 			String nome2,
	    			@PathParam("pagdepoisde") 		String pagdepoisde,
	    			@PathParam("lanDescricao") 		String lanDescricao,
	    			@PathParam("pagAntesDe") 		String pagAntesDe,
	    			@PathParam("venAntesDe") 		String venAntesDe,
	    			@PathParam("venDepoisDe") 		String venDepoisDe,
	    			@PathParam("lanPagEntre") 		String lanPagEntre, @PathParam("lanPagE") String lanPagE,
	    			@PathParam("lanVenEntre") 		String lanVenEntre, @PathParam("lanVenE") String lanVenE,

	    			@PageableDefault Pageable pageable
	    		) {
			
			List<Lancamento> retorno = new ArrayList<Lancamento>() ;
		
			List<Lancamento> pesquisa1 = lancamentoRepository.findByPessoaNomeContainingIgnoreCase(nome1);
			
			retorno.addAll(pesquisa1) ;
			
		
		
			if (nome2 != null ) {

				retorno.retainAll(lancamentoRepository.findByPessoaNomeContainingIgnoreCase(nome2));
			}
	
		
			if (pagdepoisde != null ) {
				
				LocalDate 			data			=		LocalDate.parse(pagdepoisde);
				
				retorno.retainAll(lancamentoRepository.findByDataPagamentoAfter(data));
			}
			

			if (lanDescricao != null) {

				retorno.retainAll( lancamentoRepository.findByDescricaoContainingIgnoreCase(lanDescricao) );
			}
		
			
			if (pagAntesDe != null) {
			
				LocalDate 			data			=		LocalDate.parse(pagAntesDe);
				
				retorno.retainAll(	lancamentoRepository.findByDataPagamentoBefore(data) );
			}
		
			
			
			if (venAntesDe != null) {
				
				LocalDate 			data			=		LocalDate.parse(venAntesDe);
				
				retorno.retainAll(	lancamentoRepository.findByDataVencimentoBefore(data) );
			}

			
			if (venDepoisDe != null) {
				
				LocalDate 			data			=		LocalDate.parse(venDepoisDe);
				
				retorno.retainAll(	lancamentoRepository.findByDataVencimentoAfter(data) );
			}
			

			if (lanPagEntre != null && lanPagE != null ) {
				
				LocalDate data1	=	LocalDate.parse(lanPagEntre);
				
				LocalDate data2	=	LocalDate.parse(lanPagE);

				retorno.retainAll(	lancamentoRepository.findByDataPagamentoBetween(data1, data2) );
			}

			if (lanVenEntre != null && lanVenE != null ) {
							
				LocalDate data1	=	LocalDate.parse(lanVenEntre);
				
				LocalDate data2	=	LocalDate.parse(lanVenE);
	
				retorno.retainAll( lancamentoRepository.findByDataVencimentoBetween(data1, data2) );
			}

			
			int start = pageable.getOffset();
			int end = (start + pageable.getPageSize()) > retorno.size() ? retorno.size() : (start + pageable.getPageSize());
			Page<Lancamento> pages = new PageImpl<Lancamento>(retorno.subList(start, end), pageable, retorno.size());
		
			return pages;
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
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasAuthority( 'ROLE_REMOVER_LANCAMENTO' ) and #oauth2.hasScope('write') ") 
	public void apagarLancamento(@PathVariable Long codigo) {

			lancamentoRepository.delete(codigo);
	}
	//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

	
	
	//-----------------------------------------------------------------------------------    Atualiza      ---------------------------------------------------------------------------------------------------------
	
	@PutMapping()
	public ResponseEntity<?> atualizarLancamento(@Valid @RequestBody  Lancamento lancamentoEnviado ) {
	
		return	lancamentoService.atualiza(lancamentoEnviado);
	}
	
	//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	
	
}
