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
 
package imagmas.environment;

import imagmas.agents.*;
import imagmas.algorithms.*;
import imagmas.data.*;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.imageio.*;

/**
 * Classe para representação do ambiente de observação e atuação dos agentes (o ambiente
 * de execução é a plataforma JADE). Permite a observação das intensidades dos pixels
 * 
 * @author Luiz Carlos Vieira
 */
public class CEnvironment
{
	/* ******************************************************************************************
	 * Atributos privados
	 ****************************************************************************************** */
	
	/**
	 * Instância única da classe IMEnvironment (singleton).
	 */
	private static CEnvironment m_pDefaultInstance = null;
	
	/**
	 * Dados originais da imagem carregada no ambiente.
	 */
	private BufferedImage m_pImageBuffer;

	/**
	 * Dados da imagem binária contendo apenas as bordas (em zero) da imagem original.
	 */
	private byte m_aiBoundaryBinaryImage[][];
	
	/**
	 * Dados da imagem binária contendo apenas os pixels de região (em zero) da imagem original.
	 */
	private byte m_aiRegionBinaryImage[][];
	
	/**
	 * Matriz bidimensional com o posicionamento de agentes no ambiente.
	 */
	private CAbstractAgent m_apAgentMap[][];

	/**
	 * Matriz bidimensional com a identificação dos pixels e suas regiões no ambiente.
	 */
	private CRegion m_apRegionMap[][];

	/**
	 * Matriz bidimensional com as marcas de borda no ambiente.
	 */
	private CMark m_apMarkMap[][];
	
	/**
	 * Mapa de localização de agentes posicionados no ambiente.
	 */
	private HashMap<String, CLocation> m_mpAgentPosition;
	
	/**
	 * Mapa de localização de marcas posicionadas no ambiente.
	 */
	private HashMap<Integer, CLocation> m_mpMarkPosition;
	
	/**
	 * Lista das regiões produzidas na imagem.
	 */
	private ArrayList<CRegion> m_apRegions;
	
	/**
	 * Soluções encontradas por região.
	 */
	private HashMap<CRegion, ArrayList<CCrater>> m_mpSolutions;

	/* ******************************************************************************************
	 * Construtores e métodos de inicialização
	 ****************************************************************************************** */
	
	/**
	 * Construtor da classe, responsável por inicializar as propriedades do ambiente. É definido como protegido
	 * (protected) pois a classe é um singleton. Ou seja, todos os seus métodos devem ser acessados em uma única
	 * instância, obtida por meio do método getInstance().
	 * Obs.: O ambiente apenas estará pronto para a execução de agentes após o carregamento de uma imagem por
	 * meio de um dos métodos loadLocalImage ou loadWebImage.
	 */
	protected CEnvironment()
	{
		emptyEnvironmentDataStructures();
	}

	/**
	 * Método estático público para acesso à instância única da classe de ambiente. 
	 * @return Objeto IMEnvironment com a instância única do ambiente para todo o sistema.
	 */
	public static CEnvironment getInstance()
	{
		if(m_pDefaultInstance == null)
			m_pDefaultInstance = new CEnvironment();
		
		return m_pDefaultInstance;
	}
	
	/* ******************************************************************************************
	 * Métodos públicos
	 ****************************************************************************************** */
	
	/**
	 * Método para a obtenção de uma cópia da imagem original. Utilizado exclusivamente pela interface gráfica
	 * para apresentação ao usuário.
	 * @return Objeto BufferedImage com uma cópia da imagem original do ambiente.
	 */
	public synchronized BufferedImage getSourceImage()
	{
		if(m_pImageBuffer == null)
			return null;
		
		BufferedImage pImage = new BufferedImage(m_pImageBuffer.getWidth(), m_pImageBuffer.getHeight(), m_pImageBuffer.getType());
		m_pImageBuffer.copyData(pImage.getRaster());
		return pImage;
	}
	
