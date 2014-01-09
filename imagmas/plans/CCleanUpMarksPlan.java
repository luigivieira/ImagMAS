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
public class CCleanUpMarksPlan extends Plan
{
	@Override
	public void body()
	{
		// +--- Crença sobre o próprio agente
		CAbstractAgent pMyself = (CAbstractAgent) getBeliefbase().getBelief("myself").getFact(); 
		
		// +--- Obtem o atuador de movimentação de marcas
		CMarkActuator pActuator = (CMarkActuator) pMyself.getActuator("mark");
		
		// +--- Obtem as marcas existentes
		CMark apMarks[] = (CMark []) getBeliefbase().getBeliefSet("marks").getFacts();

		// +--- Deleta todas as marcas criadas no ambiente.
		Object apParameters[] = {"delete", null, null, null};
		for(int i = 0; i < apMarks.length; i++)
		{
			apParameters[1] = apMarks[i];
			apParameters[2] = apMarks[i].getLocation();
			pActuator.act(apParameters);
		}
	}
}
