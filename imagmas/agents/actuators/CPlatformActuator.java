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

import java.util.*;
import java.util.logging.*;

import jadex.adapter.fipa.*;
import jadex.runtime.*;

public class CPlatformActuator extends CAbstractActuator implements IActuator
{
	public CPlatformActuator(String sName)
	{
		super(sName);
	}
	
	@SuppressWarnings("unchecked")
	public Object act(Object[] apParameters)
	{
		if(apParameters == null || apParameters.length < 2)
		{
			System.out.println("Erro no atuador de plataforma: N�mero de par�metros inv�lido!");
			return false;
		}
		
		Plan pPlan;
		String sCommand, sClassOrName, sNewAgentName;
		HashMap<String, Object> mpNewAgentParameters;
		
		
		/*
		 * A matriz de par�metros deve conter os seguintes par�metros obrigat�rios:
		 * [0] --> Plano em execu��o, para permitir acesso ao log e as capacidades
		 * [1] --> String com o comando, que pode ser "create", "start", "delete" ou "shutdown"
		 *         
		 * E pode conter os seguintes par�metros opcionais:
		 * [2] --> Classe do novo agente (obrigat�rio se o comando for "create") ou nome do agente a ser executado/eliminado
		 *         (obrigat�rio se o comando for "start" ou "delete")
		 * [3] --> Mapa com os par�metros para o novo agente a ser criado (obrigat�rio se o comando
		 *         for "create").
		 * [4] --> Nome para o novo agente a ser criado (obrigat�rio se o comando for "create").
		 * [5] --> Valor booleano indicando se o agente deve ser iniciado automaticamente ou n�o
		 * (utilizado para o comando for "create", mas mesmo assim opcional. O valor default � true).
		 */
		
		// +--- Plano em execu��o
		if(!(apParameters[0] instanceof Plan))
		{
			System.out.println("Erro no atuador de plataforma: par�metro 'plano' inv�lido!");
			return false;
		}
		else
			pPlan = (Plan) apParameters[0];
		
		// +--- String com o comando
		if(!(apParameters[1] instanceof String))
		{
			pPlan.getLogger().log(Level.SEVERE, "Erro no atuador de plataforma: par�metro 'comando' inv�lido!");
			return false;
		}
		else
			sCommand = (String) apParameters[1];

		/*
		 * Execu��o. Verifica os par�metros iniciais e prossegue apropriadamente. Dependendo do comando
		 * os demais par�metros s�o apropriadamente verificados.
		 */
		if(sCommand.equals("create"))
		{
			// +--- Classe do novo agente
			if(!(apParameters[2] instanceof String))
			{
				pPlan.getLogger().log(Level.SEVERE, "Erro no atuador de plataforma: par�metro 'classe do novo agente ou nome do agente a ser eliminado' inv�lido!");
				return false;
			}
			else
				sClassOrName = (String) apParameters[2];
			
			if(apParameters.length < 5)
			{
				pPlan.getLogger().log(Level.SEVERE, "Erro no atuador de plataforma: n�mero de par�metros inv�lido!");
				return false;
			}
				
			// +--- Par�metros para o novo agente
			if(!(apParameters[3] instanceof HashMap))
			{
				pPlan.getLogger().log(Level.SEVERE, "Erro no atuador de plataforma: par�metro 'par�metros do novo agente' inv�lido!");
				return false;
			}
			else
				mpNewAgentParameters = (HashMap<String, Object>) apParameters[3];

			// +--- Nome para o  novo agente
			if(!(apParameters[4] instanceof String))
			{
				pPlan.getLogger().log(Level.SEVERE, "Erro no atuador de plataforma: par�metro 'nome do novo agente' inv�lido!");
				return false;
			}
			else
				sNewAgentName = (String) apParameters[4];

			// +--- Executa imediatamente o novo agente? Default = sim (true)
			boolean bStart = true;
			if(apParameters.length == 6 && (apParameters[5] instanceof Boolean))
				bStart = (Boolean) apParameters[5];
			
			IGoal pGoal = pPlan.createGoal("ams_create_agent");
			pGoal.getParameter("type").setValue(sClassOrName);
			pGoal.getParameter("arguments").setValue(mpNewAgentParameters);
			pGoal.getParameter("name").setValue(sNewAgentName);
			pGoal.getParameter("start").setValue(bStart);
			pPlan.dispatchSubgoalAndWait(pGoal);

			AgentIdentifier pNewAgentID = (AgentIdentifier) pGoal.getParameter("agentidentifier").getValue();
			return pNewAgentID;
		}
		else if(sCommand.equals("start"))
		{
			// +--- Nome do agente a ser iniciado
			if(!(apParameters[2] instanceof String))
			{
				pPlan.getLogger().log(Level.SEVERE, "Erro no atuador de plataforma: par�metro 'classe do novo agente ou nome do agente a ser eliminado' inv�lido!");
				return false;
			}
			else
				sClassOrName = (String) apParameters[2];

			AgentIdentifier pAgent = new AgentIdentifier(sClassOrName, true);

			IGoal pGoal = pPlan.createGoal("ams_start_agent");
			pGoal.getParameter("agentidentifier").setValue(pAgent);
			pPlan.dispatchSubgoalAndWait(pGoal);
			
			return true;
		}
		else if(sCommand.equals("delete"))
		{
			// +--- Nome do agente a ser eliminado
			if(!(apParameters[2] instanceof String))
			{
				pPlan.getLogger().log(Level.SEVERE, "Erro no atuador de plataforma: par�metro 'classe do novo agente ou nome do agente a ser eliminado' inv�lido!");
				return false;
			}
			else
				sClassOrName = (String) apParameters[2];
			
			AgentIdentifier pAgent = new AgentIdentifier(sClassOrName, true);

			IGoal pGoal = pPlan.createGoal("ams_destroy_agent");
			pGoal.getParameter("agentidentifier").setValue(pAgent);
			
			try
			{
				pPlan.dispatchSubgoalAndWait(pGoal);
			}
			catch(Exception e)
			{
			}
			
			return true;
		}
		else if(sCommand.equals("shutdown"))
		{
			IGoal pGoal = pPlan.createGoal("ams_shutdown_platform");
			pPlan.dispatchSubgoal(pGoal);
			
			return true;
		}
		else
		{
			pPlan.getLogger().log(Level.SEVERE, "Erro no atuador de plataforma: comando inv�lido!");
			return false;
		}
	}
}
