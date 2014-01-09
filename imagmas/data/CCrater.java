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
 
package imagmas.data;

public class CCrater
{
	private String m_sMarkerName;
	
	private CLocation m_pCenter;
	
	private int m_iRadius;
	
	public CCrater()
	{
		m_sMarkerName = "";
		m_pCenter = null;
		m_iRadius = -1;
	}

	public String getMarkerName()
	{
		return m_sMarkerName;
	}

	public void setMarkerName(String sValue)
	{
		m_sMarkerName = sValue;
	}
	
	public CLocation getCenter()
	{
		return m_pCenter;
	}

	public void setCenter(CLocation pValue)
	{
		m_pCenter = pValue;
	}

	public int getRadius()
	{
		return m_iRadius;
	}

	public void setRadius(int iValue)
	{
		m_iRadius = iValue;
	}
	
	@Override
	public boolean equals(Object pObject)
	{
		if(pObject == null || !(pObject instanceof CCrater))
			return false;
		else
			return ((CCrater) pObject).getCenter().equals(m_pCenter);
	}
	
	@Override
	public String toString()
	{
		return "R[" + m_iRadius + "] C[" + m_pCenter.getX() + ", " + m_pCenter.getY() + "]";
	}
}
