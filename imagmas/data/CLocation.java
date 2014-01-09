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

/**
 * Classe que representa uma localiza��o (par de coordenadas) no plano espacial
 * do ambiente do sistema.
 * 
 * @author Luiz Carlos Vieira
 */
public class CLocation
{
	/**
	 * Atributo para armazenamento da coordenada x.
	 */
	private int m_iX;
	
	/**
	 * Atributo para armazenamento da coordenada y.
	 */
	private int m_iY;
	
	/**
	 * Construtor padr�o. Cria uma localidade com coordenadas-padr�o em (0,0).
	 */
	public CLocation()
	{
		m_iX = 0;
		m_iY = 0;
	}

	/**
	 * Construtor alternativo. Cria uma localidade com as coordenadas informadas.
	 * 
	 * @param iX Valor inteiro para a coordenada x.
	 * @param iY Valor inteiro para a coordenada y.
	 */
	public CLocation(int iX, int iY)
	{
		m_iX = iX;
		m_iY = iY;
	}
	
	/**
	 * M�todo setter para permitir a altera��o da coordenada x.
	 * @param iValue Valor inteiro com a nova coordenada x.
	 */
	public void setX(int iValue)
	{
		m_iX = iValue;
	}
	
	/**
	 * M�todo getter para permitir a obten��o da coordenada x.
	 * @return Valor inteiro com a coordenada x.
	 */
	public int getX()
	{
		return m_iX;
	}

	/**
	 * M�todo setter para permitir a altera��o da coordenada y.
	 * @param iValue Valor inteiro com a nova coordenada y.
	 */
	public void setY(int iValue)
	{
		m_iY = iValue;
	}
	
	/**
	 * M�todo getter para permitir a obten��o da coordenada y.
	 * @return Valor inteiro com a coordenada y.
	 */
	public int getY()
	{
		return m_iY;
	}
	
	/**
	 * Calcula a dist�ncia euclidiana da localiza��o atual at� a localiza��o dada.
	 * @param pLocation Objeto CLocation com a localiza��o at� a qual a dist�ncia dever� ser calculada.
	 * @return Valor double com a dist�ncia calculada.
	 */
	public double calcDistanceFrom(CLocation pLocation)
	{
		if(pLocation == null)
			return 0;
		
		double dDeltaX = m_iX - pLocation.getX();
		double dDeltaY = m_iY - pLocation.getY();
		
		return Math.sqrt((dDeltaX * dDeltaX) + (dDeltaY * dDeltaY));
	}
	
	/**
	 * Calcula a dire��o em que a localidade dada se encontra da localidade atual.
	 * @param pLocation Objeto IMLocation com a localiza��o para avalia��o da dire��o.
	 * @return Enumera��o IMDirection com a dire��o em que pLocation se encontra da localiza��o atual.
	 */
	public EDirection calcDirectionTo(CLocation pLocation)
	{
		if(pLocation == null)
			return EDirection.UNDEFINED;
		
		int iDeltaX = pLocation.getX() - m_iX;
		int iDeltaY = pLocation.getY() - m_iY;
		
		int iQuadrant;
		double dDistance = calcDistanceFrom(pLocation);
		double dOpposedCathetus, dHypothenuse = Math.abs(dDistance);
		
		// Checa os casos b�sicos, em que um dos deltas � zero ou em que os dois s�o zero.
		if(iDeltaX == 0)
		{
			if(iDeltaY == 0)
				return EDirection.UNDEFINED;
			else if(iDeltaY > 0)
				return EDirection.SOUTH;
			else // iDeltaY < 0
				return EDirection.NORTH;
		}
		else if(iDeltaY == 0)
		{
			if(iDeltaX > 0)
				return EDirection.EAST;
			else // iDeltaX > 0
				return EDirection.WEST;
		}
				
		// Para os demais casos, verifica o quadrante e calcula o �ngulo local. Ent�o, calcula o �ngulo
		// geral a partir da informa��o do quadrante.
		if(iDeltaX > 0)
		{
			if(iDeltaY < 0) // 1� Quadrante
			{
				iQuadrant = 0;
				dOpposedCathetus = Math.abs(iDeltaY);
			}
			else //iDeltaY > 0 // 4� Quadrante
			{
				iQuadrant = 3;
				dOpposedCathetus = Math.abs(iDeltaX);
			}
		}
		else // DeltaX < 0
		{
			if(iDeltaY < 0) // 2� Quadrante
			{
				iQuadrant = 1;
				dOpposedCathetus = Math.abs(iDeltaX);
			}
			else // iDeltaY > 0 // 3� Quadrante
			{
				iQuadrant = 2;
				dOpposedCathetus = Math.abs(iDeltaY);
			}
		}
		
		double dLocalAngle = Math.asin(dOpposedCathetus/dHypothenuse);
		double dGlobalAngle = (iQuadrant * (Math.PI / 2)) + dLocalAngle;

		// Finalmente, calcula os setores em uma divis�o do c�rculo em 16 partes (como uma pizza de 16 peda�os).
		// Com base nesse "setor" calculado, escolhe a melhor dire��o.
		double dSlice = Math.PI / 8; // 16 partes = 2PI/16
		int iSector = (int) (dGlobalAngle / dSlice);
		if((dGlobalAngle % dSlice) > 0) // Inclui o setor seguinte se sobrou resto na divis�o
			iSector++;
		
		// Escolhe a dire��o a partir do setor (crescente do eixo X positivo, em sentido anti-hor�rio
		switch(iSector)
		{
			case 16:
			case 1:
				return EDirection.EAST;
				
			case 2:
			case 3:
				return EDirection.NORTHEAST;
				
			case 4:
			case 5:
				return EDirection.NORTH;

			case 6:
			case 7:
				return EDirection.NORTHWEST;

			case 8:
			case 9:
				return EDirection.WEST;

			case 10:
			case 11:
				return EDirection.SOUTHWEST;

			case 12:
			case 13:
				return EDirection.SOUTH;
				
			default: // 14 e 15
				return EDirection.SOUTHEAST;
		}
	}
	
