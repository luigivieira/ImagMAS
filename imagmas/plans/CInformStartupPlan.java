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

@SuppressWarnings("serial")
public class CInformStartupPlan extends Plan
{
	@Override
	public void body()
	{
		// +--- Obtem a crença com o agente decompositor
		CRegionDecomposer pDecomp = (CRegionDecomposer) getBeliefbase().getBelief("decomposer").getFact();

		// +--- Envia uma mensagem ao decompositor avisando do início da análise
		CStartInformation pInfo = new CStartInformation();
		
		IMessageEvent pMsg = createMessageEvent("start_information_message");
		pMsg.setContent(pInfo);
		pMsg.getParameterSet(SFipa.RECEIVERS).addValue(pDecomp.getAgentIdentifier());
		
		sendMessage(pMsg);
	}
}
