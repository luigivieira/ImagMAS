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

import java.awt.*;

/**
 * Classe que representa um pixel da imagem carregada no ambiente, em termos de seus
 * valores de tonalidade de cinza (brilho) e localiza��o espacial.
 * 
 * @author Luiz Carlos Vieira
 */

public class CPixel
{
	/* ******************************************************************************************
	 * Atributos privados da classe
	 ****************************************************************************************** */
	
	/**
	 * Localiza��o do pixel na imagem.
	 */
	private CLocation m_pLocation;
	
	/**
	 * Brilho do pixel.
	 */
	private short m_iBrightness;
	
	/* ******************************************************************************************
	 * Construtores, m�todos de inicializa��o e acesso, e m�todos p�blicos de uso geral
	 ****************************************************************************************** */

	/**
	 * Construtor que toma como entrada um objeto Color do Java para a constru��o de um pixel.
	 * @param pLocation Objeto IMLocation com a localiza��o do pixel na imagem.
	 * @param pColor Objeto Color com a cor do pixel.
	 */
	public CPixel(CLocation pLocation, Color pColor)
	{
		setLocation(pLocation);
		updateBrightnessFromColor(pColor);
	}
	
	/**
	 * M�todo getter para obten��o da localiza��o do pixel na imagem.
	 * @return Objeto IMLocation com a localiza��o do pixel na imagem.
	 */
	public CLocation getLocation()
	{
		return m_pLocation;
	}
	
	/**
	 * M�todo setter para altera��o da localiza��o do pixel na imagem.
	 * @param pLocation Objeto IMLocation com a localiza��o do pixel na imagem.
	 */
	public void setLocation(CLocation pLocation)
	{
		m_pLocation = pLocation;
	}

	/**
	 * M�todo setter para altera��o da tonalidade de cinza (brilho) do pixel.
	 * @param iBrightness Valor inteiro entre 0 e 255 com a tonalidade de cinza do pixel.
	 */
	public void setBrightness(short iBrightness)
	{
		if(iBrightness > 255)
			m_iBrightness = (short) (iBrightness % 255);
		else if(iBrightness < 0)
			m_iBrightness = 0;
		else
			m_iBrightness = iBrightness;
	}

	/**
	 * M�todo getter para a obten��o da tonalidade de cinza (brilho) do pixel.
	 * @return Valor inteiro entre 0 e 255 com a tonalidade de cinza do pixel. 
	 */
	public short getBrightness()
	{
		return m_iBrightness;
	}

	/**
	 * M�todo de compara��o utilizado para verificar se uma outra inst�ncia de objeto
	 * IMPixel representa o mesmo pixel que a atual. � empregado automaticamente
	 * por listas e mapas da linguagem java para compara��o em opera��es de adi��o e remo��o,
	 * e tamb�m pode ser utilizado diretamente para rapidamente comparar dois pixels.
	 * 
	 * @param pData Objeto da classe Object. Deve conter uma abstra��o de objeto da classe IMPixel,
	 * caso contr�rio a fun��o retornar� sempre falso.
	 * @return Retorna verdadeiro se o objeto pixel informado cont�m a mesma localidade que o objeto
	 * atual. Caso contr�rio, retorna falso.
	 */
	@Override
	public boolean equals(Object pData)
	{
		if(pData != null && pData instanceof CPixel)
		{
			CPixel pPixel = (CPixel) pData;
			return pPixel.getLocation().equals(m_pLocation);
		}
		else
			return false;
	}
	
	
	/**
	 * M�todo p�blico para obten��o de uma string de representa��o do pixel.
	 * Utilizado para permitir a f�cil impress�o de mensagens de depura��o
	 * com chamadas de System.out.println, por exemplo. A string retornada segue
	 * o formato "(x,y) #?", com as coordenadas da localiza��o e o valor de tonalidade
	 * de cinza (brilho) do pixel.
	 * @return String de representa��o do pixel.
	 */
	@Override
	public String toString()
	{
		return m_pLocation.toString() + " #" + m_iBrightness;
	}
	
	/* ******************************************************************************************
	 * M�todos privados da classe
	 ****************************************************************************************** */
	
	/**
	 * M�todo auxiliar para atualiza��o da tonalidade de cinza (brilho) do pixel a partir
	 * de um objeto Color do JAVA.
	 * @param pColor Objeto Color do JAVA para atualiza��o do pixel.
	 */
	private void updateBrightnessFromColor(Color pColor)
	{
		int iRed = pColor.getRed();
		int iGreen = pColor.getGreen();
		int iBlue = pColor.getBlue();
		int iMin = Math.min(Math.min(iRed, iGreen), iBlue);
		int iMax = Math.max(Math.max(iRed, iGreen), iBlue);

		m_iBrightness = (short) ((iMax + iMin) / 2);
	}
}
