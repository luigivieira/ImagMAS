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
 * Plano: Informa��o de Condi��o da Cratera
 * Agente Utilizador: Marcador de Centro (MC)
 * Prop�sito: Informar ao Decompositor de Regi�o (DR) sobre a condi��o
 * da cratera encontrada (v�lida ou inv�lida). O valor da condi��o �
 * recebido de um par�metro originado do objetivo.
 * @author Luiz Carlos Vieira
 */
@SuppressWarnings("serial")
public class CInformCraterConditionPlan extends Plan
{
	/**
	 * M�todo que implementa o corpo do plano. � executado automaticamente
	 * quando o plano � ativado.
	 */
	@Override
	public void body()
	{
		// Obt�m a cren�a com o agente decompositor (DR)
		CRegionDecomposer pDecomp = (CRegionDecomposer) getBeliefbase().getBelief("decomposer").getFact();

		// Obt�m a cren�a com a localidade (centro da cratera)
		CLocation pLocation = (CLocation) getBeliefbase().getBelief("location").getFact();
		
		// Obt�m a cren�a com o raio
		int iRadius = (Integer) getBeliefbase().getBelief("radius").getFact();
		
		// Obt�m o par�metro com a condi��o (v�lida/inv�lida) da cratera
		boolean bValid = (Boolean) getParameter("$crater_valid").getValue();
		
		/*
		 * Cria uma inst�ncia da classe de ontologia utilizada para representar
		 * a informa��o de an�lise da cratera. Configura seus valores de acordo
		 * com o conhecimento atual.
		 */
		CCraterAnalysisInformation pResult = new CCraterAnalysisInformation();
		pResult.setAgentName(getAgentIdentifier().getName());
		pResult.setCraterValid(bValid);
		pResult.setCenterX(pLocation.getX());
		pResult.setCenterY(pLocation.getY());
		pResult.setRadius(iRadius);

		/*
		 * Cria a mensagem de informa��o (Performativa FIPA: INFORM) definida
		 * no arquivo ADF do agente, e inclui no conte�do a inst�ncia da ontologia
		 */
		IMessageEvent pMsg = createMessageEvent("crater_analysis_message");
		pMsg.setContent(pResult);
		
		/*
		 * Adiciona � lista de destinat�rios da mensagem o agente decompositor de
		 * regi�o (DR), e solicita o envio da mensagem a plataforma Jadex/JADE.
		 */
		pMsg.getParameterSet(SFipa.RECEIVERS).addValue(pDecomp.getAgentIdentifier());
		sendMessage(pMsg);

		// Atualiza a cren�a sobre a condi��o da cratera j� ter sido informada.
		getBeliefbase().getBelief("crater_condition_informed").setFact(true);
	}
}
