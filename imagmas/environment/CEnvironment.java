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
 * Classe para representa��o do ambiente de observa��o e atua��o dos agentes (o ambiente
 * de execu��o � a plataforma JADE). Permite a observa��o das intensidades dos pixels
 * 
 * @author Luiz Carlos Vieira
 */
public class CEnvironment
{
	/* ******************************************************************************************
	 * Atributos privados
	 ****************************************************************************************** */
	
	/**
	 * Inst�ncia �nica da classe IMEnvironment (singleton).
	 */
	private static CEnvironment m_pDefaultInstance = null;
	
	/**
	 * Dados originais da imagem carregada no ambiente.
	 */
	private BufferedImage m_pImageBuffer;

	/**
	 * Dados da imagem bin�ria contendo apenas as bordas (em zero) da imagem original.
	 */
	private byte m_aiBoundaryBinaryImage[][];
	
	/**
	 * Dados da imagem bin�ria contendo apenas os pixels de regi�o (em zero) da imagem original.
	 */
	private byte m_aiRegionBinaryImage[][];
	
	/**
	 * Matriz bidimensional com o posicionamento de agentes no ambiente.
	 */
	private CAbstractAgent m_apAgentMap[][];

	/**
	 * Matriz bidimensional com a identifica��o dos pixels e suas regi�es no ambiente.
	 */
	private CRegion m_apRegionMap[][];

	/**
	 * Matriz bidimensional com as marcas de borda no ambiente.
	 */
	private CMark m_apMarkMap[][];
	
	/**
	 * Mapa de localiza��o de agentes posicionados no ambiente.
	 */
	private HashMap<String, CLocation> m_mpAgentPosition;
	
	/**
	 * Mapa de localiza��o de marcas posicionadas no ambiente.
	 */
	private HashMap<Integer, CLocation> m_mpMarkPosition;
	
	/**
	 * Lista das regi�es produzidas na imagem.
	 */
	private ArrayList<CRegion> m_apRegions;
	
	/**
	 * Solu��es encontradas por regi�o.
	 */
	private HashMap<CRegion, ArrayList<CCrater>> m_mpSolutions;

	/* ******************************************************************************************
	 * Construtores e m�todos de inicializa��o
	 ****************************************************************************************** */
	
	/**
	 * Construtor da classe, respons�vel por inicializar as propriedades do ambiente. � definido como protegido
	 * (protected) pois a classe � um singleton. Ou seja, todos os seus m�todos devem ser acessados em uma �nica
	 * inst�ncia, obtida por meio do m�todo getInstance().
	 * Obs.: O ambiente apenas estar� pronto para a execu��o de agentes ap�s o carregamento de uma imagem por
	 * meio de um dos m�todos loadLocalImage ou loadWebImage.
	 */
	protected CEnvironment()
	{
		emptyEnvironmentDataStructures();
	}

	/**
	 * M�todo est�tico p�blico para acesso � inst�ncia �nica da classe de ambiente. 
	 * @return Objeto IMEnvironment com a inst�ncia �nica do ambiente para todo o sistema.
	 */
	public static CEnvironment getInstance()
	{
		if(m_pDefaultInstance == null)
			m_pDefaultInstance = new CEnvironment();
		
		return m_pDefaultInstance;
	}
	
	/* ******************************************************************************************
	 * M�todos p�blicos
	 ****************************************************************************************** */
	
