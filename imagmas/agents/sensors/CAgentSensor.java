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
 
package imagmas.agents.sensors;

import imagmas.agents.*;
import imagmas.data.*;
import imagmas.environment.*;

public class CAgentSensor extends CAbstractSensor implements ISensor
{
	public CAgentSensor(String sName)
	{
		super(sName);
	}
	
	public Object[] sense()
	{
		CAbstractAgent pAgent = getAgent();
		if(pAgent == null || !(pAgent instanceof CCenterMarker))
			return new CCenterMarker[0];
		
		CCenterMarker pMarker = (CCenterMarker) pAgent;
		CLocation pLocation = pMarker.getLocation();
		int iRadius = pMarker.getRadius();
		CAbstractAgent apNeighbours[] = CEnvironment.getInstance().getAgentsInCircularArea(pMarker, pLocation, iRadius);
		
		if(apNeighbours == null || apNeighbours.length == 0)
			return new CCenterMarker[0];
		else
		{
			CCenterMarker apMarkers[] = new CCenterMarker[apNeighbours.length];
			for(int i = 0; i < apNeighbours.length; i++)
				apMarkers[i] = (CCenterMarker) apNeighbours[i]; 
			
			return apMarkers;
		}
	}
}
