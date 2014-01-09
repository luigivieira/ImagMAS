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
import imagmas.algorithms.*;
import imagmas.algorithms.data.*;
import imagmas.data.*;
import jadex.adapter.fipa.*;
import jadex.runtime.*;

@SuppressWarnings("serial")
public class CCreateCenterMarkersPlan extends Plan
{
	private CRegion m_pRegion;
	
	private CPlatformActuator m_pPlatformActuator;
	
	private CAbstractAgent m_pMyself;
	
	private int m_iMinimumRadius;
	
	private int m_iMaximumRadius;
	
	private double m_dInclusionFactor;
	
	private double m_dDifferentiationFactor;
	
	@Override
	public void body()
	{
		// +--- Crença a respeito de si mesmo
		m_pMyself = (CAbstractAgent) getBeliefbase().getBelief("myself").getFact();
		
		// +--- Crença com os valores de configuração da segmentação.
		m_iMinimumRadius = (Integer) getBeliefbase().getBelief("minimum_crater_radius").getFact();
		m_iMaximumRadius = (Integer) getBeliefbase().getBelief("maximum_crater_radius").getFact();
		m_dInclusionFactor = (Double) getBeliefbase().getBelief("inclusion_factor").getFact();
		m_dDifferentiationFactor = (Double) getBeliefbase().getBelief("differentiation_factor").getFact();
		
		// +--- Crença a respeito da região de execução dos agentes.
		m_pRegion = (CRegion) getBeliefbase().getBelief("region").getFact();
		
		// +--- Crença com as marcas de borda existentes na região
		CMark apMarks[] = (CMark []) getBeliefbase().getBeliefSet("marks").getFacts();

		// +--- Obtem o atuador de hierarquia
		m_pPlatformActuator = (CPlatformActuator) m_pMyself.getActuator("platform");
		
		// +--- A partir das marcas de borda, calcula os vértices do Diagrama de Voronoi para a criação dos agentes.
		ArrayList<CLocation> apRegionLocations = m_pRegion.getLocationList();
		CLocation apLocations[] = new CLocation[apMarks.length];
		int i;
		for(i = 0; i < apMarks.length; i++)
			apLocations[i] = apMarks[i].getLocation();

		ArrayList<CVertex> apVertices = CGeometricAlgorithms.calcVoronoiVertices(apLocations, apRegionLocations);
		int iCreated = 0, iNumAgents = apVertices.size();
		
		// +--- Se existirem mais do que um vértice, cria um Marcadores de Centro para cada 
		if(iNumAgents > 1)
		{
			int iRadius;
			ArrayList<CLocation> apPoints;
			CLocation pLocation = null;		
			
			for(i = 0; i < iNumAgents; i++)
			{
				pLocation = apVertices.get(i).getLocation();
				apPoints = apVertices.get(i).getPoints();
				
				// +--- Ajusta o raio para "enxergar" todas as marcas - gera resultados melhores
				//iRadius = apVertices.get(i).getRadius();
				iRadius = calculateBetterRadius(pLocation, apPoints);
				
				// +--- Ignora os vértices que não apresentem o raio mínimo necessário a uma cratera válida
				if(iRadius < m_iMinimumRadius || iRadius > m_iMaximumRadius)
					continue;
				
				createMarkerAgent(pLocation, iRadius, apPoints);
				iCreated++;
			}
		}
		
		// +--- Se nenhum marcador de centro foi criado, encerra imediatamente (simulando que todos agentes já concluiram a análise)
		if(iCreated == 0)
		{
			System.out.println("0 marcadores criados.");
			getBeliefbase().getBeliefSet("agents_pending_analysis").addFact(getAgentIdentifier().getName());
			getBeliefbase().getBeliefSet("agents_pending_analysis").removeFacts();
		}
		// +--- Se só um foi criado, indica o início da contagem para término pois ele é a resposta. 
		else if(iCreated == 1)
		{
			System.out.println("1 marcador criado.");
			getBeliefbase().getBelief("seconds_since_last_analysis_inform").setFact(0);
		}
		// +--- Se mais do que um foram criados, segue procedimento normal
		else
			System.out.println(iCreated + " marcadores criados.");
		
		getBeliefbase().getBelief("decomposition_in_progress").setFact(true);
	}
	
	private void createMarkerAgent(CLocation pLocation, int iRadius, ArrayList<CLocation> apPoints)
	{
		// +--- Cria um mapa com os parâmetros para o novo agente decompositor
		HashMap<String, Object> mpArgs;
		mpArgs = new HashMap<String, Object>();
		mpArgs.put("decomposer", m_pMyself);
		mpArgs.put("location", pLocation);
		mpArgs.put("radius", iRadius);
		mpArgs.put("inclusion_factor", m_dInclusionFactor);
		mpArgs.put("differentiation_factor", m_dDifferentiationFactor);
		mpArgs.put("marks_param", getMarksForLocations(apPoints));

		String sAgentName = "CenterMarker#R" + m_pRegion.getID() + "L" + pLocation.toString();
		
		// +--- Cria os parâmetros para o atuador
		Object[] apArgs = { this,                               // Plano
							"create",                           // String com o comando
							"imagmas.agents.ADFs.CenterMarker", // Classe do novo agente
							mpArgs,                             // Mapa com os parâmetros para o novo agente
							sAgentName                          // Nome do novo agente
						  };	
		
		// +--- Executa o atuador e em caso de sucesso adiciona o novo agente às crenças
		Object pRet = m_pPlatformActuator.act(apArgs);

		if(pRet instanceof AgentIdentifier)
		{
			getBeliefbase().getBeliefSet("agents_pending_analysis").addFact(((AgentIdentifier) pRet).getName());
			getBeliefbase().getBeliefSet("agents_running").addFact(((AgentIdentifier) pRet).getName());
			getLogger().info("Novo marcador de centro criado na localidade L" + pLocation.toString());
		}
		else
		{
			getLogger().warning("Marcador de centro da localidade L" + pLocation.toString() + " não pôde ser criado. Verifique mensagem de erro anterior.");
			fail();
		}
	}
	
	private CMark[] getMarksForLocations(ArrayList<CLocation> apLocations)
	{
		ArrayList<CMark> apMarkList = new ArrayList<CMark>();
		CMark apMarks[] = (CMark []) getBeliefbase().getBeliefSet("marks").getFacts();

		for(int i = 0; i < apMarks.length; i++)
			if(apLocations.contains(apMarks[i].getLocation()))
				apMarkList.add(apMarks[i]);

		CMark apRet[] = new CMark[apMarkList.size()];
		apMarkList.toArray(apRet);
		return apRet;
	}
	
	private int calculateBetterRadius(CLocation pLocation, ArrayList<CLocation> apPoints)
	{
		int iRadius, iRet = 0;
		CLocation pMark;
		Iterator<CLocation> pIt = apPoints.iterator();
		while(pIt.hasNext())
		{
			pMark = pIt.next();
			iRadius = (int) Math.ceil(pLocation.calcDistanceFrom(pMark));
			if(iRadius > iRet)
				iRet = iRadius;
		}
		
		return iRet;
	}
}
