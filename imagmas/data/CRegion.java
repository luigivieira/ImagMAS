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

import java.util.*;

/**
 * Classe que representa uma regi�o da imagem do ambiente do sistema (ou seja, um conjunto
 * de pixels).
 * 
 * @author Luiz Carlos Vieira
 */
public class CRegion
{
	/* ******************************************************************************************
	 * Atributos privados da classe
	 ****************************************************************************************** */

	/**
	 * Lista de pixels contidos na regi�o.
	 */
	private ArrayList<CPixel> m_apPixels;
	
	/**
	 * Lista de pixels que tamb�m est�o nas bordas da regi�o.
	 */
	private ArrayList<CPixel> m_apBoundaryPixels;
	
	/**
	 * �rea retangular da imagem em que est� contida a regi�o.
	 */
	private CRectangle m_pRectangle;
	
	/**
	 * ID da regi�o.
	 */
	private int m_iID;
	
	/**
	 * Contador est�tico para incremento automatico do ID da regi�o.
	 */
	private static int m_iIDCounter = -1;
	
	/* ******************************************************************************************
	 * Construtores, m�todos de inicializa��o e acesso, e m�todos p�blicos de uso geral
	 ****************************************************************************************** */
	
	/**
	 * Construtor padr�o da classe.
	 */
	public CRegion()
	{
		m_iID = m_iIDCounter + 1;
		m_iIDCounter++;
		
		m_apPixels = new ArrayList<CPixel>();
		m_apBoundaryPixels = new ArrayList<CPixel>();
		m_pRectangle = new CRectangle(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
	}
	
	public int getID()
	{
		return m_iID;
	}
	
	public CRectangle getRectangle()
	{
		return m_pRectangle;
	}
	
	/**
	 * M�todo para consulta se um pixel est� contido na regi�o.
	 * @param pPixel Objeto CPixel com o pixel a ser consultado.
	 * @return Retorna True se o pixel est� contido na regi�o, ou false caso contr�rio.
	 */
	public boolean contains(CPixel pPixel)
	{
		return m_apPixels.contains(pPixel);
	}
	
	/**
	 * M�todo para consulta se um pixel faz parte das fronteiras da regi�o.
	 * @param pPixel Objeto CPixel com o pixel a ser consultado.
	 * @return Retorna True se o pixel faz parte da fronteira da regi�o, ou false caso n�o o fa�a
	 * ou n�o exista na regi�o.
	 */
	public boolean isInBoundary(CPixel pPixel)
	{
		return m_apBoundaryPixels.contains(pPixel);
	}
	
	/**
	 * M�todo getter para obten��o da lista de pixels contidos na regi�o.
	 * @return Objeto ArrayList contendo as inst�ncias de CPixel com os pixels da regi�o.
	 */
	public ArrayList<CPixel> getPixelList()
	{
		return m_apPixels;
	}

	/**
	 * M�todo getter para obten��o da lista de localidades dos pixels contidos na regi�o.
	 * @return Objeto ArrayList contendo as inst�ncia de CLocation com as localidades dos pixels da regi�o.
	 */
	public ArrayList<CLocation> getLocationList()
	{
		ArrayList<CLocation> apRet = new ArrayList<CLocation>();
		
		CLocation pLocation;
		Iterator<CPixel> pIt = m_apPixels.iterator();
		while(pIt.hasNext())
		{
			pLocation = pIt.next().getLocation();
			apRet.add(pLocation);
		}
		
		return apRet;
	}
	
	/**
	 * M�todo getter para obten��o da lista de pixels que fazem parte das fronteiras da regi�o.
	 * @return Objeto ArrayList contendo as inst�ncias de CPixel com os pixels das fronteiras da regi�o.
	 */
	public ArrayList<CPixel> getBoundaryPixelList()
	{
		return m_apBoundaryPixels;
	}
	
	/**
	 * M�todo para adicionar um pixel � regi�o.
	 * @param pPixel Objeto CPixel para ser adicionado � regi�o.
	 */
	public void addPixel(CPixel pPixel, boolean bIsBoundary)
	{
		m_apPixels.add(pPixel);
		if(bIsBoundary)
			m_apBoundaryPixels.add(pPixel);
		
		// +--- Expande a �rea retangular de conten��o da regi�o
		int iTopX, iTopY, iBottomX, iBottomY;
		iTopX = m_pRectangle.getTopLeftLocation().getX();
		iTopY = m_pRectangle.getTopLeftLocation().getY();
		iBottomX = m_pRectangle.getBottomRightLocation().getX();
		iBottomY = m_pRectangle.getBottomRightLocation().getY();
		
		if(pPixel.getLocation().getX() < iTopX)
			iTopX = pPixel.getLocation().getX();
		if(pPixel.getLocation().getY() < iTopY)
			iTopY = pPixel.getLocation().getY();
		if(pPixel.getLocation().getX() > iBottomX)
			iBottomX = pPixel.getLocation().getX();
		if(pPixel.getLocation().getY() > iBottomY)
			iBottomY = pPixel.getLocation().getY();
		
		m_pRectangle = new CRectangle(iTopX, iTopY, iBottomX, iBottomY);
	}
	
	/**
	 * M�todo para remover um pixel da regi�o.
	 * @param pPixel Objeto CPixel com o pixel a ser removido da regi�o.
	 */
	public void removePixel(CPixel pPixel)
	{
		m_apPixels.remove(pPixel);
		m_apBoundaryPixels.remove(pPixel);
	}
	
	public void removeAllPixels()
	{
		m_apPixels.clear();
		m_apBoundaryPixels.clear();
	}
	
	/**
	 * M�todo getter para busca de um pixel na regi�o com base em uma localidade informada.
	 * @param pLocation Objeto IMLocation com a localiza��o do pixel a ser buscado. 
	 * @return Retorna um objeto CPixel com o pixel encontrado ou null se a regi�o
	 * n�o cont�m um pixel com a localidade informada.
	 */
	public CPixel getPixelAt(CLocation pLocation)
	{
		CPixel pPixel = null;
		Iterator pIt = m_apPixels.iterator();
		while(pIt.hasNext())
		{
			pPixel = (CPixel) pIt.next();
			if(pPixel.getLocation().equals(pLocation))
				return pPixel;
		}
		return null;
	}
	
	/**
	 * M�todo getter para obten��o do n�mero de pixels contidos na regi�o.
	 * @return Valor inteiro com o n�mero de pixels contidos na regi�o.
	 */
	public int getPixelCount()
	{
		return m_apPixels.size();
	}
	
	/**
	 * M�todo getter para obten��o do n�mero de pixels nas fronteiras da regi�o.
	 * @return Valor inteiro com o n�mero de pixels nas fronteiras da regi�o.
	 */
	public int getBoundaryPixelCount()
	{
		return m_apBoundaryPixels.size();
	}

	/**
	 * M�todo de compara��o utilizado para verificar se uma outra inst�ncia de objeto
	 * IMRegion representa a mesma regi�o que a atual. � empregado automaticamente
	 * por listas e mapas da linguagem java para compara��o em opera��es de adi��o e remo��o,
	 * e tamb�m pode ser utilizado diretamente para rapidamente comparar duas regi�es.
	 * 
	 * @param pData Objeto da classe Object. Deve conter uma abstra��o de objeto da classe IMRegion,
	 * caso contr�rio a fun��o retornar� sempre falso.
	 * @return Retorna verdadeiro se o objeto de regi�o informado cont�m os mesmos pixels que o objeto atual.
	 * Caso contr�rio, retorna falso.
	 */
	@Override
	public boolean equals(Object pData)
	{
		if(pData != null && pData instanceof CRegion)
		{
			CRegion pRegion = (CRegion) pData;
			return pRegion.m_apPixels.equals(m_apPixels);
		}
		else
			return false;
	}
	
	/**
	 * M�todo p�blico para obten��o de uma string de representa��o da regi�o.
	 * Utilizado para permitir a f�cil impress�o de mensagens de depura��o
	 * com chamadas de System.out.println, por exemplo. A string retornada segue
	 * o formato "#N [(x1,y1) #???, (x2,y2) #???, ..., (xN,yN) #???]", com a quantidade de pixels contidos
	 * na regi�o seguida pela lista de todos os seus pixels
	 * 'y'.
	 * @return String de representa��o da regi�o.
	 */
	@Override
	public String toString()
	{
		boolean bFirstTime = true;
		String sReturn = "ID: " + m_iID + " #" + m_apPixels.size() + " [";
		
		CPixel pPixel = null;
		Iterator pIt = m_apPixels.iterator();
		while(pIt.hasNext())
		{
			if(bFirstTime)
				bFirstTime = false;
			else
				sReturn += ", ";
			
			pPixel = (CPixel) pIt.next();
			sReturn += pPixel.toString();
			
			if(isInBoundary(pPixel))
				sReturn += " |b|";
		}
		
		sReturn += "]";
		return  sReturn;
	}
	
	/* ******************************************************************************************
	 * M�todos privados da classe
	 ****************************************************************************************** */
}