	/**
	 * Método para a obtenção de uma imagem de representação gráfica do estado da segmentação em execução no ambiente.
	 * @return Objeto BufferedImage com a imagem de representação do estado da segmentação em execução.
	 */
	public synchronized BufferedImage genSegmentedRepresentation(boolean bShowOriginalImage, boolean bShowRegions, boolean bShowMarks, boolean bShowAgents, boolean bShowCraters)
	{
		if(m_pImageBuffer == null)
			return null;
		
		// +--- Cria a imagem de retorno com os mesmos atributos da imagem original.
		BufferedImage pImage = new BufferedImage(m_pImageBuffer.getWidth(), m_pImageBuffer.getHeight(), m_pImageBuffer.getType());
		WritableRaster pRaster = pImage.getRaster();			
		
		// +--- Se foi solicitada a inclusão da imagem original, copia seus dados antes de continuar. 
		if(bShowOriginalImage)
			m_pImageBuffer.copyData(pRaster);
		
		int iX, iY;
		int iWidth = m_pImageBuffer.getWidth();
		int iHeight = m_pImageBuffer.getHeight();
		
		CCenterMarker pMarker;
		Graphics2D pG2d = pImage.createGraphics();
		//float faDist[] = { 6.0f };
		//BasicStroke pDashedStroke = new BasicStroke(0.2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 2.0f, faDist, 0.0f);		
		
		int aiRegionColor[] = {0, 255, 0}; // Verde
		int aiMarkColor[] = {255, 255, 255}; // Branco
		int aiAgentColor[] = {255, 0, 0}; // Vermelho
		int aiCraterColor[] = {255, 255, 0}; // Amarelo

		pG2d.setColor(new Color(aiAgentColor[0], aiAgentColor[1], aiAgentColor[2]));
		//pG2d.setStroke(pDashedStroke);
		
		for(iX = 0; iX < iWidth; iX++)
		{
			for(iY = 0; iY < iHeight; iY++)
			{
				// +--- Se foi solicitada a inclusão das regiões, desenha os pixels contidos em regiões
				if(bShowRegions && m_apRegionMap[iX][iY] != null)
					pRaster.setPixel(iX, iY, aiRegionColor);

				// +--- Se foi solicitada a inclusão das marcas, desenha os pixels contendo marcas
				if(bShowMarks && m_apMarkMap[iX][iY] != null)
					pRaster.setPixel(iX, iY, aiMarkColor);

				// +--- Se foi solicitada a inclusão dos agentes, desenha os pixels contendo agentes
				if(bShowAgents && m_apAgentMap[iX][iY] != null)
				{
					pMarker = (CCenterMarker) m_apAgentMap[iX][iY];
					pG2d.drawOval(pMarker.getLocation().getX() - pMarker.getRadius(), pMarker.getLocation().getY() - pMarker.getRadius(), pMarker.getRadius() * 2, pMarker.getRadius() * 2);
					pRaster.setPixel(iX, iY, aiAgentColor);
				}
			}
		}

		pG2d.setColor(new Color(aiCraterColor[0], aiCraterColor[1], aiCraterColor[2]));
		pG2d.setStroke(new BasicStroke(1));
		
		if(bShowCraters)
		{
			CCrater pCrater;
			CRegion pRegion;
			ArrayList<CCrater> apCraters;
			Iterator<CRegion> pItReg = m_apRegions.iterator();
			Iterator<CCrater> pItCrat;
			while(pItReg.hasNext())
			{
				pRegion = pItReg.next();
				apCraters = m_mpSolutions.get(pRegion);
				pItCrat = apCraters.iterator();
				while(pItCrat.hasNext())
				{
					pCrater = pItCrat.next();

					pG2d.drawOval(pCrater.getCenter().getX() - pCrater.getRadius(), pCrater.getCenter().getY() - pCrater.getRadius(), pCrater.getRadius() * 2, pCrater.getRadius() * 2);
					pRaster.setPixel(pCrater.getCenter().getX(), pCrater.getCenter().getY(), aiCraterColor);
				}
			}
		}

		pG2d.dispose();
		//return getScaledInstance(pImage, pImage.getWidth() * 4, pImage.getHeight() * 4, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR, false);
		return pImage;
	}
	