	/**
	 * M�todo para a obten��o de uma c�pia da imagem original. Utilizado exclusivamente pela interface gr�fica
	 * para apresenta��o ao usu�rio.
	 * @return Objeto BufferedImage com uma c�pia da imagem original do ambiente.
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
	 * M�todo para a obten��o de uma imagem de representa��o gr�fica do estado da segmenta��o em execu��o no ambiente.
	 * @return Objeto BufferedImage com a imagem de representa��o do estado da segmenta��o em execu��o.
	 */
	public synchronized BufferedImage genSegmentedRepresentation(boolean bShowOriginalImage, boolean bShowRegions, boolean bShowMarks, boolean bShowAgents, boolean bShowCraters)
	{
		if(m_pImageBuffer == null)
			return null;
		
		// +--- Cria a imagem de retorno com os mesmos atributos da imagem original.
		BufferedImage pImage = new BufferedImage(m_pImageBuffer.getWidth(), m_pImageBuffer.getHeight(), m_pImageBuffer.getType());
		WritableRaster pRaster = pImage.getRaster();			
		
		// +--- Se foi solicitada a inclus�o da imagem original, copia seus dados antes de continuar. 
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
				// +--- Se foi solicitada a inclus�o das regi�es, desenha os pixels contidos em regi�es
				if(bShowRegions && m_apRegionMap[iX][iY] != null)
					pRaster.setPixel(iX, iY, aiRegionColor);

				// +--- Se foi solicitada a inclus�o das marcas, desenha os pixels contendo marcas
				if(bShowMarks && m_apMarkMap[iX][iY] != null)
					pRaster.setPixel(iX, iY, aiMarkColor);

				// +--- Se foi solicitada a inclus�o dos agentes, desenha os pixels contendo agentes
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
			// +--- T�cnica de m�ltiplos passos: inicia-se com o tamanho original e faz-se as escalas em m�ltiplos passos
			// +--- com o drawImage() at� que o tamanho novo seja alcan�ado.
			iW = pSourceImage.getWidth();
			iH = pSourceImage.getHeight();
		}
		else
		{
			// +--- T�cnica de �nico passo: faz a escala diretamente ao tamanho final com uma �nica chamada de drawImage()
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
	 * M�todo p�blico para o carregamento de uma imagem digital no ambiente a partir de um arquivo local.
	 * Gera uma exce��o caso a imagem local n�o possa ser carregada.
	 * @param sFileName String com o nome do arquivo da imagem digital a ser carregada.
	 * @throws IOException Exce��o gerada caso a imagem n�o possa ser lida do disco.
	 */
	public void loadLocalImage(String sFileName) throws IOException
	{
		/*
		 * For�a uma limpeza nas estruturas de dados necess�rias ao ambiente dos agentes.
		 */
		emptyEnvironmentDataStructures();

		/*
		 * Tenta carregar a imagem. Em caso de erro, a exce��o � automaticamente gerada
		 */
		m_pImageBuffer = ImageIO.read(new File(sFileName));
		
		/*
		 * Monta as estruturas de dados necess�rias ao ambiente dos agentes
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
		System.out.println("Cobertura s� do pr�-processamento: " + String.format("%3.2f", dCoverage) + "%");
	}
	
	/**
	 * M�todo p�blico para o carregamento de uma imagem digital no ambiente a partir de uma URL da Web.
	 * Gera uma exce��o caso a imagem da web n�o possa ser carregada.
	 * @param sURL String com o endere�o da imagem na Web.
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public void loadWebImage(String sURL) throws MalformedURLException, IOException
	{
		/*
		 * For�a uma limpeza nas estruturas de dados necess�rias ao ambiente dos agentes.
		 */
		emptyEnvironmentDataStructures();
		
		/*
		 * Tenta obter a URL. Em caso de erro, a exce��o � automaticamente gerada
		 */
		URL pURL = new URL(sURL);

		/*
		 * Tenta carregar a imagem. Em caso de erro, a exce��o � automaticamente gerada
		 */
		m_pImageBuffer = ImageIO.read(pURL.openStream());
		
		/*
		 * Monta as estruturas de dados necess�rias ao ambiente dos agentes
		 */
		buildEnvironmentDataStructures();
	}

	/**
	 * M�todo de observa��o da largura da imagem no ambiente.
	 * @return Valor inteiro com a largura (em pixels) da imagem carregada no ambiente ou 
	 * -1 se n�o existir uma imagem carregada no ambiente.
	 */
	public synchronized int getImageWidth()
	{
		if(m_pImageBuffer == null)
			return -1;

		return m_pImageBuffer.getWidth();
	}

	/**
	 * M�todo getter da altura da imagem no ambiente.
	 * @return Valor inteiro com a altura (em pixels) da imagem carregada no ambiente ou 
	 * -1 se n�o existir uma imagem carregada no ambiente.
	 */
	public synchronized int getImageHeight()
	{
		if(m_pImageBuffer == null)
			return -1;

		return m_pImageBuffer.getHeight();
	}
	
	/**
	 * M�todo getter dos pixels da imagem no ambiente.
	 * @param pLocation Objeto CLocation com a localiza��o a ser observada.
	 * @return Objeto CPixel com o pixel da imagem carregada no ambiente ou null
	 * se a localiza��o informada for inv�lida (exceder os limites da imagem) ou n�o existir
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
	 * M�todo getter das regi�es existentes no ambiente.
	 * @return Array de objetos CRegion com as regi�es existentes no ambiente. Se n�o existir uma imagem carregada
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
		
		// +--- Se n�o foi dada � marca uma localidade de origem, tenta um posicionamento inicial
		if(pSourceLocation == null)
		{
			CLocation pLocation = m_mpMarkPosition.get(pMark.getID());
			if(pLocation != null)
			{
				System.out.println("Erro no posicionamento de uma marca: tentou-se realizar um posicionamento inicial em uma marca j� posicionada.");
				return false;
			}

			if(m_apMarkMap[pTargetLocation.getX()][pTargetLocation.getY()] != null)
			{
				System.out.println("Erro no posicionamento de uma marca: a localidade de destino j� est� ocupada.");
				return false;
			}
			
			m_mpMarkPosition.put(pMark.getID(), pTargetLocation);
			m_apMarkMap[pTargetLocation.getX()][pTargetLocation.getY()] = pMark;
			pMark.setLocation(pTargetLocation);
		}
		
		// +--- Se foi dada � marca uma localidade de origem...
		else
		{
			// +--- ... mas n�o uma localidade de destino, remove seu posicionamento atual do ambiente.
			if(pTargetLocation == null)
			{
				CLocation pLocation = m_mpMarkPosition.get(pMark.getID());
				if(pLocation == null || pLocation != pSourceLocation || m_apMarkMap[pLocation.getX()][pLocation.getY()] != pMark)
				{
					System.out.println("Erro no posicionamento de uma marca: tentou-se remover o posicionamento de uma marca com uma localidade de origem inv�lida.");
					return false;
				}
				
				m_mpMarkPosition.remove(pMark.getID());
				m_apMarkMap[pLocation.getX()][pLocation.getY()] = null;
				pMark.setLocation(null);
			}
			
			// +--- ... e tamb�m uma localidade de destino, tenta mov�-la no ambiente.
			else
			{
				CLocation pLocation = m_mpMarkPosition.get(pMark.getID());
				if(pLocation == null || pLocation != pSourceLocation || m_apMarkMap[pLocation.getX()][pLocation.getY()] != pMark)
				{
					System.out.println("Erro no posicionamento de uma marca: tentou-se mover uma marca com uma localidade de origem inv�lida.");
					return false;
				}

				if(m_apMarkMap[pTargetLocation.getX()][pTargetLocation.getY()] != null)
				{
					System.out.println("Erro no posicionamento de uma marca: a localidade de destino j� est� ocupada.");
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
		
		// +--- Se n�o foi dada ao agente uma localidade de origem, tenta um posicionamento inicial
		if(pSourceLocation == null)
		{
			CLocation pLocation = m_mpAgentPosition.get(pAgent.getName());
			if(pLocation != null)
			{
				System.out.println("Erro no posicionamento de um agente: tentou-se realizar um posicionamento inicial em um agente j� posicionado.");
				return false;
			}

			if(m_apAgentMap[pTargetLocation.getX()][pTargetLocation.getY()] != null)
			{
				System.out.println("Erro no posicionamento de um agente: a localidade de destino j� est� ocupada.");
				return false;
			}
			
			m_mpAgentPosition.put(pAgent.getName(), pTargetLocation);
			m_apAgentMap[pTargetLocation.getX()][pTargetLocation.getY()] = pAgent;
		}
		
		// +--- Se foi dada ao agente uma localidade de origem...
		else
		{
			// +--- ... mas n�o uma localidade de destino, remove seu posicionamento atual do ambiente.
			if(pTargetLocation == null)
			{
				CLocation pLocation = m_mpAgentPosition.get(pAgent.getName());
				if(pLocation == null || pLocation != pSourceLocation || m_apAgentMap[pLocation.getX()][pLocation.getY()] != pAgent)
				{
					System.out.println("Erro no posicionamento de um agente: tentou-se remover o posicionamento de um agente com uma localidade de origem inv�lida.");
					return false;
				}
				
				m_mpAgentPosition.remove(pAgent.getName());
				m_apAgentMap[pLocation.getX()][pLocation.getY()] = null;
			}
			
			// +--- ... e tamb�m uma localidade de destino, tenta mov�-lo no ambiente.
			else
			{
				CLocation pLocation = m_mpAgentPosition.get(pAgent.getName());
				if(pLocation == null || pLocation != pSourceLocation || m_apAgentMap[pLocation.getX()][pLocation.getY()] != pAgent)
				{
					System.out.println("Erro no posicionamento de um agente: tentou-se mover um agente com uma localidade de origem inv�lida.");
					return false;
				}

				if(m_apAgentMap[pTargetLocation.getX()][pTargetLocation.getY()] != null)
				{
					System.out.println("Erro no posicionamento de um agente: a localidade de destino j� est� ocupada.");
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
	 * M�todos privados da classe
	 ****************************************************************************************** */
	
	/**
	 * M�todo privado utilizado no construtor ou em momentos de exce��o para limpar todas
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
	 * M�todo privado utilizado durante o carregamento de imagens no ambiente para inicializar
	 * todas as estruturas de dados do ambiente, por meio da aplica��o dos pr�-processamentos
	 * necess�rios � imagem carregada.
	 */
	private void buildEnvironmentDataStructures()
	{
		int iWidth = m_pImageBuffer.getWidth();
		int iHeight = m_pImageBuffer.getHeight();

		/*
		 * Cria os arrays bidimensionais de posicionamento de agentes, marcas e regi�es 
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
		 * Cria os mapas e listas de indexa��o 
		 */
		m_mpAgentPosition = new HashMap<String, CLocation>();
		m_mpMarkPosition = new HashMap<Integer, CLocation>();
		m_apRegions = new ArrayList<CRegion>();
		m_mpSolutions = new HashMap<CRegion, ArrayList<CCrater>>();
		
		
		/* *************************************************************************
		 * Pr�-processamento da imagem para gera��o das regi�es 
		 ************************************************************************* */
		
		/*
		 * Segmenta��o da imagem utilizando o algoritmo k-m�dias. Os pixels da imagem s�o divididos
		 * em tr�s classes e apenas a classe com a m�dia de tonalidade de cinza mais baixa � mantida (em negro).
		 * As demais classes s�o descartadas (configuradas em branco). O resultado � uma imagem bin�ria.
		 */
		m_aiRegionBinaryImage = CImageProcessingAlgorithms.segmentImageByKMeans(m_pImageBuffer, (short) 3);
		
		/*
		 * Aplica��o de opera��es de morfologia matem�tica para remover o m�ximo poss�vel de ru�do da imagem bin�ria
		 * resultante da segmenta��o anterior. Primeiro a imagem sofre uma opera��o de abertura para eliminar
		 * ru�dos decorrentes de estrias e pequenas sombras. Ent�o a imagem bin�ria resultante sofre uma
		 * opera��o de fechamento para eliminar pequenos ru�dos decorrentes de reflexos. Finalmente, a imagem bin�ria
		 * resultante sofre uma opera��o de hit-miss para fechar todos os buracos restantes, eliminando assim
		 * grande parte dos ru�dos decorrentes de reflexos.
		 * O elemento estruturante utilizado � um disco de raio 3.
		 */
		byte aiStructElem[][] = CImageProcessingAlgorithms.getDiscStructuringElement(3);
		
		m_aiRegionBinaryImage = CImageProcessingAlgorithms.openBinaryImage(m_aiRegionBinaryImage, aiStructElem);
		m_aiRegionBinaryImage = CImageProcessingAlgorithms.closeBinaryImage(m_aiRegionBinaryImage, aiStructElem);
		m_aiRegionBinaryImage = CImageProcessingAlgorithms.fillHoles(m_aiRegionBinaryImage, aiStructElem);
		
		/*
		 * Novamente aplica uma opera��o de morfologia matem�tica, mas desta vez para obter os contornos da imagem.
		 */
		aiStructElem = CImageProcessingAlgorithms.getDiscStructuringElement(1);
		m_aiBoundaryBinaryImage = CImageProcessingAlgorithms.extractBoundaries(m_aiRegionBinaryImage, aiStructElem);
		
		/*
		 * Montagem da estrutura de dados das regi�es com base nos dados das imagens bin�ria obtidas.
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
	 * M�todo privado para adi��o de todos os pixels vizinhos a um pixel inicial de uma regi�o. Utilizado
	 * pelo m�todo buildEnvironmentDataStructures.
	 * @param aiBinaryImage Array de bytes com os dados da imagem bin�ria (obtida durante o pr�-processamento) com
	 * as indica��es de regi�es encontradas.
	 * @param aiBoundaryImage Array de bytes com os dados da imagem bin�ria de bordas (tamb�m obtida durante o pr�-
	 * processamento).
	 * @param pRegion Objeto CRegion com a regi�o em que os pixels vizinhos devem ser adicionados.
	 * @param pSeed Objeto CPixel com o pixel semente (j� adicionado � regi�o) a partir do qual os vizinhos devem ser
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
