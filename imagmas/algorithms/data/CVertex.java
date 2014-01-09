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

import imagmas.data.*;

public class CVertex
{
	private CLocation m_pLocation;
	
	private int m_iRadius;
	
	private ArrayList<CLocation> m_apPoints;
	
	public CVertex()
	{
		m_pLocation = null;
		m_iRadius = 0;
		m_apPoints = null;
	}
	
	public CVertex(CLocation pLocation, int iRadius, ArrayList<CLocation> apPoints)
	{
		m_pLocation = (CLocation) pLocation;
		m_iRadius = iRadius;
		m_apPoints = apPoints;
	}
	
	public void setLocation(CLocation pLocation)
	{
		m_pLocation = (CLocation) pLocation;
	}
	
	public CLocation getLocation()
	{
		return m_pLocation;
	}
	
	public void setRadius(int iRadius)
	{
		m_iRadius = iRadius;
	}
	
	public int getRadius()
	{
		return m_iRadius;
	}
	
	public void setPoints(ArrayList<CLocation> apPoints)
	{
		m_apPoints = apPoints;
	}
	
	public ArrayList<CLocation> getPoints()
	{
		return m_apPoints;
	}
	
	@Override
	public boolean equals(Object pObject)
	{
		if(pObject == null || !(pObject instanceof CVertex))
			return false;
		else
			return ((CVertex) pObject).getLocation().equals(m_pLocation);
	}
}
