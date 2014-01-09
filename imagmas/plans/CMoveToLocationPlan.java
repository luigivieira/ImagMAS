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
import imagmas.agents.actuators.*;
import imagmas.data.*;
import jadex.runtime.*;

@SuppressWarnings("serial")
public class CMoveToLocationPlan extends Plan
{
	@Override
	public void body()
	{
		// +--- Obtem a crença sobre o próprio agente
		CAbstractAgent pMyself = (CAbstractAgent) getBeliefbase().getBelief("myself").getFact();
		
		// +--- Obtem a crença sobre a localidade atual
		CLocation pCurrentLocation = (CLocation) getBeliefbase().getBelief("location").getFact();
		
		// +--- Obtem o parâmetro com a localidade de destino
		CLocation pTargetLocation = (CLocation) getParameter("location").getValue();

		// +--- Obtem o atuador de mobilidade
		CMobilityActuator pActuator = (CMobilityActuator) pMyself.getActuator("mobility");
		
		// +--- Executa o atuador, para movimentar o agente
		Object apParameters[] = {pCurrentLocation, pTargetLocation};
		Object pRet = pActuator.act(apParameters);
		
		if(!(pRet instanceof Boolean) || !((Boolean) pRet))
		{
			System.out.println(pMyself.getName() + ": Não consegui me mover para a localidade " + pTargetLocation);
			//fail();
		}
		else
		{
			getBeliefbase().getBelief("location").setFact(pTargetLocation);
			getLogger().info(pMyself.getName() + ": me movi para a localidade " + pTargetLocation);
		}
	}
}
