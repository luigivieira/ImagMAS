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
 
package imagmas.algorithms.data;

import imagmas.data.*;
import imagmas.ontology.*;

import java.util.*;

public class CVoronoiVertex
{
	private ArrayList<CVoronoiEdge> m_apEdges;

	private CLocation m_pLocation;
	
	private boolean m_bFake;
	
	public CVoronoiVertex(CLocation pLocation)
	{
		m_pLocation = pLocation;
		m_apEdges = new ArrayList<CVoronoiEdge>();
		m_bFake = false;
	}
	
	public CLocation getLocation()
	{
		return m_pLocation;
	}
	
	public void addEdge(CVoronoiEdge pEdge)
	{
		if(!m_apEdges.contains(pEdge))
			m_apEdges.add(pEdge);
	}

	public void removeEdge(CVoronoiEdge pEdge)
	{
		m_apEdges.remove(pEdge);
	}
	
	public ArrayList<CVoronoiEdge> getEdges()
	{
		return m_apEdges;
	}
	
	public void setFake(boolean bValue)
	{
		m_bFake = bValue;
	}
	
	public boolean isFake()
	{
		return m_bFake;
	}
	
	@Override
	public boolean equals(Object pData)
	{
		if((pData == null) || !(pData instanceof CVoronoiVertex))
			return false;
				
		CVoronoiVertex pVertex = (CVoronoiVertex) pData;
		return m_pLocation.equals(pVertex.m_pLocation);
	}
	
	@Override
	public String toString()
	{
		return "[CVoronoiVertex] Localidade: " + m_pLocation.toString() + " Arestas: " + m_apEdges.size() + " Falso: " + m_bFake; 
	}
}
