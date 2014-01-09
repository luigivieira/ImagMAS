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
import java.util.logging.*;

import imagmas.agents.*;
import imagmas.agents.actuators.*;
import imagmas.data.*;
import jadex.runtime.*;

@SuppressWarnings("serial")
public class CCreateMarksPlan extends Plan
{
	/** Valor da largura da imagem no ambiente. */
	private int m_iWidth;
	
	/** Valor da altura da imagem no ambiente. */
	private int m_iHeight;
	
	/** Localidades de borda da região no ambiente. */
	private CLocation m_apEdgeLocations[];
	
	/**
	 * Matriz para descrição de deltas de vizinhança utilizado pelo algoritmo de navegação em bordas.
	 * A vizinhança é vasculhada no sentido horário a partir do pixel acima do pixel atual. Por isso,
	 * o algoritmo utiliza-se essa matriz com valores de variação nos eixos x e y dispostos desde o
	 * primeiro ao último elemento. A interação nesse sentido permite seguir o sentido horário e
	 * encontrar diretamente os pixels desejados a partir das coordenadas do pixel atual.
	 */
	private int m_aiDelta[][] = { //   X,  Y
				    				{  0, -1 }, // Vizinho ao norte
				    				{  1, -1 }, // Vizinho ao nordeste
				    				{  1,  0 }, // Vizinho ao leste
				    				{  1,  1 }, // Vizinho ao sudeste
				    				{  0,  1 }, // Vizinho ao sul
				    				{ -1,  1 }, // Vizinho ao sudoeste
				    				{ -1,  0 }, // Vizinho ao oeste
				    				{ -1, -1 }  // Vizinho ao noroeste
								};
	
	/**
	 * Lista para armazenar as localidades dos pixels de borda já visitados, durante o algoritmo de navegação.
	 */
	private ArrayList<CLocation> m_apVisited;
	
	/**
	 * Método com o corpo do plano. Contém o algoritmo de navegação na borda da região para o cálculo das marcas.
	 */
	@Override
	public void body()
	{
		// +--- Crença a respeito das localidades formadoras da borda da região.
		m_apEdgeLocations = (CLocation []) getBeliefbase().getBeliefSet("edge_locations").getFacts();
		if(m_apEdgeLocations == null || m_apEdgeLocations.length == 0)
		{
			getLogger().log(Level.SEVERE, "Erro na criação das marcas: localidades de borda desconhecidas!");
			fail();
		}

		// +--- Crença sobre o próprio agente
		CAbstractAgent pMyself = (CAbstractAgent) getBeliefbase().getBelief("myself").getFact(); 
		
		// +--- Obtem o atuador de movimentação de marcas
		CMarkActuator pActuator = (CMarkActuator) pMyself.getActuator("mark");
		
		// +--- Crença a respeito da distância inicial entre duas marcas de borda.
		int iDistance = (Integer) getBeliefbase().getBelief("distance_between_marks").getFact();

		// +--- Crença sobre a altura da imagem no ambiente
		m_iWidth = (Integer) getBeliefbase().getBelief("image_width").getFact();
		
		// +--- Crença sobre a largura da imagem no ambiente
		m_iHeight = (Integer) getBeliefbase().getBelief("image_height").getFact();

		// +--- Lista para armazenar as localidades de borda marcadas
		ArrayList<CMark> apMarks = new ArrayList<CMark>();

		// +--- Cria a lista para armazenar as localidades já "visitadas"
		m_apVisited = new ArrayList<CLocation>();		
		
		// +--- A marca de inicio é sempre a primeira localidade da lista, mas somente se não estiver nos extremos da imagem
		Object pRet;
		Object apParameters[] = {"create", null, null, null};
		CLocation pStart = m_apEdgeLocations[0];
		m_apVisited.add(pStart);
		if(!isLocationAtImageBorder(pStart))
		{
			apParameters[3] = pStart;
			pRet = pActuator.act(apParameters);
			if(!(pRet instanceof CMark))
			{
				getLogger().log(Level.SEVERE, "Erro na criação das marcas. Veja mensagens anteriores.");
				fail();
			}
				
			apMarks.add((CMark) pRet);				
		}

		// +--- Percorre toda a borda, marcando as localidades segundo a distância dada
		CLocation pNext = getNextMarkLocation(pStart, iDistance);
		while(pNext != null)
		{
			// +--- Mas coloca a marca apenas se a localidade não estiver nos extremos da imagem
			if(!isLocationAtImageBorder(pNext))
			{
				apParameters[3] = pNext;
				pRet = pActuator.act(apParameters);
				if(!(pRet instanceof CMark))
				{
					getLogger().log(Level.SEVERE, "Erro na criação das marcas. Veja mensagens anteriores.");
					fail();
				}
					
				apMarks.add((CMark) pRet);
			}
			pNext = getNextMarkLocation(pNext, iDistance);
		}
		
		// +--- Atualiza a crença do agente a respeito das marcas calculadas
		//getLogger().info("Foram calculadas " + apMarks.size() + " marcas de borda.");
		System.out.println("Número de marcas criadas: " + apMarks.size());
		getBeliefbase().getBeliefSet("marks").addFacts(apMarks.toArray());
	}

	private boolean isLocationAtImageBorder(CLocation pLocation)
	{
		int iX = pLocation.getX(), iY = pLocation.getY();
		return iX == 0 || iY == 0 || iX == (m_iWidth - 1) || iY == (m_iHeight - 1);
	}
	
	private boolean isLocationAnEdge(CLocation pLocation)
	{
		for(int i = 0; i < m_apEdgeLocations.length; i++)
			if(m_apEdgeLocations[i].equals(pLocation))
				return true;
		return false;
	}
	
	private CLocation getNextMarkLocation(CLocation pCurrent, int iDistance)
	{
		CLocation pNeighbour;
		int iX, iY, iDelta;		
		for(iDelta = 0; iDelta < m_aiDelta.length; iDelta++)
		{
			iX = pCurrent.getX()+ m_aiDelta[iDelta][0];
			iY = pCurrent.getY()+ m_aiDelta[iDelta][1];
			pNeighbour = new CLocation(iX, iY);
		
			if(!isLocationAnEdge(pNeighbour))
				continue;
				
			if(m_apVisited.contains(pNeighbour))
				continue;
			
			m_apVisited.add(pNeighbour);
			
			if(iDistance == 0)
				return pNeighbour;
			else
				return getNextMarkLocation(pNeighbour, iDistance - 1);
		}

		return null;
	}
}
