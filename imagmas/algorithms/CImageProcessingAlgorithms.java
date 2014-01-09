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
 
package imagmas.algorithms;

import imagmas.data.*;

import java.awt.Color;
import java.awt.image.*;

public abstract class CImageProcessingAlgorithms
{
	public static byte[][] segmentImageByThresholding(BufferedImage pImageBuffer)
	{
		byte i;
		byte aiHistogram[] = new byte[256];
		for(i = 0; i < 256; i++)
			aiHistogram[i] = 0;

		int iX, iY;
		CPixel pPixel;
		Color pColor;
		int iWidth = pImageBuffer.getWidth();
		int iHeight = pImageBuffer.getHeight();
		short aiIntensities[][] = new short[iWidth][iHeight];
		byte aiBinaryImage[][] = new byte[iWidth][iHeight];
		
		for(iX = 0; iX < iWidth; iX++)
		{
			for(iY = 0; iY < iHeight; iY++)
			{
				pColor = new Color(pImageBuffer.getRGB(iX, iY));
				pPixel = new CPixel(new CLocation(iX, iY), pColor);
				
				aiIntensities[iX][iY] = pPixel.getBrightness(); 
				aiHistogram[aiIntensities[iX][iY]] += 1;
				aiBinaryImage[iX][iY] = 0;
			}
		}
		
		int iThreshold = 0;
		int iMinBin = Integer.MAX_VALUE;
		
		for(i = 0; i < 128; i++)
		{
			if(iMinBin > aiHistogram[i])
			{
				iMinBin = aiHistogram[i];
				iThreshold = i;
			}
		}
		
		for(iX = 0; iX < iWidth; iX++)
		{
			for(iY = 0; iY < iHeight; iY++)
			{
				if(aiIntensities[iX][iY] <= iThreshold)
					aiBinaryImage[iX][iY] = 0;
				else
					aiBinaryImage[iX][iY] = 1;
			}
		}

		return aiBinaryImage;
	}
	
