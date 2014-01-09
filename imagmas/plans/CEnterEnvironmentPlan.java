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
public class CEnterEnvironmentPlan extends Plan
{
	@Override
	public void body()
	{
		// +--- Obtem a crença sobre o próprio agente
		CAbstractAgent pMyself = (CAbstractAgent) getBeliefbase().getBelief("myself").getFact();

		// +--- Obtem a crença com a localidade atual do agente
		CLocation pTargetLocation = (CLocation) getBeliefbase().getBelief("location").getFact();
		
		// +--- Obtem o atuador de mobilidade
		CRegionActuator pActuator = (CRegionActuator) pMyself.getActuator("region");
		
		// +--- Executa o atuador, para movimentar o agente para a posição inicial sobre o ambiente
		Object apParameters[] = {null, pTargetLocation};
		Object pRet = pActuator.act(apParameters);
		
		if(!(pRet instanceof Boolean) || !((Boolean) pRet))
		{
			System.out.println("O plano de entrada no ambiente do agente " + pMyself.getName() + " e localidade " + pTargetLocation.toString() + " falhou!");
			fail();
		}
	}
}
