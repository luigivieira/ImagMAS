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
import jadex.adapter.fipa.*;
import jadex.runtime.*;

@SuppressWarnings("serial")
public class CInformAnalysisConclusionPlan extends Plan
{
	@Override
	public void body()
	{
		IMessageEvent pMsg = createMessageEvent("conclusion_message");
		pMsg.setContent(new CConclusionInformation(true));
		
		String asAgents[] = (String []) getBeliefbase().getBeliefSet("agents_running").getFacts();
		
		if(asAgents.length > 0)
		{
			for(int i = 0; i < asAgents.length; i++)
				pMsg.getParameterSet(SFipa.RECEIVERS).addValue(new AgentIdentifier(asAgents[i]));
	
			//System.out.println(getAgentIdentifier().getName() + " informando conclusão de análise.");
		
			sendMessage(pMsg);
		}
	}
}
