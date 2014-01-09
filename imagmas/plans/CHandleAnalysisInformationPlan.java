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

import imagmas.data.*;
import imagmas.environment.*;
import imagmas.ontology.*;
import jadex.runtime.*;

@SuppressWarnings("serial")
public class CHandleAnalysisInformationPlan extends Plan
{
	@Override
	public void body()
	{
		int iMinimumRadius = (Integer) getBeliefbase().getBelief("minimum_crater_radius").getFact();
		int iMaximumRadius = (Integer) getBeliefbase().getBelief("maximum_crater_radius").getFact();
		
		IMessageEvent pMsg = (IMessageEvent) getInitialEvent();
		CCraterAnalysisInformation pResult = (CCraterAnalysisInformation) pMsg.getContent();		
		
		//System.out.println(pResult.getAgentName() + " informou " + pResult.isCraterValid() + " com raio " + pResult.getRadius());
		
		if(pResult.isCraterValid() && ((pResult.getRadius() >= iMinimumRadius) && (pResult.getRadius() <= iMaximumRadius)))
		{
			CCrater pCrater = new CCrater();
			pCrater.setMarkerName(pResult.getAgentName());
			pCrater.setCenter(new CLocation(pResult.getCenterX(), pResult.getCenterY()));
			pCrater.setRadius(pResult.getRadius());
			
			getBeliefbase().getBeliefSet("valid_craters").addFact(pCrater);
			
			CRegion pRegion = (CRegion) getBeliefbase().getBelief("region").getFact();
			CEnvironment.getInstance().setSolution(pRegion, (CCrater []) getBeliefbase().getBeliefSet("valid_craters").getFacts());
		}
		
		getBeliefbase().getBeliefSet("agents_pending_analysis").removeFact(pResult.getAgentName());
	}
}
