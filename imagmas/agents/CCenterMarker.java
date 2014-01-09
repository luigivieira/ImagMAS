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
 
package imagmas.agents;

import imagmas.agents.actuators.*;
import imagmas.agents.sensors.*;
import imagmas.data.CLocation;
import jadex.adapter.fipa.*;

public class CCenterMarker extends CAbstractAgent
{
	private CLocation m_pLocation;
	
	private int m_iRadius;
	
	public CCenterMarker(AgentIdentifier pAgentIdentifier, CLocation pLocation, int iRadius)
	{
		super(pAgentIdentifier);
		
		m_pLocation = new CLocation(pLocation.getX(), pLocation.getY());
		m_iRadius = iRadius;
		
		// +--- Adiciona os sensores utilizados pelo agente
		addSensor(new CMarkSensor("mark"));
		addSensor(new CAgentSensor("agent"));
		
		// +--- Adiciona os atuadores utilizados pelo agente
		addActuator(new CMarkActuator("mark"));
		addActuator(new CRegionActuator("region"));
		addActuator(new CPlatformActuator("platform"));
	}
	
	public synchronized CLocation getLocation()
	{
		return new CLocation(m_pLocation.getX(), m_pLocation.getY());
	}
	
	public synchronized void setLocation(CLocation pValue)
	{
		m_pLocation = new CLocation(pValue.getX(), pValue.getY());
	}

	public synchronized int getRadius()
	{
		return m_iRadius;
	}
	
	public synchronized void setRadius(int iValue)
	{
		m_iRadius = iValue;
	}
}
