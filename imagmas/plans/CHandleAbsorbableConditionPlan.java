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

import imagmas.ontology.*;
import jadex.runtime.*;

/**
 * Plano: Recebimento de Informa��o de Condi��o de Absorv�vel
 * Agente Utilizador: Marcador de Centro (MC)
 * Prop�sito: Receber e tratar a informa��o da condi��o de
 * absorv�vel por outro agente.
 * @author Luiz Carlos Vieira
 */
@SuppressWarnings("serial")
public class CHandleAbsorbableConditionPlan extends Plan
{
	/**
	 * M�todo que implementa o corpo do plano. � executado automaticamente
	 * quando o plano � ativado.
	 */
	@Override
	public void body()
	{
		// Obtem o evento que iniciou o plano. Nesse caso, uma mensagem recebida.
		IMessageEvent pMsg = (IMessageEvent) getInitialEvent();
		
		/*
		 * Obtem do conte�do da mensagem a inst�ncia da ontologia com as
		 * informa��es comunicadas.
		 */
		CAbsorbableConditionInformation pAbs = (CAbsorbableConditionInformation) pMsg.getContent();
		
		/*
		 * Atualiza a cren�a sobre a condi��o de absorv�vel por outro
		 * agente de acordo com a informa��o recebida.
		 */
		getBeliefbase().getBelief("absorbable_by_other_agent").setFact(pAbs.isAbsorbable());
	}
}
