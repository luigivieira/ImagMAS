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

/**
 * Plano: Encerramento
 * Agente Utilizador: Decompositor de Região (DR) e Marcador de Centro (MC)
 * Propósito: Encerrar a execução do agente na plataforma Jadex/JADE.
 * @author Luiz Carlos Vieira
 */
@SuppressWarnings("serial")
public class CEndMyselfPlan extends Plan
{
	/**
	 * Método que implementa o corpo do plano. É executado automaticamente
	 * quando o plano é ativado.
	 */
	@Override
	public void body()
	{
		try
		{
			// Obtém a crença sobre o próprio agente
			CAbstractAgent pMyself = (CAbstractAgent) getBeliefbase().getBelief("myself").getFact();
			
			/*
			 * Remove o agente do ambiente se ele for um Marcador de Centro (MC)
			 */
			if(pMyself instanceof CCenterMarker)
			{
				// Obtém a crença com a localidade
				CLocation pLocation = (CLocation) getBeliefbase().getBelief("location").getFact();
	
				// Obtém o atuador de região
				CRegionActuator pRegionActuator = (CRegionActuator) pMyself.getActuator("region");

				// Executa o atuador de região para remover o agente do ambiente
				Object apParameters[] = {pLocation, null};
				pRegionActuator.act(apParameters);
			}
				
			// Obtém o atuador de plataforma
			CPlatformActuator pPlatformActuator = (CPlatformActuator) pMyself.getActuator("platform");
			
			// Cria os parâmetros para o atuador de plataforma
			Object[] apArgs = { this,             // Plano
								"delete",         // String de comando
								pMyself.getName() // Nome do agente
							  };	

			// Executa o atuador de plataforma para encerrar o próprio agente
			pPlatformActuator.act(apArgs);
		}
		catch(Exception e)
		{
			System.out.println("O plano de encerramento falhou devido a uma exceção:");
			e.printStackTrace();
			fail();
		}
	}
}