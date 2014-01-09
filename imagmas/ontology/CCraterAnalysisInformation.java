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
 
package imagmas.ontology;

public class CCraterAnalysisInformation
{
	private String m_sAgentName;

	private boolean m_bCraterValid;
	
	private int m_iCenterX;
	
	private int m_iCenterY;
	
	private int m_iRadius;
	
	public CCraterAnalysisInformation()
	{
		m_sAgentName = "";
		m_bCraterValid = false;
		m_iCenterX = -1;
		m_iCenterY = -1;
		m_iRadius = -1;
	}

	public String getAgentName()
	{
		return m_sAgentName;
	}

	public void setAgentName(String sValue)
	{
		m_sAgentName = sValue;
	}
	
	public boolean isCraterValid()
	{
		return m_bCraterValid;
	}

	public void setCraterValid(boolean bValue)
	{
		m_bCraterValid = bValue;
	}

	public int getCenterX()
	{
		return m_iCenterX;
	}

	public void setCenterX(int iValue)
	{
		m_iCenterX = iValue;
	}

	public int getCenterY()
	{
		return m_iCenterY;
	}

	public void setCenterY(int iValue) {
		m_iCenterY = iValue;
	}

	public int getRadius()
	{
		return m_iRadius;
	}

	public void setRadius(int iValue)
	{
		m_iRadius = iValue;
	}
}
