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
 * Agente Utilizador: Decompositor de Regi�o (DR) e Marcador de Centro (MC)
 * Prop�sito: Encerrar a execu��o do agente na plataforma Jadex/JADE.
 * @author Luiz Carlos Vieira
 */
@SuppressWarnings("serial")
public class CEndMyselfPlan extends Plan
{
	/**
	 * M�todo que implementa o corpo do plano. � executado automaticamente
	 * quando o plano � ativado.
	 */
	@Override
	public void body()
	{
		try
		{
			// Obt�m a cren�a sobre o pr�prio agente
			CAbstractAgent pMyself = (CAbstractAgent) getBeliefbase().getBelief("myself").getFact();
			
			/*
			 * Remove o agente do ambiente se ele for um Marcador de Centro (MC)
			 */
			if(pMyself instanceof CCenterMarker)
			{
				// Obt�m a cren�a com a localidade
				CLocation pLocation = (CLocation) getBeliefbase().getBelief("location").getFact();
	
				// Obt�m o atuador de regi�o
				CRegionActuator pRegionActuator = (CRegionActuator) pMyself.getActuator("region");

				// Executa o atuador de regi�o para remover o agente do ambiente
				Object apParameters[] = {pLocation, null};
				pRegionActuator.act(apParameters);
			}
				
			// Obt�m o atuador de plataforma
			CPlatformActuator pPlatformActuator = (CPlatformActuator) pMyself.getActuator("platform");
			
			// Cria os par�metros para o atuador de plataforma
			Object[] apArgs = { this,             // Plano
								"delete",         // String de comando
								pMyself.getName() // Nome do agente
							  };	

			// Executa o atuador de plataforma para encerrar o pr�prio agente
			pPlatformActuator.act(apArgs);
		}
		catch(Exception e)
		{
			System.out.println("O plano de encerramento falhou devido a uma exce��o:");
			e.printStackTrace();
			fail();
		}
	}
}