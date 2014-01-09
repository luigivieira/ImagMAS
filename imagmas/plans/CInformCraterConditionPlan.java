/*
 * Copyright (C) 2009 Luiz Carlos Vieira
 * http://www.luiz.vieira.nom.br
 *
 * This file is part of the ImagMAS (A Multiagent System to Estimate
 * the Coverage of Alluminum Alloy Plates Submitted to Peen Forming Process).
 *
 * ImagMAS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ImagMAS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
 
package imagmas.plans;

import imagmas.agents.*;
import imagmas.data.*;
import imagmas.ontology.*;
import jadex.adapter.fipa.*;
import jadex.runtime.*;

/**
 * Plano: Informação de Condição da Cratera
 * Agente Utilizador: Marcador de Centro (MC)
 * Propósito: Informar ao Decompositor de Região (DR) sobre a condição
 * da cratera encontrada (válida ou inválida). O valor da condição é
 * recebido de um parâmetro originado do objetivo.
 * @author Luiz Carlos Vieira
 */
@SuppressWarnings("serial")
public class CInformCraterConditionPlan extends Plan
{
	/**
	 * Método que implementa o corpo do plano. É executado automaticamente
	 * quando o plano é ativado.
	 */
	@Override
	public void body()
	{
		// Obtém a crença com o agente decompositor (DR)
		CRegionDecomposer pDecomp = (CRegionDecomposer) getBeliefbase().getBelief("decomposer").getFact();

		// Obtém a crença com a localidade (centro da cratera)
		CLocation pLocation = (CLocation) getBeliefbase().getBelief("location").getFact();
		
		// Obtém a crença com o raio
		int iRadius = (Integer) getBeliefbase().getBelief("radius").getFact();
		
		// Obtém o parâmetro com a condição (válida/inválida) da cratera
		boolean bValid = (Boolean) getParameter("$crater_valid").getValue();
		
		/*
		 * Cria uma instância da classe de ontologia utilizada para representar
		 * a informação de análise da cratera. Configura seus valores de acordo
		 * com o conhecimento atual.
		 */
		CCraterAnalysisInformation pResult = new CCraterAnalysisInformation();
		pResult.setAgentName(getAgentIdentifier().getName());
		pResult.setCraterValid(bValid);
		pResult.setCenterX(pLocation.getX());
		pResult.setCenterY(pLocation.getY());
		pResult.setRadius(iRadius);

		/*
		 * Cria a mensagem de informação (Performativa FIPA: INFORM) definida
		 * no arquivo ADF do agente, e inclui no conteúdo a instância da ontologia
		 */
		IMessageEvent pMsg = createMessageEvent("crater_analysis_message");
		pMsg.setContent(pResult);
		
		/*
		 * Adiciona à lista de destinatários da mensagem o agente decompositor de
		 * região (DR), e solicita o envio da mensagem a plataforma Jadex/JADE.
		 */
		pMsg.getParameterSet(SFipa.RECEIVERS).addValue(pDecomp.getAgentIdentifier());
		sendMessage(pMsg);

		// Atualiza a crença sobre a condição da cratera já ter sido informada.
		getBeliefbase().getBelief("crater_condition_informed").setFact(true);
	}
}
