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

import java.util.*;

import imagmas.agents.*;
import imagmas.agents.actuators.*;
import imagmas.data.*;
import jadex.adapter.fipa.*;
import jadex.runtime.*;

/**
 * Classe do plano para cria��o de um agente decompositor para uma regi�o dada.
 * @author Luiz Carlos Vieira
 */
@SuppressWarnings("serial")
public class CCreateDecomposerPlan extends Plan
{
	/**
	 * M�todo que implementa a execu��o do plano.
	 */
	@Override
	public void body()
	{
		// +--- Obtem a cren�a sobre o pr�prio agente
		CAbstractAgent pMyself = (CAbstractAgent) getBeliefbase().getBelief("myself").getFact();
		
		// +--- Obtem o par�metro com a regi�o
		CRegion pRegion = (CRegion) getParameter("$region").getValue();
		
		System.out.println("Processando regi�o " + pRegion.getID());
		
		// +--- Obtem o atuador de hierarquia
		CPlatformActuator pActuator = (CPlatformActuator) pMyself.getActuator("platform");

		// +--- Obtem os valores de configura��o da segmenta��o
		int iMinimumRadius = (Integer) getBeliefbase().getBelief("minimum_crater_radius").getFact();
		int iMaximumRadius = (Integer) getBeliefbase().getBelief("maximum_crater_radius").getFact();
		int iMarksDistance = (Integer) getBeliefbase().getBelief("distance_between_marks").getFact();
		double dInclusionFactor = (Double) getBeliefbase().getBelief("inclusion_factor").getFact();
		double dDifferentiationFactor = (Double) getBeliefbase().getBelief("differentiation_factor").getFact();
		
		// +--- Cria um mapa com os par�metros para o novo agente decompositor
		HashMap<String, Object> mpArgs;
		mpArgs = new HashMap<String, Object>();
		mpArgs.put("manager", pMyself);
		mpArgs.put("region", pRegion);
		mpArgs.put("minimum_crater_radius", iMinimumRadius);
		mpArgs.put("maximum_crater_radius", iMaximumRadius);
		mpArgs.put("distance_between_marks", iMarksDistance);
		mpArgs.put("inclusion_factor", dInclusionFactor);
		mpArgs.put("differentiation_factor", dDifferentiationFactor);

		// +--- Cria os par�metros para o atuador
		Object[] apArgs = { this,                                   // Plano
							"create",                               // String com o comando
							"imagmas.agents.ADFs.RegionDecomposer", // Classe do novo agente
							mpArgs,                                 // Mapa com os par�metros para o novo agente
							"Decomposer#R" + pRegion.getID()        // Nome do novo agente
						  };	
		
		// +--- Executa o atuador e em caso de sucesso adiciona o novo agente �s cren�as
		Object pRet = pActuator.act(apArgs);

		if(pRet instanceof AgentIdentifier)
		{
			getBeliefbase().getBeliefSet("regions_processed").addFact(pRegion);
			getBeliefbase().getBelief("region_in_process").setFact(pRegion);
			getBeliefbase().getBelief("decomposer_running").setFact(((AgentIdentifier) pRet).getName());
			getLogger().info("Novo decompositor criado para regi�o #" + pRegion.getID());
		}
		else
		{
			getBeliefbase().getBelief("region_in_process").setFact(null);
			getBeliefbase().getBelief("decomposer_running").setFact("");
			getLogger().warning("Decompositor da regi�o #" + pRegion.getID() + " n�o p�de ser criado. Verifique mensagem de erro anterior.");
			fail();
		}
	}
}
