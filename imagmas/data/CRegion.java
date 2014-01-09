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
 * Classe que representa uma região da imagem do ambiente do sistema (ou seja, um conjunto
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
	 * Lista de pixels contidos na região.
	 */
	private ArrayList<CPixel> m_apPixels;
	
	/**
	 * Lista de pixels que também estão nas bordas da região.
	 */
	private ArrayList<CPixel> m_apBoundaryPixels;
	
	/**
	 * Área retangular da imagem em que está contida a região.
	 */
	private CRectangle m_pRectangle;
	
	/**
	 * ID da região.
	 */
	private int m_iID;
	
	/**
	 * Contador estático para incremento automatico do ID da região.
	 */
	private static int m_iIDCounter = -1;
	
	/* ******************************************************************************************
	 * Construtores, métodos de inicialização e acesso, e métodos públicos de uso geral
	 ****************************************************************************************** */
	
	/**
	 * Construtor padrão da classe.
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
	 * Método para consulta se um pixel está contido na região.
	 * @param pPixel Objeto CPixel com o pixel a ser consultado.
	 * @return Retorna True se o pixel está contido na região, ou false caso contrário.
	 */
	public boolean contains(CPixel pPixel)
	{
		return m_apPixels.contains(pPixel);
	}
	
	/**
	 * Método para consulta se um pixel faz parte das fronteiras da região.
	 * @param pPixel Objeto CPixel com o pixel a ser consultado.
	 * @return Retorna True se o pixel faz parte da fronteira da região, ou false caso não o faça
	 * ou não exista na região.
	 */
	public boolean isInBoundary(CPixel pPixel)
	{
		return m_apBoundaryPixels.contains(pPixel);
	}
	
	/**
	 * Método getter para obtenção da lista de pixels contidos na região.
	 * @return Objeto ArrayList contendo as instâncias de CPixel com os pixels da região.
	 */
	public ArrayList<CPixel> getPixelList()
	{
		return m_apPixels;
	}

	/**
	 * Método getter para obtenção da lista de localidades dos pixels contidos na região.
	 * @return Objeto ArrayList contendo as instância de CLocation com as localidades dos pixels da região.
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
	 * Método getter para obtenção da lista de pixels que fazem parte das fronteiras da região.
	 * @return Objeto ArrayList contendo as instâncias de CPixel com os pixels das fronteiras da região.
	 */
	public ArrayList<CPixel> getBoundaryPixelList()
	{
		return m_apBoundaryPixels;
	}
	
	/**
	 * Método para adicionar um pixel à região.
	 * @param pPixel Objeto CPixel para ser adicionado à região.
	 */
	public void addPixel(CPixel pPixel, boolean bIsBoundary)
	{
		m_apPixels.add(pPixel);
		if(bIsBoundary)
			m_apBoundaryPixels.add(pPixel);
		
		// +--- Expande a área retangular de contenção da região
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
	 * Método para remover um pixel da região.
	 * @param pPixel Objeto CPixel com o pixel a ser removido da região.
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
	 * Método getter para busca de um pixel na região com base em uma localidade informada.
	 * @param pLocation Objeto IMLocation com a localização do pixel a ser buscado. 
	 * @return Retorna um objeto CPixel com o pixel encontrado ou null se a região
	 * não contém um pixel com a localidade informada.
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
	 * Método getter para obtenção do número de pixels contidos na região.
	 * @return Valor inteiro com o número de pixels contidos na região.
	 */
	public int getPixelCount()
	{
		return m_apPixels.size();
	}
	
	/**
	 * Método getter para obtenção do número de pixels nas fronteiras da região.
	 * @return Valor inteiro com o número de pixels nas fronteiras da região.
	 */
	public int getBoundaryPixelCount()
	{
		return m_apBoundaryPixels.size();
	}

	/**
	 * Método de comparação utilizado para verificar se uma outra instância de objeto
	 * IMRegion representa a mesma região que a atual. É empregado automaticamente
	 * por listas e mapas da linguagem java para comparação em operações de adição e remoção,
	 * e também pode ser utilizado diretamente para rapidamente comparar duas regiões.
	 * 
	 * @param pData Objeto da classe Object. Deve conter uma abstração de objeto da classe IMRegion,
	 * caso contrário a função retornará sempre falso.
	 * @return Retorna verdadeiro se o objeto de região informado contém os mesmos pixels que o objeto atual.
	 * Caso contrário, retorna falso.
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
	 * Método público para obtenção de uma string de representação da região.
	 * Utilizado para permitir a fácil impressão de mensagens de depuração
	 * com chamadas de System.out.println, por exemplo. A string retornada segue
	 * o formato "#N [(x1,y1) #???, (x2,y2) #???, ..., (xN,yN) #???]", com a quantidade de pixels contidos
	 * na região seguida pela lista de todos os seus pixels
	 * 'y'.
	 * @return String de representação da região.
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
	 * Métodos privados da classe
	 ****************************************************************************************** */
}
