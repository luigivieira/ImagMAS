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

public class CMarkSensor extends CAbstractSensor implements ISensor
{
	public CMarkSensor(String sName)
	{
		super(sName);
	}
	
	public Object[] sense()
	{
		CAbstractAgent pAgent = getAgent();
		if(pAgent == null || !(pAgent instanceof CCenterMarker))
			return new CMark[0];
		
		CCenterMarker pMarker = (CCenterMarker) pAgent;
		int iRadius = pMarker.getRadius();
		CLocation pLocation = pMarker.getLocation();
		
		CMark apMarks[] = CEnvironment.getInstance().getMarksInCircularArea(pLocation, iRadius);
		if(apMarks == null)
			return new CMark[0];
		else
			return apMarks;
	}
}