	public static byte[][] segmentImageByKMeans(BufferedImage pImageBuffer, short iNumClasses)
	{
		/*
		 * Inicializa os centróides com valores distintos da imagem
		 */
		int iWidth = pImageBuffer.getWidth(), iHeight = pImageBuffer.getHeight();
		short aiCentroids[] = new short[iNumClasses];
		int iCount = -1;
		short iIdx, iClass;
		boolean bChanged;
		CPixel pPixel;
		Color pColor;
		int iX, iY;
		
		for(iX = 0; iX < iWidth; iX++)
		{
			for(iY = 0; iY < iHeight; iY++)
			{
				pColor = new Color(pImageBuffer.getRGB(iX, iY));
				pPixel = new CPixel(new CLocation(iX, iY), pColor);
				
				iIdx = pPixel.getBrightness();
				bChanged = true;
				
				// +--- Verifica se esse valor de intensidade já não foi usado por um centróide
				for(iClass = 0; iClass <= iCount; iClass++)
				{
					if(aiCentroids[iClass] == iIdx)
					{
						bChanged = false;
						break;
					}
				}
				
				// +--- Se já foi usado, procura outro...
				if(!bChanged)
					continue;
				
				// +--- Se ainda não foi usado, adiciona-o ao centróide atual, e move ao próximo.
				// +--- Encerra o laço se todos os centróides já foram inicializados.
				iCount++;
				aiCentroids[iCount] = iIdx;
				if(iCount == (iNumClasses - 1))
				{
					iX = iWidth;
					iY = iHeight;
					break;
				}
			}
		}
	
		/*
		 * Inicialmente, todos os pixels são classificados na primeira classe (primeiro centróide)
		 */
		short aiIntensities[][] = new short[iWidth][iHeight];
		short aiClusters[][] = new short[iWidth][iHeight];
		for(iX = 0; iX < iWidth; iX++)
		{
			for(iY = 0; iY < iHeight; iY++)
			{
				pColor = new Color(pImageBuffer.getRGB(iX, iY));
				pPixel = new CPixel(new CLocation(iX, iY), pColor);
				
				aiClusters[iX][iY] = 0;
				aiIntensities[iX][iY] = pPixel.getBrightness();
			}
		}
		
		/*
		 * Início da classificação
		 */
		int iMean, iDistance;
		int aiDistances[][][] = new int[iWidth][iHeight][iNumClasses];
		while(true)
		{
			/*
			 * Calculo da matriz de distâncias (euclidianas) entre cada ponto e cada centróide
			 */
			for(iX = 0; iX < iWidth; iX++)
			{
				for(iY = 0; iY < iHeight; iY++)
				{
					for(iClass = 0; iClass < iNumClasses; iClass++)
					{
						aiDistances[iX][iY][iClass] = Math.abs(aiIntensities[iX][iY] - aiCentroids[iClass]);
					}
				}
			}

			/*
			 * Classificação dos pixels aos centróides existentes, com base nas distâncias
			 */
			bChanged = false;
			for(iX = 0; iX < iWidth; iX++)
			{
				for(iY = 0; iY < iHeight; iY++)
				{
					iClass = 0;
					iDistance = aiDistances[iX][iY][0];
					for(iIdx = 0; iIdx < iNumClasses; iIdx++)
					{
						if(aiDistances[iX][iY][iIdx] < iDistance)
						{
							iClass = iIdx;
							iDistance = aiDistances[iX][iY][iIdx];
						}
					}
					if(aiClusters[iX][iY] != iClass)
					{
						aiClusters[iX][iY] = iClass;
						bChanged = true;
					}
				}
			}
			
			/*
			 * Se nenhum pixel mudou de classe, encerra a classificação
			 */
			if(!bChanged)
				break;
			
			/*
			 * Se houve mudança, refina os centróides com a média dos pixels que contêm
			 * e retorna à classificação
			 */
			for(iClass = 0; iClass < iNumClasses; iClass++)
			{
				iMean = 0;
				iCount = 0;
				
				for(iX = 0; iX < iWidth; iX++)
				{
					for(iY = 0; iY < iHeight; iY++)
					{
						if(aiClusters[iX][iY] == iClass)
						{
							iMean += aiIntensities[iX][iY];
							iCount++;
						}
					}
				}
				
				iMean /= iCount;
				aiCentroids[iClass] = (short) iMean;
			}
		}
		
		/*
		 * Ao terminar, já se tem os pixels agrupados em cada classe. Assim, cria uma imagem binária com base na
		 * seguinte regra:
		 * 
		 * As intensidades médias da imagem original são calculadas para todas as classes. Aquela que tiver a média
		 * mais baixa será classificada como objeto, tendo por isso seus pixels identificados com valor 0. Todas as
		 * demais classes terão seus pixels identificados com valor 1 (a desconsiderar).
		 */

		int aiMeans[] = new int[iNumClasses];
		int aiCounts[] = new int[iNumClasses];
		for(iClass = 0; iClass < iNumClasses; iClass++)
		{
			aiMeans[iClass] = 0;
			aiCounts[iClass] = 0;
		}
		
		for(iX = 0; iX < iWidth; iX++)
		{
			for(iY = 0; iY < iHeight; iY++)
			{
				pColor = new Color(pImageBuffer.getRGB(iX, iY));
				pPixel = new CPixel(new CLocation(iX, iY), pColor);
				
				aiMeans[aiClusters[iX][iY]] += pPixel.getBrightness();
				aiCounts[aiClusters[iX][iY]] += 1;
			}
		}
		
		short iSmallestMean = Short.MAX_VALUE;
		short iSmallestClass = 0;
		
		for(iClass = 0; iClass < iNumClasses; iClass++)
		{
			aiMeans[iClass] /= aiCounts[iClass];
			if(aiMeans[iClass] < iSmallestMean)
			{
				iSmallestMean = (short) aiMeans[iClass];
				iSmallestClass = iClass;
			}
		}
		
		byte aiRet[][] = new byte[iWidth][iHeight];
		
		for(iX = 0; iX < iWidth; iX++)
		{
			for(iY = 0; iY < iHeight; iY++)
			{
				if(aiClusters[iX][iY] == iSmallestClass)
					aiRet[iX][iY] = 0;
				else
					aiRet[iX][iY] = 1;
			}
		}
		
		return aiRet;
	}
	
