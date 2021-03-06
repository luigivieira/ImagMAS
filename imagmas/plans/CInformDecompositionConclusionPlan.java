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
import imagmas.environment.*;
import imagmas.ontology.*;
import jadex.adapter.fipa.*;
import jadex.runtime.*;

@SuppressWarnings("serial")
public class CInformDecompositionConclusionPlan extends Plan
{
	@Override
	public void body()
	{
		IMessageEvent pMsg = createMessageEvent("conclusion_message");
		pMsg.setContent(new CConclusionInformation(true));
		
		CAbstractAgent pManager = (CAbstractAgent) getBeliefbase().getBelief("manager").getFact();
		pMsg.getParameterSet(SFipa.RECEIVERS).addValue(pManager.getAgentIdentifier());
		
		CRegion pRegion = (CRegion) getBeliefbase().getBelief("region").getFact();
		System.out.println("N�mero de crateras encontradas: " + CEnvironment.getInstance().getSolutionCount(pRegion));
		
		sendMessage(pMsg);
	}
}