	/**
	 * Calcula uma localidade vizinha a partir da dire��o dada.
	 * @param eDir Enumera��o EDirection com a dire��o para verifica��o de vizinhan�a. 
	 * @return Objeto CLocation com a localidade vizinha.
	 */
	public CLocation calcNeighbourLocationAt(EDirection eDir)
	{
		CLocation pRet;
		switch(eDir.getAngle())
		{
			case 0: // EAST
				pRet = new CLocation(m_iX + 1, m_iY);
				break;
				
			case 45: // NORTHEAST
				pRet = new CLocation(m_iX + 1, m_iY - 1);
				break;
				
			case 90: // NORTH
				pRet = new CLocation(m_iX, m_iY - 1);
				break;
				
			case 135: // NORTHWEST
				pRet = new CLocation(m_iX - 1, m_iY - 1);
				break;

			case 180: // WEST
				pRet = new CLocation(m_iX - 1, m_iY);
				break;
				
			case 225: // SOUTHWEST
				pRet = new CLocation(m_iX - 1, m_iY + 1);
				break;

			case 270: // SOUTH
				pRet = new CLocation(m_iX, m_iY + 1);
				break;

			case 315: // SOUTHEAST
				pRet = new CLocation(m_iX + 1, m_iY + 1);
				break;
				
			default: // UNDEFINED
				pRet = new CLocation(m_iX, m_iY); // A pr�pria
				break;
		}
		
		return pRet;
	}
	
	public boolean isNeighbourOf(CLocation pLocation)
	{
		if(pLocation == null)
			return false;
		
		int iDeltaX = Math.abs(pLocation.getX() - m_iX);
		int iDeltaY = Math.abs(pLocation.getY() - m_iY);
		
		return (iDeltaX + iDeltaY) <= 2;
	}
	
	public static CLocation calcCentroidLocation(CLocation apLocations[])
	{
		if(apLocations == null || apLocations.length == 0)
			return null;
		
		int i, iX = 0, iY = 0;
		
		for(i = 0; i < apLocations.length; i++)
		{
			iX += apLocations[i].getX();
			iY += apLocations[i].getY();
		}
		
		iX /= apLocations.length;
		iY /= apLocations.length;
		
		return new CLocation(iX, iY);
	}
	
	/**
	 * M�todo de compara��o utilizado para verificar se uma outra inst�ncia de objeto
	 * IMLocation representa a mesma localiza��o do que a atual. � empregado automaticamente
	 * por listas e mapas da linguagem java para compara��o em opera��es de adi��o e remo��o,
	 * e tamb�m pode ser utilizado diretamente para rapidamente comparar duas localidades.
	 * 
	 * @param pData Objeto da classe Object. Deve conter uma abstra��o de objeto da classe IMLocation,
	 * caso contr�rio a fun��o retornar� sempre falso.
	 * @return Retorna verdadeiro se o objeto de localidade informado cont�m as mesmas coordenadas
	 * que o objeto atual. Caso contr�rio, retorna falso.
	 */
	@Override
	public boolean equals(Object pData)
	{
		if(pData != null && pData instanceof CLocation)
		{
			CLocation pLoc = (CLocation) pData;
			return pLoc.getX() == m_iX && pLoc.getY() == m_iY;
		}
		else
			return false;
	}
	
	/**
	 * M�todo p�blico para obten��o de uma string de representa��o da localidade.
	 * Utilizado para permitir a f�cil impress�o de mensagens de depura��o
	 * com chamadas de System.out.println, por exemplo. A string retornada segue
	 * o formato "(x,y)", com os valores das coordenadas nos lugares de 'x' e de
	 * 'y'.
	 * @return String de representa��o da localidade.
	 */
	@Override
	public String toString()
	{
		return "(" + m_iX + "," + m_iY + ")";
	}
}