	public static byte[][] getDiscStructuringElement(int iRadius)
	{
		int iDiameter = (iRadius * 2) + 1;
		byte aiRet[][] = new byte[iDiameter][iDiameter];
		
		/*
		 * Primeiramente, desenha a borda do circulo utilizando o algoritmo de ponto médio da circunferência
		 */
	    int iCX = iRadius;
	    int iCY = iRadius;
	    int iX = 0;
	    int iY = iRadius;
	    int iFunc = (5 - iRadius * 4) / 4;
	 
	    aiRet[iCX][iCY + iY] = 1;
	    aiRet[iCX][iCY - iY] = 1;
	    aiRet[iCX + iY][iCY] = 1;
	    aiRet[iCX - iY][iCY] = 1;
	 
	    while(iX < iY)
	    {
	    	iX++;
	    	if(iFunc < 0)
	    		iFunc += 2 * iX + 1;
	    	else
	    	{
	    		iY--;
	    		iFunc += 2 * (iX - iY) + 1;
	    	}
	      
	    	if(iX == iY)
	    	{
	    		aiRet[iCX + iX][iCY + iY] = 1;
	    		aiRet[iCX - iX][iCY + iY] = 1;
	    		aiRet[iCX + iX][iCY - iY] = 1;
	    		aiRet[iCX - iX][iCY - iY] = 1;
	    	}
	    	else
	    	{
	    		aiRet[iCX + iX][iCY + iY] = 1;
	    		aiRet[iCX - iX][iCY + iY] = 1;
	    		aiRet[iCX + iX][iCY - iY] = 1;
	    		aiRet[iCX - iX][iCY - iY] = 1;
	    		aiRet[iCX + iY][iCY + iX] = 1;
	    		aiRet[iCX - iY][iCY + iX] = 1;
	    		aiRet[iCX + iY][iCY - iX] = 1;
	    		aiRet[iCX - iY][iCY - iX] = 1;
	    	}
	    }
		
		/*
		 * E então, preenche o interior do círculo com base na pertinência à função da circunferência
		 * (resultado menor que zero)
		 */
		int iRadiusSq = iRadius * iRadius;
		int iRet, iDeltaX, iDeltaY;
	    
		for(iX = 0; iX < iDiameter; iX++)
		{
			for(iY = 0; iY < iDiameter; iY++)
			{
				if(aiRet[iX][iY] == 0)
				{
					iDeltaX = iX - iRadius;
					iDeltaY = iY - iRadius;
					iRet = (iDeltaX * iDeltaX) + (iDeltaY * iDeltaY) - iRadiusSq;
					if(iRet < 0)
						aiRet[iX][iY] = 1;
					else
						aiRet[iX][iY] = 0;
				}
			}
		}
		
		return aiRet;
	}
	
	public static byte[][] dilateBinaryImage(byte aiBinaryImage[][], byte aiStructElem[][])
	{
		int iWidth = aiBinaryImage.length;
		int iHeight = aiBinaryImage[0].length;
		int iX, iY;

		byte aiRet[][] = new byte[iWidth][iHeight];
		for(iX = 0; iX < iWidth; iX++)
		{
			for(iY = 0; iY < iHeight; iY++)
			{
				aiRet[iX][iY] = 0;
			}
		}

		int iWinX, iWinY, iStartX, iEndX, iStartY, iEndY, iSizeX, iSizeY;
		int iElemWidth = aiStructElem.length;
		int iElemHeight = aiStructElem[0].length;
		
		for(iX = 0; iX < iWidth; iX++)
		{
			for(iY = 0; iY < iHeight; iY++)
			{
				if(aiBinaryImage[iX][iY] == 0)
				{
					iSizeX = iElemWidth / 2;
					iStartX = iX - iSizeX;
					iEndX = iX + iSizeX;
					iSizeY = iElemHeight / 2;
					iStartY = iY - iSizeY;
					iEndY = iY + iSizeY;
					
					for(iWinX = iStartX; iWinX <= iEndX; iWinX++)
					{
						for(iWinY = iStartY; iWinY <= iEndY; iWinY++)
						{
							if(iWinX >=0 && iWinX < iWidth && iWinY >= 0 && iWinY < iHeight)
							{
								aiRet[iWinX][iWinY] = (byte) (aiRet[iWinX][iWinY] | aiStructElem[iWinX - iX + iSizeX][iWinY - iY + iSizeY]);
							}
						}
					}
				}
			}
		}

		return getInvertedBinaryImage(aiRet);
	}
	
