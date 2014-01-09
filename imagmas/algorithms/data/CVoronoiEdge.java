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

public class CVoronoiEdge
{
	private CVoronoiVertex m_apVertices[];
	
	public CVoronoiEdge()
	{
		m_apVertices = new CVoronoiVertex[2];
	}
	
	public void setVertices(CVoronoiVertex pOneVertex, CVoronoiVertex pAnotherVertex)
	{
		m_apVertices[0] = pOneVertex;
		m_apVertices[1] = pAnotherVertex;
	}
	
	public CVoronoiVertex[] getVertices()
	{
		return m_apVertices;
	}
	
	@Override
	public boolean equals(Object pData)
	{
		if(m_apVertices[0] == null || m_apVertices[1] == null)
			return false;
			
		if((pData == null) || !(pData instanceof CVoronoiEdge))
			return false;
				
		CVoronoiEdge pEdge = (CVoronoiEdge) pData;
		return (m_apVertices[0].equals(pEdge.m_apVertices[0]) &&  m_apVertices[1].equals(pEdge.m_apVertices[1])) ||
			   (m_apVertices[0].equals(pEdge.m_apVertices[1]) &&  m_apVertices[1].equals(pEdge.m_apVertices[0]));
	}
	
	@Override
	public String toString()
	{
		if(m_apVertices[0] != null && m_apVertices[1] != null)
			return "[CVoronoiEdge] Vértices: " + m_apVertices[0] + " - " + m_apVertices[1];
		else
			return "[CVoronoiEdge] Sem dados";
	}
}
