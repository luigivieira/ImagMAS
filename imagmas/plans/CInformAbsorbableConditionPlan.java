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
import imagmas.ontology.*;
import jadex.adapter.fipa.*;
import jadex.runtime.*;

/***
 * Plano para informa��o a outro MC de sua condi��o de absorv�vel.
 * @author Luiz Carlos Vieira
 */
@SuppressWarnings("serial")
public class CInformAbsorbableConditionPlan extends Plan
{
	/***
	 * M�todo que implementa o corpo do plano. � executado automaticamente
	 * quando o plano � ativado.
	 */
	@Override
	public void body()
	{
		// Obtem o par�metro com o marcador a ser informado
		CCenterMarker pMarker = (CCenterMarker) getParameter("$marker").getValue();
		
		/*
		 * Cria a mensagem de informa��o (Performativa FIPA: INFORM) definida
		 * no arquivo ADF
		 */
		IMessageEvent pMsg = createMessageEvent("absorbable_condition_message");
		
		/*
		 * Inclui no conte�do da mensagem uma inst�ncia da classe de ontologia
		 * utilizada para descrever o conhecimento a respeito da condi��o de absorv�vel.
		 */
		pMsg.setContent(new CAbsorbableConditionInformation(true));
		
		// Adiciona o marcador como destinat�rio da mensagem
		pMsg.getParameterSet(SFipa.RECEIVERS).addValue(pMarker.getAgentIdentifier());
		
		// Solicita o envio
		sendMessage(pMsg);
		
		/*
		 * Atualiza a cren�a sobre os agentes informados da condi��o de absorv�vel.
		 * Inclui o marcador rec�m notificado na lista.
		 */
		getBeliefbase().getBeliefSet("absorbable_agents_informed").addFact(pMarker);
	}
}