	public static byte[][] erodeBinaryImage(byte aiBinaryImage[][], byte aiStructElem[][])
	{
		int iWidth = aiBinaryImage.length;
		int iHeight = aiBinaryImage[0].length;
		int iX, iY;

		byte aiRet[][] = new byte[iWidth][iHeight];
		for(iX = 0; iX < iWidth; iX++)
		{
			for(iY = 0; iY < iHeight; iY++)
			{
				aiRet[iX][iY] = 0;
			}
		}

		int iWinX, iWinY, iStartX, iEndX, iStartY, iEndY, iSizeX, iSizeY;
		int iElemWidth = aiStructElem.length;
		int iElemHeight = aiStructElem[0].length;
		
		for(iX = 0; iX < iWidth; iX++)
		{
			for(iY = 0; iY < iHeight; iY++)
			{
				iSizeX = iElemWidth / 2;
				iStartX = iX - iSizeX;
				iEndX = iX + iSizeX;
				iSizeY = iElemHeight / 2;
				iStartY = iY - iSizeY;
				iEndY = iY + iSizeY;
				
				boolean bOk = true;

				for(iWinX = iStartX; iWinX <= iEndX; iWinX++)
				{
					for(iWinY = iStartY; iWinY <= iEndY; iWinY++)
					{
						if(aiStructElem[iWinX - iX + iSizeX][iWinY - iY + iSizeY] == 1)
						{
							if(iWinX >=0 && iWinX < iWidth && iWinY >= 0 && iWinY < iHeight)
							{
								if(aiBinaryImage[iWinX][iWinY] == 1)
								{
									bOk = false;
									break;
								}
							}
						}
					}
					if(bOk == false)
						break;
				}
				
				if(bOk)
					aiRet[iX][iY] = (byte) (aiRet[iX][iY] | aiStructElem[iSizeX][iSizeY]);
			}
		}
		
		return getInvertedBinaryImage(aiRet);
	}
	
	public static byte[][] openBinaryImage(byte aiBinaryImage[][], byte aiStructElem[][])
	{
		return dilateBinaryImage(erodeBinaryImage(aiBinaryImage, aiStructElem), aiStructElem);
	}

	public static byte[][] closeBinaryImage(byte aiBinaryImage[][], byte aiStructElem[][])
	{
		return erodeBinaryImage(dilateBinaryImage(aiBinaryImage, aiStructElem), aiStructElem);
	}

	public static byte[][] fillHoles(byte aiBinaryImage[][], byte aiStructElem[][])
	{
		// +--- Obtem uma imagem apenas com o frame (quadro) da área original
		byte aiSeedImage[][] = getFrameImage(aiBinaryImage.length, aiBinaryImage[0].length);

		// +--- Obtem a imagem invertida da imagem original
		byte aiInverted[][] = getInvertedBinaryImage(aiBinaryImage);
		
		// +--- Propaga a imagem do frame sobre a mascara, utilizando a imagem original invertida
		byte aiLastImage[][] = null;
		do
		{
			aiLastImage = aiSeedImage;
			aiSeedImage = applyLogicalAndToBinaryImages(dilateBinaryImage(aiSeedImage, aiStructElem), aiInverted);
		}
		while(!areBinaryImagesEqual(aiLastImage, aiSeedImage));

		// +--- Retorna a inversão do resultado obtido
		return getInvertedBinaryImage(aiSeedImage);
	}
	
	public static byte[][] extractBoundaries(byte aiBinaryImage[][], byte aiStructElem[][])
	{
		// +--- Obtem as bordas desligadas dos limites da imagem, utilizando operadores morfológicos
		byte aiInBoundaries[][] = applyLogicalAndToBinaryImages(getInvertedBinaryImage(erodeBinaryImage(aiBinaryImage, aiStructElem)), aiBinaryImage);
		
		// +--- Obtem as bordas ligadas aos limites da imagem, utilizando a interseção do frame e da imagem original
		byte aiFrame[][] = getFrameImage(aiBinaryImage.length, aiBinaryImage[0].length);
		byte aiOutBoundaries[][] = applyLogicalAndToBinaryImages(aiFrame, aiBinaryImage);
		
		// +--- A imagem final é a resultante da união das duas imagens de bordas 
		byte aiRet[][] = applyLogicalOrToBinaryImages(aiInBoundaries, aiOutBoundaries);
		
		return aiRet;
	}
	
