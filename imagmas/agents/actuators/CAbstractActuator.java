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

import imagmas.agents.*;

public abstract class CAbstractActuator
{
	private String m_sName;
	
	private CAbstractAgent m_pAgent;
	
	public CAbstractActuator()
	{
		setName("");
		setAgent(null);
	}
	
	public CAbstractActuator(String sName)
	{
		setName(sName);
		setAgent(null);
	}
	
	public void setName(String sName)
	{
		m_sName = sName;
	}
	
	public String getName()
	{
		return m_sName;
	}
	
	public void setAgent(CAbstractAgent pAgent)
	{
		m_pAgent = pAgent;
	}
	
	public CAbstractAgent getAgent()
	{
		return m_pAgent;
	}
}
