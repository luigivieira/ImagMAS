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

import imagmas.algorithms.*;
import imagmas.data.*;
import imagmas.environment.*;
import imagmas.ontology.*;
import jadex.adapter.fipa.*;
import jadex.runtime.*;

@SuppressWarnings("serial")
public class CConcludeToMinimumCirclePlan extends Plan
{
	@Override
	public void body()
	{
		// +--- Calcula o círculo mínimo para as marcas e adiciona como solução
		CMark apMarks[] = (CMark []) getBeliefbase().getBeliefSet("marks").getFacts();
		CLocation apLocations[] = new CLocation[apMarks.length];
		int i;
		for(i = 0; i < apMarks.length; i++)
			apLocations[i] = apMarks[i].getLocation();
		
		Object apPair[] = CGeometricAlgorithms.calcMinimumCircle(apLocations);
		CCrater pCrater = new CCrater();
		pCrater.setMarkerName(getAgentIdentifier().getName());
		pCrater.setCenter((CLocation) apPair[0]); 
		pCrater.setRadius((Integer) apPair[1]);
		
		int iMinimumRadius = (Integer) getBeliefbase().getBelief("minimum_crater_radius").getFact();
		int iMaximumRadius = (Integer) getBeliefbase().getBelief("maximum_crater_radius").getFact();
		
		if(((pCrater.getRadius() >= iMinimumRadius) && (pCrater.getRadius() <= iMaximumRadius)))
		{
			getBeliefbase().getBeliefSet("valid_craters").addFact(pCrater);
			
			CRegion pRegion = (CRegion) getBeliefbase().getBelief("region").getFact();
			CEnvironment.getInstance().setSolution(pRegion, (CCrater []) getBeliefbase().getBeliefSet("valid_craters").getFacts());
		}
		
		// +--- Avisa todos os agentes da conclusão
		IMessageEvent pMsg = createMessageEvent("conclusion_message");
		pMsg.setContent(new CConclusionInformation(true));
		
		String asAgents[] = (String []) getBeliefbase().getBeliefSet("agents_running").getFacts();
		if(asAgents.length > 0)
		{
			for(i = 0; i < asAgents.length; i++)
				pMsg.getParameterSet(SFipa.RECEIVERS).addValue(new AgentIdentifier(asAgents[i]));
	
			System.out.println(getAgentIdentifier().getName() + " informando conclusão de análise (círculo mínimo).");
		
			sendMessage(pMsg);
		}
		else
			System.out.println(getAgentIdentifier().getName() + " não há agentes para informar a conclusão da análise (círculo mínimo).");
	}
}
