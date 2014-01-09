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
import jadex.adapter.fipa.*;

public class CRegionDecomposer extends CAbstractAgent
{
	public CRegionDecomposer(AgentIdentifier pAgentIdentifier)
	{
		super(pAgentIdentifier);
		
		// +--- Adiciona os sensores utilizados pelo agente
		addSensor(new CImageBoundarySensor("image_boundary"));
		
		// +--- Adiciona os atuadores utilizados pelo agente
		addActuator(new CPlatformActuator("platform"));
		addActuator(new CMarkActuator("mark"));
	}
}
