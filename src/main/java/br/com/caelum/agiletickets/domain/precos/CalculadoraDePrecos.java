package br.com.caelum.agiletickets.domain.precos;

import java.math.BigDecimal;

import br.com.caelum.agiletickets.models.Sessao;
import br.com.caelum.agiletickets.models.TipoDeEspetaculo;

public class CalculadoraDePrecos {

	public static BigDecimal calcula(Sessao sessao, Integer quantidade) {
		BigDecimal preco;
		
		if(sessao.getEspetaculo().getTipo().equals(TipoDeEspetaculo.CINEMA) || sessao.getEspetaculo().getTipo().equals(TipoDeEspetaculo.SHOW)) {
			//quando estiver acabando os ingressos... 
			preco = obtemPreco(sessao, 0.05, 0.10);
		} else if ((sessao.getEspetaculo().getTipo().equals(TipoDeEspetaculo.BALLET)) || (sessao.getEspetaculo().getTipo().equals(TipoDeEspetaculo.ORQUESTRA))) {
			//quando estiver acabando os ingressos e o filme é longametragem... 
			preco = obtemPreco(sessao, 0.50, 0.20, 0.10);
		} else {
			//nao aplica aumento para teatro (quem vai é pobretão)
			preco = sessao.getPreco();
		} 

		return preco.multiply(BigDecimal.valueOf(quantidade));
	}

	private static BigDecimal obtemPreco(Sessao sessao, Double taxaOcupacaoAtual, Double taxaAcrescimo) {
		BigDecimal preco;
		if((sessao.getTotalIngressos() - sessao.getIngressosReservados()) / sessao.getTotalIngressos().doubleValue() <= taxaOcupacaoAtual) { 
			preco = sessao.getPreco().add(sessao.getPreco().multiply(BigDecimal.valueOf(taxaAcrescimo)));
		} else {
			preco = sessao.getPreco();
		}
		return preco;
	}

	private static BigDecimal obtemPreco(Sessao sessao, Double taxaOcupacaoAtual, Double taxaAcrescimo, Double taxaAcrescimoTempo) {
		BigDecimal preco;
		preco = obtemPreco(sessao, taxaOcupacaoAtual, taxaAcrescimo);
				
		if(sessao.getDuracaoEmMinutos() > 60){
			preco = preco.add(sessao.getPreco().multiply(BigDecimal.valueOf(taxaAcrescimoTempo)));
		}

		return preco;
	}
}