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

public class CRectangle
{
	private CLocation m_pTopLeftLocation;
	
	private CLocation m_pBottomRightLocation;
	
	public CRectangle()
	{
		m_pTopLeftLocation = new CLocation();
		m_pBottomRightLocation = new CLocation();
	}
	
	public CRectangle(int iTopX, int iTopY, int iBottomX, int iBottomY)
	{
		m_pTopLeftLocation = new CLocation(iTopX, iTopY);
		m_pBottomRightLocation = new CLocation(iBottomX, iBottomY);
	}
	
	public void setTopLeftLocation(CLocation pLocation)
	{
		m_pTopLeftLocation = pLocation;
	}
	
	public CLocation getTopLeftLocation()
	{
		return m_pTopLeftLocation;
	}

	public void setBottomRightLocation(CLocation pLocation)
	{
		m_pBottomRightLocation = pLocation;
	}
	
	public CLocation getBottomRightLocation()
	{
		return m_pBottomRightLocation;
	}
	
	public int getWidth()
	{
		return m_pBottomRightLocation.getX() - m_pTopLeftLocation.getX() + 1;
	}

	public int getHeight()
	{
		return m_pBottomRightLocation.getY() - m_pTopLeftLocation.getY() + 1;
	}
}