	public static byte[][] applyLogicalAndToBinaryImages(byte aiBinaryImage[][], byte aiOtherBinaryImage[][])
	{
		int iWidth = aiBinaryImage.length;
		int iHeight = aiBinaryImage[0].length;
		int iOtherWidth = aiOtherBinaryImage.length;
		int iOtherHeight = aiOtherBinaryImage[0].length;

		byte aiRet[][] = new byte[iWidth][iHeight];
		
		int iX, iY;
		for(iX = 0; iX < iWidth; iX++)
		{
			for(iY = 0; iY < iHeight; iY++)
			{
				if(iX < iOtherWidth && iY < iOtherHeight)
					if(aiBinaryImage[iX][iY] == 0 && aiOtherBinaryImage[iX][iY] == 0)
						aiRet[iX][iY] = 0;
					else
						aiRet[iX][iY] = 1;
				else
					aiRet[iX][iY] = aiBinaryImage[iX][iY];
			}
		}
		
		return aiRet;
	}
	
	public static byte[][] applyLogicalOrToBinaryImages(byte aiBinaryImage[][], byte aiOtherBinaryImage[][])
	{
		int iWidth = aiBinaryImage.length;
		int iHeight = aiBinaryImage[0].length;
		int iOtherWidth = aiOtherBinaryImage.length;
		int iOtherHeight = aiOtherBinaryImage[0].length;

		byte aiRet[][] = new byte[iWidth][iHeight];
		
		int iX, iY;
		for(iX = 0; iX < iWidth; iX++)
		{
			for(iY = 0; iY < iHeight; iY++)
			{
				if(iX < iOtherWidth && iY < iOtherHeight)
					if(aiBinaryImage[iX][iY] == 0 || aiOtherBinaryImage[iX][iY] == 0)
						aiRet[iX][iY] = 0;
					else
						aiRet[iX][iY] = 1;
				else
					aiRet[iX][iY] = aiBinaryImage[iX][iY];
			}
		}
		
		return aiRet;
	}
	
	private static boolean areBinaryImagesEqual(byte aiBinaryImage[][], byte aiOtherBinaryImage[][])
	{
		int iWidth = aiBinaryImage.length;
		int iHeight = aiBinaryImage[0].length;
		int iOtherWidth = aiOtherBinaryImage.length;
		int iOtherHeight = aiOtherBinaryImage[0].length;

		if(iWidth != iOtherWidth || iHeight != iOtherHeight)
			return false;
		
		boolean bEqual = true;
		int iX, iY;
		for(iX = 0; iX < iWidth; iX++)
		{
			for(iY = 0; iY < iHeight; iY++)
			{
				if(aiBinaryImage[iX][iY] != aiOtherBinaryImage[iX][iY])
				{
					bEqual = false;
					break;
				}
			}
			if(bEqual == false)
				break;
		}
		
		return bEqual;
	}
	
	private static byte[][] getInvertedBinaryImage(byte aiBinaryImage[][])
	{
		int iWidth = aiBinaryImage.length;
		int iHeight = aiBinaryImage[0].length;
		byte aiRet[][] = new byte[iWidth][iHeight];
		int iX, iY;
		
		for(iX = 0; iX < iWidth; iX++)
		{
			for(iY = 0; iY < iHeight; iY++)
			{
				if(aiBinaryImage[iX][iY] == 0)
					aiRet[iX][iY] = 1;
				else
					aiRet[iX][iY] = 0;
			}
		}

		return aiRet;
	}
	
	private static byte[][] getFrameImage(int iWidth, int iHeight)
	{
		int iX, iY;
		byte aiRet[][] = new byte[iWidth][iHeight];
		for(iX = 0; iX < iWidth; iX++)
		{
			for(iY = 0; iY < iHeight; iY++)
			{
				if(iX == 0 || iY == 0 || iX == iWidth - 1 || iY == iHeight - 1)
					aiRet[iX][iY] = 0;
				else
					aiRet[iX][iY] = 1;
			}
		}
		return aiRet;
	}
}