	/**
     * Convenience method that returns a scaled instance of the
     * provided {@code BufferedImage}.
     *
     * @param img the original image to be scaled
     * @param targetWidth the desired width of the scaled instance,
     *    in pixels
     * @param targetHeight the desired height of the scaled instance,
     *    in pixels
     * @param hint one of the rendering hints that corresponds to
     *    {@code RenderingHints.KEY_INTERPOLATION} (e.g.
     *    {@code RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR},
     *    {@code RenderingHints.VALUE_INTERPOLATION_BILINEAR},
     *    {@code RenderingHints.VALUE_INTERPOLATION_BICUBIC})
     * @param higherQuality if true, this method will use a multi-step
     *    scaling technique that provides higher quality than the usual
     *    one-step technique (only useful in downscaling cases, where
     *    {@code targetWidth} or {@code targetHeight} is
     *    smaller than the original dimensions, and generally only when
     *    the {@code BILINEAR} hint is specified)
     * @return a scaled version of the original {@code BufferedImage}
     */
	private BufferedImage getScaledInstance(BufferedImage pSourceImage, int iNewWidth, int iNewHeight, Object pHint, boolean bHigherQuality)
	{
		int iType = (pSourceImage.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
		BufferedImage pTargetImage = (BufferedImage) pSourceImage;
		int iW, iH;
		
		if(bHigherQuality)
		{
			// +--- Técnica de múltiplos passos: inicia-se com o tamanho original e faz-se as escalas em múltiplos passos
			// +--- com o drawImage() até que o tamanho novo seja alcançado.
			iW = pSourceImage.getWidth();
			iH = pSourceImage.getHeight();
		}
		else
		{
			// +--- Técnica de único passo: faz a escala diretamente ao tamanho final com uma única chamada de drawImage()
			iW = iNewWidth;
			iH = iNewHeight;
		}

		do
		{
			if(bHigherQuality && iW > iNewWidth)
			{
				iW /= 2;
				if(iW < iNewWidth)
					iW = iNewWidth;
			}

			if(bHigherQuality && iH > iNewHeight)
			{
				iH /= 2;
				if(iH < iNewHeight)
					iH = iNewHeight;
			}

			BufferedImage pTmp = new BufferedImage(iW, iH, iType);
			Graphics2D pG2 = pTmp.createGraphics();
			pG2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, pHint);
			pG2.drawImage(pTargetImage, 0, 0, iW, iH, null);
			pG2.dispose();

			pTargetImage = pTmp;
		}
		while(iW != iNewWidth || iH != iNewHeight);

		return pTargetImage;
	}
	
	/**
	 * Método público para o carregamento de uma imagem digital no ambiente a partir de um arquivo local.
	 * Gera uma exceção caso a imagem local não possa ser carregada.
	 * @param sFileName String com o nome do arquivo da imagem digital a ser carregada.
	 * @throws IOException Exceção gerada caso a imagem não possa ser lida do disco.
	 */
	public void loadLocalImage(String sFileName) throws IOException
	{
		/*
		 * Força uma limpeza nas estruturas de dados necessárias ao ambiente dos agentes.
		 */
		emptyEnvironmentDataStructures();

		/*
		 * Tenta carregar a imagem. Em caso de erro, a exceção é automaticamente gerada
		 */
		m_pImageBuffer = ImageIO.read(new File(sFileName));
		
		/*
		 * Monta as estruturas de dados necessárias ao ambiente dos agentes
		 */
		buildEnvironmentDataStructures();
		
		/*
		 * 
		 */
		
		Iterator<CRegion> pIt = m_apRegions.iterator();
		CRegion pRegion;
		long lTotal = m_pImageBuffer.getWidth() * m_pImageBuffer.getHeight();
		long lRegions = 0;
		double dCoverage;
		
		while(pIt.hasNext())
		{
			pRegion = pIt.next();
			lRegions += pRegion.getPixelCount();
		}
		
		dCoverage = ((double) lRegions) / ((double) lTotal) * 100.0;
		System.out.println("Cobertura só do pré-processamento: " + String.format("%3.2f", dCoverage) + "%");
	}
	
	/**
	 * Método público para o carregamento de uma imagem digital no ambiente a partir de uma URL da Web.
	 * Gera uma exceção caso a imagem da web não possa ser carregada.
	 * @param sURL String com o endereço da imagem na Web.
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public void loadWebImage(String sURL) throws MalformedURLException, IOException
	{
		/*
		 * Força uma limpeza nas estruturas de dados necessárias ao ambiente dos agentes.
		 */
		emptyEnvironmentDataStructures();
		
		/*
		 * Tenta obter a URL. Em caso de erro, a exceção é automaticamente gerada
		 */
		URL pURL = new URL(sURL);

