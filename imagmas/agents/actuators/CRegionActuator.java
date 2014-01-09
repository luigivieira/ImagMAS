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
 
package imagmas.agents.actuators;

import imagmas.agents.*;
import imagmas.data.*;
import imagmas.environment.*;

public class CRegionActuator extends CAbstractActuator implements IActuator
{
	public CRegionActuator(String sName)
	{
		super(sName);
	}

	public Object act(Object[] apParameters)
	{
		if(apParameters == null || apParameters.length != 2)
		{
			System.out.println("Erro no atuador de região: Número de parâmetros inválido!");
			return false;
		}
		
		CAbstractAgent pAgent = getAgent();
		CLocation pSourceLocation, pTargetLocation;
		
		/*
		 * A matriz de parâmetros deve conter os seguintes parâmetros obrigatórios:
		 * [0] --> Localidade atual do agente, ou nulo se ele estiver entrando pela primeira vez no ambiente
		 * [1] --> Localidade de destino do agente, ou nulo se ele estiver deixando o ambiente
		 */
		
		// +--- Localidade de origem para o Agente
		if(apParameters[0] == null)
			pSourceLocation = null;
		else if(!(apParameters[0] instanceof CLocation))
		{
			System.out.println("Erro no atuador de região: parâmetro 'localidade de origem para o agente' inválido!");
			return false;
		}
		else
			pSourceLocation = (CLocation) apParameters[0];

		// +--- Localidade de destino para o Agente
		if(apParameters[1] == null)
			pTargetLocation = null;
		else if(!(apParameters[1] instanceof CLocation))
		{
			System.out.println("Erro no atuador de região: parâmetro 'localidade de destino para o agente' inválido!");
			return false;
		}
		else
			pTargetLocation = (CLocation) apParameters[1];
		
		if(pSourceLocation == null && pTargetLocation == null)
		{
			System.out.println("Erro no atuador de região: ambos os parâmetros 'localidade de origem/destino para o agente' estão nulos!");
			return false;
		}
		
		/*
		 * Execução.
		 */
		if(CEnvironment.getInstance().setAgentLocation(pAgent, pSourceLocation, pTargetLocation) == true)
			return true;
		else
		{
			System.out.println("Erro no atuador de região: não foi possível mover o agente. Verifique as mensagens anteriores.");
			return false;
		}
	}
}
