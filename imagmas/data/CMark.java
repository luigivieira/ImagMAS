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

public class CMark
{
	private CLocation m_pLocation;

	private int m_iID;
	
	private static int m_iIDCount = -1;
	
	public CMark(CLocation pLocation)
	{
		m_iID = m_iIDCount + 1;
		m_iIDCount++;
		setLocation(pLocation);
	}
	
	public int getID()
	{
		return m_iID;
	}
	
	public void setLocation(CLocation pLocation)
	{
		m_pLocation = pLocation;
	}
	
	public CLocation getLocation()
	{
		return m_pLocation;
	}
	
	public static CLocation calcCentroidForMarks(CMark apMarks[])
	{
		if(apMarks == null || apMarks.length == 0)
			return null;
		
		int i, iX = 0, iY = 0;
		
		for(i = 0; i < apMarks.length; i++)
		{
			iX += apMarks[i].getLocation().getX();
			iY += apMarks[i].getLocation().getY();
		}
		
		iX /= apMarks.length;
		iY /= apMarks.length;
		
		return new CLocation(iX, iY);
	}
	
	@Override
	public String toString()
	{
		return "M:" + m_pLocation.toString();
	}
	
	@Override
	public boolean equals(Object pObject)
	{
		if(pObject == null || !(pObject instanceof CMark))
			return false;
		
		return ((CMark) pObject).getLocation().equals(m_pLocation);
	}
}