		/*
		 * Tenta carregar a imagem. Em caso de erro, a exceção é automaticamente gerada
		 */
		m_pImageBuffer = ImageIO.read(pURL.openStream());
		
		/*
		 * Monta as estruturas de dados necessárias ao ambiente dos agentes
		 */
		buildEnvironmentDataStructures();
	}

	/**
	 * Método de observação da largura da imagem no ambiente.
	 * @return Valor inteiro com a largura (em pixels) da imagem carregada no ambiente ou 
	 * -1 se não existir uma imagem carregada no ambiente.
	 */
	public synchronized int getImageWidth()
	{
		if(m_pImageBuffer == null)
			return -1;

		return m_pImageBuffer.getWidth();
	}

	/**
	 * Método getter da altura da imagem no ambiente.
	 * @return Valor inteiro com a altura (em pixels) da imagem carregada no ambiente ou 
	 * -1 se não existir uma imagem carregada no ambiente.
	 */
	public synchronized int getImageHeight()
	{
		if(m_pImageBuffer == null)
			return -1;

		return m_pImageBuffer.getHeight();
	}
	
	/**
	 * Método getter dos pixels da imagem no ambiente.
	 * @param pLocation Objeto CLocation com a localização a ser observada.
	 * @return Objeto CPixel com o pixel da imagem carregada no ambiente ou null
	 * se a localização informada for inválida (exceder os limites da imagem) ou não existir
	 * uma imagem carregada no ambiente.
	 */
	public synchronized CPixel getPixelAt(CLocation pLocation)
	{
		if(m_pImageBuffer == null)
			return null;
		
		int iX = pLocation.getX();
		int iY = pLocation.getY();
		
		if(iX < 0 || iY < 0 || iX >= m_pImageBuffer.getWidth() || iY >= m_pImageBuffer.getHeight())
			return null;
		
		Color pColor = new Color(m_pImageBuffer.getRGB(iX, iY));
		CPixel pRet = new CPixel(pLocation, pColor);

		return pRet;
	}
	
	/**
	 * Método getter das regiões existentes no ambiente.
	 * @return Array de objetos CRegion com as regiões existentes no ambiente. Se não existir uma imagem carregada
	 * no ambiente, retorna um array vazio.
	 */
	public synchronized CRegion[] getExistingRegions()
	{
		if(m_pImageBuffer == null)
			return null;
	
		CRegion apRet[] = new CRegion[m_apRegions.size()];
		m_apRegions.toArray(apRet);
		return apRet;
	}
	
	public synchronized CMark[] getMarksInCircularArea(CLocation pCenter, int iRadius)
	{
		if(m_pImageBuffer == null)
			return null;
		
		byte aiDisc[][] = CImageProcessingAlgorithms.getDiscStructuringElement(iRadius);
		ArrayList<CMark> apMarks = new ArrayList<CMark>();

		int iX, iY;
		int iWidth = m_pImageBuffer.getWidth();
		int iHeight = m_pImageBuffer.getHeight();
		int iStartX = pCenter.getX() - iRadius;
		int iStartY = pCenter.getY() - iRadius;
		int iEndX = pCenter.getX() + iRadius;
		int iEndY = pCenter.getY() + iRadius;
		
		for(iX = iStartX; iX <= iEndX; iX++)
		{
			for(iY = iStartY; iY <= iEndY; iY++)
			{
				if(iX < 0 || iX >= iWidth || iY < 0 || iY >= iHeight)
					continue;
				
				if(aiDisc[iX - iStartX][iY - iStartY] == 0)
					continue;
					
				if(m_apMarkMap[iX][iY] != null)
					apMarks.add(m_apMarkMap[iX][iY]);
			}
		}
		
		/*
		 * Retorna o array de marcas encontradas
		 */
		CMark apRet[] = new CMark[apMarks.size()];
		apMarks.toArray(apRet);
		
		return apRet;
	}
	
	public synchronized CAbstractAgent[] getAgentsInCircularArea(CAbstractAgent pObserver, CLocation pCenter, int iRadius)
	{
		if(m_pImageBuffer == null)
			return null;
		
		byte aiDisc[][] = CImageProcessingAlgorithms.getDiscStructuringElement(iRadius);
		ArrayList<CAbstractAgent> apAgents = new ArrayList<CAbstractAgent>();

		int iX, iY;
		int iWidth = m_pImageBuffer.getWidth();
		int iHeight = m_pImageBuffer.getHeight();
		int iStartX = pCenter.getX() - iRadius;
		int iStartY = pCenter.getY() - iRadius;
		int iEndX = pCenter.getX() + iRadius;
		int iEndY = pCenter.getY() + iRadius;
		
		for(iX = iStartX; iX <= iEndX; iX++)
		{
			for(iY = iStartY; iY <= iEndY; iY++)
			{
				if(iX < 0 || iX >= iWidth || iY < 0 || iY >= iHeight)
					continue;
				
				if(aiDisc[iX - iStartX][iY - iStartY] == 0)
					continue;
					
				if(m_apAgentMap[iX][iY] != null && !m_apAgentMap[iX][iY].equals(pObserver))
					apAgents.add(m_apAgentMap[iX][iY]);
			}
		}
		
		/*
		 * Retorna o array de agentes encontrados
		 */
		CAbstractAgent apRet[] = new CAbstractAgent[apAgents.size()];
		apAgents.toArray(apRet);
		
		return apRet;
	}

	public synchronized boolean setMarkLocation(CMark pMark, CLocation pSourceLocation, CLocation pTargetLocation)
	{
		if(m_pImageBuffer == null)
			return false;

		if(pSourceLocation != null)
		{
			int iX = pSourceLocation.getX();
			int iY = pSourceLocation.getY();
			
			if(iX < 0 || iY < 0 || iX >= m_pImageBuffer.getWidth() || iY >= m_pImageBuffer.getHeight())
			{
				System.out.println("Erro no posicionamento de uma marca: localidade de origem fora dos limites da imagem.");
				return false;
			}
		}
		if(pTargetLocation != null)
		{
			int iX = pTargetLocation.getX();
			int iY = pTargetLocation.getY();
			
			if(iX < 0 || iY < 0 || iX >= m_pImageBuffer.getWidth() || iY >= m_pImageBuffer.getHeight())
			{
				System.out.println("Erro no posicionamento de uma marca: localidade de destino fora dos limites da imagem.");
				return false;
			}
		}
		
		// +--- Se não foi dada à marca uma localidade de origem, tenta um posicionamento inicial
		if(pSourceLocation == null)
		{
			CLocation pLocation = m_mpMarkPosition.get(pMark.getID());
			if(pLocation != null)
			{
				System.out.println("Erro no posicionamento de uma marca: tentou-se realizar um posicionamento inicial em uma marca já posicionada.");
				return false;
			}

			if(m_apMarkMap[pTargetLocation.getX()][pTargetLocation.getY()] != null)
			{
				System.out.println("Erro no posicionamento de uma marca: a localidade de destino já está ocupada.");
				return false;
			}
			
			m_mpMarkPosition.put(pMark.getID(), pTargetLocation);
			m_apMarkMap[pTargetLocation.getX()][pTargetLocation.getY()] = pMark;
			pMark.setLocation(pTargetLocation);
		}
		
		// +--- Se foi dada à marca uma localidade de origem...
		else
		{
			// +--- ... mas não uma localidade de destino, remove seu posicionamento atual do ambiente.
			if(pTargetLocation == null)
			{
				CLocation pLocation = m_mpMarkPosition.get(pMark.getID());
				if(pLocation == null || pLocation != pSourceLocation || m_apMarkMap[pLocation.getX()][pLocation.getY()] != pMark)
				{
					System.out.println("Erro no posicionamento de uma marca: tentou-se remover o posicionamento de uma marca com uma localidade de origem inválida.");
					return false;
				}
				
				m_mpMarkPosition.remove(pMark.getID());
				m_apMarkMap[pLocation.getX()][pLocation.getY()] = null;
				pMark.setLocation(null);
			}
			
			// +--- ... e também uma localidade de destino, tenta movê-la no ambiente.
			else
			{
				CLocation pLocation = m_mpMarkPosition.get(pMark.getID());
				if(pLocation == null || pLocation != pSourceLocation || m_apMarkMap[pLocation.getX()][pLocation.getY()] != pMark)
				{
					System.out.println("Erro no posicionamento de uma marca: tentou-se mover uma marca com uma localidade de origem inválida.");
					return false;
				}

				if(m_apMarkMap[pTargetLocation.getX()][pTargetLocation.getY()] != null)
				{
					System.out.println("Erro no posicionamento de uma marca: a localidade de destino já está ocupada.");
					return false;
				}
				
				m_mpMarkPosition.put(pMark.getID(), pTargetLocation);
				m_apMarkMap[pLocation.getX()][pLocation.getY()] = null;
				m_apMarkMap[pTargetLocation.getX()][pTargetLocation.getY()] = pMark;
				pMark.setLocation(pTargetLocation);
			}
		}
		
		return true;
	}
	
	public synchronized boolean setAgentLocation(CAbstractAgent pAgent, CLocation pSourceLocation, CLocation pTargetLocation)
	{
		if(m_pImageBuffer == null)
			return false;

		if(pSourceLocation != null)
		{
			int iX = pSourceLocation.getX();
			int iY = pSourceLocation.getY();
			
			if(iX < 0 || iY < 0 || iX >= m_pImageBuffer.getWidth() || iY >= m_pImageBuffer.getHeight())
			{
				System.out.println("Erro no posicionamento de um agente: localidade de origem fora dos limites da imagem.");
				return false;
			}
		}
		if(pTargetLocation != null)
		{
			int iX = pTargetLocation.getX();
			int iY = pTargetLocation.getY();
			
			if(iX < 0 || iY < 0 || iX >= m_pImageBuffer.getWidth() || iY >= m_pImageBuffer.getHeight())
			{
				System.out.println("Erro no posicionamento de um agente: localidade de destino fora dos limites da imagem.");
				return false;
			}
		}
		
		// +--- Se não foi dada ao agente uma localidade de origem, tenta um posicionamento inicial
		if(pSourceLocation == null)
		{
			CLocation pLocation = m_mpAgentPosition.get(pAgent.getName());
			if(pLocation != null)
			{
				System.out.println("Erro no posicionamento de um agente: tentou-se realizar um posicionamento inicial em um agente já posicionado.");
				return false;
			}

			if(m_apAgentMap[pTargetLocation.getX()][pTargetLocation.getY()] != null)
			{
				System.out.println("Erro no posicionamento de um agente: a localidade de destino já está ocupada.");
				return false;
			}
			
			m_mpAgentPosition.put(pAgent.getName(), pTargetLocation);
			m_apAgentMap[pTargetLocation.getX()][pTargetLocation.getY()] = pAgent;
		}
		
		// +--- Se foi dada ao agente uma localidade de origem...
		else
		{
			// +--- ... mas não uma localidade de destino, remove seu posicionamento atual do ambiente.
			if(pTargetLocation == null)
			{
				CLocation pLocation = m_mpAgentPosition.get(pAgent.getName());
				if(pLocation == null || pLocation != pSourceLocation || m_apAgentMap[pLocation.getX()][pLocation.getY()] != pAgent)
				{
					System.out.println("Erro no posicionamento de um agente: tentou-se remover o posicionamento de um agente com uma localidade de origem inválida.");
					return false;
				}
				
				m_mpAgentPosition.remove(pAgent.getName());
				m_apAgentMap[pLocation.getX()][pLocation.getY()] = null;
			}
			
			// +--- ... e também uma localidade de destino, tenta movê-lo no ambiente.
			else
			{
				CLocation pLocation = m_mpAgentPosition.get(pAgent.getName());
				if(pLocation == null || pLocation != pSourceLocation || m_apAgentMap[pLocation.getX()][pLocation.getY()] != pAgent)
				{
					System.out.println("Erro no posicionamento de um agente: tentou-se mover um agente com uma localidade de origem inválida.");
					return false;
				}

				if(m_apAgentMap[pTargetLocation.getX()][pTargetLocation.getY()] != null)
				{
					System.out.println("Erro no posicionamento de um agente: a localidade de destino já está ocupada.");
					return false;
				}
				
				m_mpAgentPosition.put(pAgent.getName(), pTargetLocation);
				m_apAgentMap[pLocation.getX()][pLocation.getY()] = null;
				m_apAgentMap[pTargetLocation.getX()][pTargetLocation.getY()] = pAgent;
			}
		}
		
		return true;
	}
	
	/* ******************************************************************************************
	 * Métodos privados da classe
	 ****************************************************************************************** */
	
	/**
	 * Método privado utilizado no construtor ou em momentos de exceção para limpar todas
	 * as estruturas de dados do ambiente.
	 */
	private void emptyEnvironmentDataStructures()
	{
		m_pImageBuffer = null;
		m_apAgentMap = null;
		m_apMarkMap = null;
		m_apRegionMap = null;
		m_mpAgentPosition = null;
		m_mpMarkPosition = null;
		m_apRegions = null;
		m_mpSolutions = null;
		m_mpSolutions = null;
	}
	
	/**
	 * Método privado utilizado durante o carregamento de imagens no ambiente para inicializar
	 * todas as estruturas de dados do ambiente, por meio da aplicação dos pré-processamentos
	 * necessários à imagem carregada.
	 */
	private void buildEnvironmentDataStructures()
	{
		int iWidth = m_pImageBuffer.getWidth();
		int iHeight = m_pImageBuffer.getHeight();

		/*
		 * Cria os arrays bidimensionais de posicionamento de agentes, marcas e regiões 
		 */
		m_apAgentMap = new CAbstractAgent[iWidth][iHeight];
		m_apMarkMap = new CMark[iWidth][iHeight];
		m_apRegionMap = new CRegion[iWidth][iHeight];

		int iX, iY;
		for(iX = 0; iX < iWidth; iX++)
		{
			for(iY = 0; iY < iHeight; iY++)
			{
				m_apRegionMap[iX][iY] = null;
				m_apAgentMap[iX][iY] = null;
				m_apMarkMap[iX][iY] = null;
			}
		}
		
		/*
		 * Cria os mapas e listas de indexação 
		 */
		m_mpAgentPosition = new HashMap<String, CLocation>();
		m_mpMarkPosition = new HashMap<Integer, CLocation>();
		m_apRegions = new ArrayList<CRegion>();
		m_mpSolutions = new HashMap<CRegion, ArrayList<CCrater>>();
		
		
		/* *************************************************************************
		 * Pré-processamento da imagem para geração das regiões 
		 ************************************************************************* */
		
		/*
		 * Segmentação da imagem utilizando o algoritmo k-médias. Os pixels da imagem são divididos
		 * em três classes e apenas a classe com a média de tonalidade de cinza mais baixa é mantida (em negro).
		 * As demais classes são descartadas (configuradas em branco). O resultado é uma imagem binária.
		 */
		m_aiRegionBinaryImage = CImageProcessingAlgorithms.segmentImageByKMeans(m_pImageBuffer, (short) 3);
		
		/*
		 * Aplicação de operações de morfologia matemática para remover o máximo possível de ruído da imagem binária
		 * resultante da segmentação anterior. Primeiro a imagem sofre uma operação de abertura para eliminar
		 * ruídos decorrentes de estrias e pequenas sombras. Então a imagem binária resultante sofre uma
		 * operação de fechamento para eliminar pequenos ruídos decorrentes de reflexos. Finalmente, a imagem binária
		 * resultante sofre uma operação de hit-miss para fechar todos os buracos restantes, eliminando assim
		 * grande parte dos ruídos decorrentes de reflexos.
		 * O elemento estruturante utilizado é um disco de raio 3.
		 */
		byte aiStructElem[][] = CImageProcessingAlgorithms.getDiscStructuringElement(3);
		
		m_aiRegionBinaryImage = CImageProcessingAlgorithms.openBinaryImage(m_aiRegionBinaryImage, aiStructElem);
		m_aiRegionBinaryImage = CImageProcessingAlgorithms.closeBinaryImage(m_aiRegionBinaryImage, aiStructElem);
		m_aiRegionBinaryImage = CImageProcessingAlgorithms.fillHoles(m_aiRegionBinaryImage, aiStructElem);
		
		/*
		 * Novamente aplica uma operação de morfologia matemática, mas desta vez para obter os contornos da imagem.
		 */
		aiStructElem = CImageProcessingAlgorithms.getDiscStructuringElement(1);
		m_aiBoundaryBinaryImage = CImageProcessingAlgorithms.extractBoundaries(m_aiRegionBinaryImage, aiStructElem);
		
		/*
		 * Montagem da estrutura de dados das regiões com base nos dados das imagens binária obtidas.
		 */
		CRegion pRegion;
		CPixel pPixel;
		CLocation pLocation;
		byte aiTempBinaryImage[][] = m_aiRegionBinaryImage.clone();
		
		for(iX = 0; iX < iWidth; iX++)
		{
			for(iY = 0; iY < iHeight; iY++)
			{
				if(aiTempBinaryImage[iX][iY] == 0)
				{
					aiTempBinaryImage[iX][iY] = 1;
					
					pLocation = new CLocation(iX, iY);
					pPixel = getPixelAt(pLocation);
					
					pRegion = new CRegion();
					pRegion.addPixel(pPixel, (m_aiBoundaryBinaryImage[iX][iY] == 0));
					m_apRegionMap[iX][iY] = pRegion;
					
					addAllNeighbours(aiTempBinaryImage, m_aiBoundaryBinaryImage, pRegion, pPixel);
					m_apRegions.add(pRegion);
					
					m_mpSolutions.put(pRegion, new ArrayList<CCrater>());
				}
			}
		}
	}
	
	/**
	 * Método privado para adição de todos os pixels vizinhos a um pixel inicial de uma região. Utilizado
	 * pelo método buildEnvironmentDataStructures.
	 * @param aiBinaryImage Array de bytes com os dados da imagem binária (obtida durante o pré-processamento) com
	 * as indicações de regiões encontradas.
	 * @param aiBoundaryImage Array de bytes com os dados da imagem binária de bordas (também obtida durante o pré-
	 * processamento).
	 * @param pRegion Objeto CRegion com a região em que os pixels vizinhos devem ser adicionados.
	 * @param pSeed Objeto CPixel com o pixel semente (já adicionado à região) a partir do qual os vizinhos devem ser
	 * procurados.
	 */
	private void addAllNeighbours(byte aiBinaryImage[][], byte aiBoundaryImage[][], CRegion pRegion, CPixel pSeed)
	{
		int iX, iY;
		CPixel pPixel;
		CLocation pLocation;
		Stack<CPixel> apSeeds = new Stack<CPixel>();
		
		apSeeds.push(pSeed);
		while(apSeeds.size() > 0)
		{
			pSeed = apSeeds.pop();

			for(iX = pSeed.getLocation().getX() - 1; iX <= pSeed.getLocation().getX() + 1; iX++)
			{
				for(iY = pSeed.getLocation().getY() - 1; iY <= pSeed.getLocation().getY() + 1; iY++)
				{
					pLocation = new CLocation(iX, iY);
					pPixel = getPixelAt(pLocation);
					if(pPixel == null || pPixel.equals(pSeed))
						continue;

					if(aiBinaryImage[iX][iY] == 0)
					{
						aiBinaryImage[iX][iY] = 1;
						m_apRegionMap[iX][iY] = pRegion;
						pRegion.addPixel(pPixel, (aiBoundaryImage[iX][iY] == 0));
						apSeeds.push(pPixel);
					}
				}
			}
		}
	}
	
	
	public synchronized void setSolution(CRegion pRegion, CCrater[] apCraters)
	{
		ArrayList<CCrater> apSolution;
		apSolution = m_mpSolutions.get(pRegion);
		if(apSolution != null)
		{
			apSolution.clear();
			for(int i = 0; i < apCraters.length; i++)
				apSolution.add(apCraters[i]);
		}
	}
	
	public synchronized int getSolutionCount(CRegion pRegion)
	{
		ArrayList<CCrater> apSolution;
		apSolution = m_mpSolutions.get(pRegion);
		if(apSolution != null)
			return apSolution.size();
		else
			return 0;
	}
	
	public synchronized void updateRegion(CRegion pRegion)
	{
		int iX, iY;
		for(iX = 0; iX < m_apRegionMap.length; iX++)
		{
			for(iY = 0; iY < m_apRegionMap[iX].length; iY++)
			{
				if(!pRegion.contains(new CPixel(new CLocation(iX, iY), Color.GREEN)))
				{
					if(m_apRegionMap[iX][iY] == pRegion)
						m_apRegionMap[iX][iY] = null;
				}
				else
					m_apRegionMap[iX][iY] = pRegion;
			}
		}
	}
}
