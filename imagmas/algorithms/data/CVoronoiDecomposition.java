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

import java.util.*;

public class CVoronoiDecomposition
{
	private ArrayList<CVoronoiVertex> m_apVertices;
	
	private ArrayList<CVoronoiEdge> m_apEdges;
	
	public CVoronoiDecomposition()
	{
		m_apVertices = new ArrayList<CVoronoiVertex>();
		m_apEdges = new ArrayList<CVoronoiEdge>();
	}
	
	public void addVertex(CVoronoiVertex pVertex)
	{
		m_apVertices.add(pVertex);
	}
	
	public void removeVertex(CVoronoiVertex pVertex)
	{
		m_apVertices.remove(pVertex);
	}
	
	public ArrayList<CVoronoiVertex> getVertices()
	{
		return m_apVertices;
	}

	public void addEdge(CVoronoiEdge pEdge)
	{
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
	
	@Override
	public boolean equals(Object pData)
	{
		if((pData == null) || !(pData instanceof CVoronoiDecomposition))
			return false;
				
		CVoronoiDecomposition pDecomp = (CVoronoiDecomposition) pData;
		return m_apVertices.equals(pDecomp.m_apVertices);
	}
	
	@Override
	public String toString()
	{
		return "[CVoronoiDecomposition] Vértices: " + m_apVertices.size() + " Arestas: " + m_apEdges.size();
	}
}
