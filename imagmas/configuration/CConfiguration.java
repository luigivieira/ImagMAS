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
 
package imagmas.configuration;

public abstract class CConfiguration
{
	private static int m_iMinimumRadius = 8;
	
	private static int m_iMaximumRadius = 28;
	
	private static int m_iMarksDistance = 5;
	
	private static double m_dInclusionFactor = 0.5;
	
	private static double m_dDifferentiationFactor = 0.9;
	
	public static int getMinimumRadius()
	{
		return m_iMinimumRadius;
	}
	
	public static void setMinimumRadius(int iValue)
	{
		m_iMinimumRadius = iValue;
	}
	
	public static int getMaximumRadius()
	{
		return m_iMaximumRadius;
	}
	
	public static void setMaximumRadius(int iValue)
	{
		m_iMaximumRadius = iValue;
	}
	
	public static int getMarksDistance()
	{
		return m_iMarksDistance;
	}
	
	public static void setMarksDistance(int iValue)
	{
		m_iMarksDistance = iValue;
	}

	public static double getInclusionFactor()
	{
		return m_dInclusionFactor;
	}
	
	public static void setInclusionFactor(double dValue)
	{
		m_dInclusionFactor = dValue;
	}

	public static double getDifferentiationFactor()
	{
		return m_dDifferentiationFactor;
	}
	
	public static void setDifferentiationFactor(double dValue)
	{
		m_dDifferentiationFactor = dValue;
	}
}
