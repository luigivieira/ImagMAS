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
 
package imagmas.gui;

import jadex.runtime.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.*;
import java.io.*;

import javax.swing.*;
import javax.swing.border.*;

import imagmas.configuration.CConfiguration;
import imagmas.environment.*;
import imagmas.gui.commands.*;
import imagmas.gui.display.*;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
/**
 * Classe da janela principal da interface gráfica do sistema ImagMAS. Exibe
 * a imagem original e a imagem segmentada, e permite a interação do sistema
 * com o usuário. Essa janela interage também com o agente de ambiente, para 
 * obter o resultado da segmentação, informar o conhecimento configurado pelo
 * usuário e iniciar o processo de segmentação da imagem.
 * 
 * @author Luiz Carlos Vieira
 * 
 * @see IMInterfaceAgent
 */
public class CMainWindow implements ActionListener
{
	/**
	 * Atributo privado para o armazenamento do objeto de acesso externo ao agente de interface.
	 */
	private IExternalAccess m_pAgent;

	/**
	 * Atributo privado para o armazenamento do objeto de exibição da imagem carregada
	 * pelo usuário.
	 */
	private CImageDisplay m_pSourceDisplay;
	
	/**
	 * Atributo privado para o armazenamento do objeto de exibição da imagem gerada
	 * do processo de segmentação.
	 */
	private CImageDisplay m_pTargetDisplay;

	/**
	 * Atributo privado para o armazenamento do Frame da janela principal.
	 */
	private JFrame m_pFrame;

	/**
	 * Atributo privado para o armazenamento da ação do comando de carregamento
	 * de imagens locais.
	 */
	private CLoadLocalCommand m_pLoadLocalCommand = new CLoadLocalCommand(this);
	
	/**
	 * Atributo privado para o armazenamento da ação do comando de configuração do processo de 
	 * segmentação.
	 */
	private CConfigureSegmentationCommand m_pConfigSegmentationCommand = new CConfigureSegmentationCommand(this);

	/**
	 * Atributo privado para o armazenamento da ação do comando de início do processo de segmentação
	 * para a imagem carregada.
	 */
	private CStartSegmentationCommand m_pStartSegmentationCommand = new CStartSegmentationCommand(this);
	
	/**
	 * Atributo privado para o armazenamento da ação do comando de parada do processo de segmentação
	 * para a imagem carregada.
	 */
	private CStopSegmentationCommand m_pStopSegmentationCommand = new CStopSegmentationCommand(this);

	
	private JProgressBar m_pProgressBar;
	
	private JButton m_pBtnCancel;

	/**
	 * Atributo privado para o armazenamento da ação do comando de atualização da imagem segmentada.
	 */
	private CUpdateSegmentationCommand m_pUpdateSegmentationCommand = new CUpdateSegmentationCommand(this);

	/**
	 * Atributo privado para o armazenamento da ação do comando de encerramento
	 * do sistema.
	 */
	private CExitCommand m_pExitCommand = new CExitCommand(this);

	/**
	 * Atributo privado para o armazenamento da ação do comando de exibição da imagem original no resultado da segmentação.
	 */
	private CToggleShowBaseImageCommand m_pToggleShowBaseImageCommand = new CToggleShowBaseImageCommand(this);

	/**
	 * Atributo privado para o armazenamento da ação do comando de exibição das regiões no resultado da segmentação.
	 */
	private CToggleShowRegionsCommand m_pToggleShowRegionsCommand = new CToggleShowRegionsCommand(this);

	/**
	 * Atributo privado para o armazenamento da ação do comando de exibição das marcas no resultado da segmentação.
	 */
	private CToggleShowMarksCommand m_pToggleShowMarksCommand = new CToggleShowMarksCommand(this);
	
	/**
	 * Atributo privado para o armazenamento da ação do comando de exibição dos agentes no resultado da segmentação.
	 */
	private CToggleShowAgentsCommand m_pToggleShowAgentsCommand = new CToggleShowAgentsCommand(this);
	
	/**
	 * Atributo privado para o armazenamento da ação do comando de exibição das crateras no resultado da segmentação.
	 */
	private CToggleShowCratersCommand m_pToggleShowCratersCommand = new CToggleShowCratersCommand(this);

	/**
	 * Atributo utilizado para armazenar o estado atual da exibição da imagem original no resultado da segmentação.
	 */
	private boolean m_bShowBaseImage;
	
	/**
	 * Atributo utilizado para armazenar o estado atual da exibição da imagem original no resultado da segmentação.
	 */
	private boolean m_bShowMarks;
	
	/**
	 * Atributo utilizado para armazenar o estado atual da exibição das regiões no resultado da segmentação.
	 */
	private boolean m_bShowRegions;
	
	/**
	 * Atributo utilizado para armazenar o estado atual da exibição dos agentes no resultado da segmentação.
	 */
	private boolean m_bShowAgents;
	
	/**
	 * Atributo utilizado para armazenar o estado atual da exibição das crateras no resultado da segmentação.
	 */
	private boolean m_bShowCraters;
	
	/**
	 * Construtor da classe. Inicializa os objetos e monta a interface gráfica inicial.
	 * @param pAgent Objeto IMEnvironmentAgent com o agente de ambiente para interface.
	 */
	public CMainWindow(IExternalAccess pAgent)
	{
		m_pAgent = pAgent;
		m_pSourceDisplay = new CImageDisplay();
		m_pTargetDisplay = new CImageDisplay();
		m_pTargetDisplay.setPreferredSize(new java.awt.Dimension(290, 295));

		m_bShowBaseImage = true;
		m_bShowRegions = false;
		m_bShowMarks = false;
		m_bShowAgents = true;
		m_bShowCraters = true;
		
		prepare();
		
		evaluateActionStatus();
		m_pFrame.setVisible(true);
	}

	/**
	 * Método privado chamado pelo construtor para a inicialização da 
	 * interface gráfica: preparação de menus, barras de ferramentas, etc.
	 */
	private void prepare()
	{
		m_pFrame = new JFrame();
		m_pFrame.setTitle("ImagMAS - Sistema Multiagentes para Segmentação de Imagens Digitais");
		m_pFrame.setName("m_pMainWindow");
		m_pFrame.setBounds(0, 27, 595, 431);
		m_pFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		m_pFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		final JMenuBar menuBar = new JMenuBar();
		menuBar.setName("m_pMenuBar");
		m_pFrame.setJMenuBar(menuBar);

		final JMenu fileMenu = new JMenu();
		fileMenu.setText("Arquivo");
		menuBar.add(fileMenu);

		final JMenuItem mnuLoad = new JMenuItem();
		mnuLoad.setAction(m_pLoadLocalCommand);
		fileMenu.add(mnuLoad);

		final JMenuItem mnuSave = new JMenuItem();
		fileMenu.add(mnuSave);
		
		fileMenu.addSeparator();

		final JMenuItem mnuExit = new JMenuItem();
		mnuExit.setAction(m_pExitCommand);
		fileMenu.add(mnuExit);

		final JMenu segMenu = new JMenu();
		segMenu.setText("Segmentação");
		menuBar.add(segMenu);

		final JMenuItem mnuCfg = new JMenuItem();
		mnuCfg.setAction(m_pConfigSegmentationCommand);
		segMenu.add(mnuCfg);

		final JMenuItem mnuStart = new JMenuItem();
		mnuStart.setAction(m_pStartSegmentationCommand);
		segMenu.add(mnuStart);

		final JMenuItem mnuStop = new JMenuItem();
		mnuStop.setAction(m_pStopSegmentationCommand);
		segMenu.add(mnuStop);
		
		segMenu.addSeparator();
		
		final JMenuItem mnuUpdate = new JMenuItem();
		mnuUpdate.setAction(m_pUpdateSegmentationCommand);
		segMenu.add(mnuUpdate);
		
		final JMenu helpMenu = new JMenu();
		helpMenu.setText("Ajuda");
		menuBar.add(helpMenu);
		
		final JMenuItem aboutMenu = new JMenuItem();
		helpMenu.add(aboutMenu);
		
		final JPanel pPanelStatus = new JPanel();
		pPanelStatus.setFocusable(false);
		pPanelStatus.setBorder(new LineBorder(Color.black, 1, false));
		BorderLayout pPanelStatusLayout = new BorderLayout();
		pPanelStatus.setLayout(pPanelStatusLayout);
		pPanelStatus.setPreferredSize(new Dimension(0, 20));
		m_pFrame.getContentPane().add(pPanelStatus, BorderLayout.SOUTH);
		{
			m_pBtnCancel = new JButton();
			pPanelStatus.add(m_pBtnCancel, BorderLayout.WEST);
			m_pBtnCancel.setText("Cancelar");
			m_pBtnCancel.setSize(66, 19);
			m_pBtnCancel.setPreferredSize(new java.awt.Dimension(98, 18));
			m_pBtnCancel.addActionListener(this);
		}
		{
			m_pProgressBar = new JProgressBar();
			m_pProgressBar.setStringPainted(true);
			pPanelStatus.add(m_pProgressBar, BorderLayout.CENTER);
		}

		final JPanel pPanelCenter = new JPanel();
		pPanelCenter.setLayout(new BoxLayout(pPanelCenter, BoxLayout.X_AXIS));
		m_pFrame.getContentPane().add(pPanelCenter, BorderLayout.CENTER);
		
		final JScrollPane pLeftScroll = new JScrollPane(m_pSourceDisplay);
		pLeftScroll.setViewportView(m_pSourceDisplay);
		pPanelCenter.add(pLeftScroll);

		final JScrollPane pRightScroll = new JScrollPane(m_pTargetDisplay);
		pRightScroll.setViewportView(m_pTargetDisplay);
		pPanelCenter.add(pRightScroll);

		final JPanel pToolbarArea = new JPanel();
		final FlowLayout flowLayout = new FlowLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		pToolbarArea.setLayout(flowLayout);
		m_pFrame.getContentPane().add(pToolbarArea, BorderLayout.NORTH);

		final JToolBar pFileToolbar = new JToolBar();
		pToolbarArea.add(pFileToolbar);
		pFileToolbar.setName("Arquivo");

		final JButton btnLoad = new JButton();		
		btnLoad.setAction(m_pLoadLocalCommand);
		btnLoad.setText("");
		pFileToolbar.add(btnLoad);

		final JButton btnExit = new JButton();
		btnExit.setAction(m_pExitCommand);
		btnExit.setText("");
		pFileToolbar.add(btnExit);
		
		final JToolBar pViewToolbar = new JToolBar();
		pToolbarArea.add(pViewToolbar);
		pViewToolbar.setName("Visualizar");

		final JToggleButton btnShowBaseImage = new JToggleButton("", m_bShowBaseImage);
		//btnShowBaseImage.getModel().setPressed(m_bShowBaseImage);
		btnShowBaseImage.setAction(m_pToggleShowBaseImageCommand);
		btnShowBaseImage.setText("");
		pViewToolbar.add(btnShowBaseImage);

		final JToggleButton btnShowRegions = new JToggleButton("", m_bShowRegions);
		//btnShowRegions.getModel().setPressed(m_bShowRegions);
		btnShowRegions.setAction(m_pToggleShowRegionsCommand);
		btnShowRegions.setText("");
		pViewToolbar.add(btnShowRegions);

		final JToggleButton btnShowMarks = new JToggleButton("", m_bShowMarks);
		//btnShowMarks.getModel().setPressed(m_bShowMarks);
		btnShowMarks.setAction(m_pToggleShowMarksCommand);
		btnShowMarks.setText("");
		pViewToolbar.add(btnShowMarks);

		final JToggleButton btnShowAgents = new JToggleButton("", m_bShowAgents);
		//btnShowAgents.getModel().setPressed(m_bShowAgents);
		btnShowAgents.setAction(m_pToggleShowAgentsCommand);
		btnShowAgents.setText("");
		pViewToolbar.add(btnShowAgents);

		final JToggleButton btnShowCraters = new JToggleButton("", m_bShowCraters);
		//btnShowCraters.getModel().setPressed(m_bShowCraters);
		btnShowCraters.setAction(m_pToggleShowCratersCommand);
		btnShowCraters.setText("");
		pViewToolbar.add(btnShowCraters);
	}

	/* ***************************************************************************
	 * Métodos de interface com as ações de comando
	 *************************************************************************** */

	public void toggleShowBaseImage()
	{
		m_bShowBaseImage = !m_bShowBaseImage;
		updateSegmentation();
	}
	
	public void toggleShowMarks()
	{
		m_bShowMarks = !m_bShowMarks;
		updateSegmentation();
	}
	
	public void toggleShowRegions()
	{
		m_bShowRegions = !m_bShowRegions;
		updateSegmentation();
	}
	
	public void toggleShowAgents()
	{
		m_bShowAgents = !m_bShowAgents;
		updateSegmentation();
	}
	
	public void toggleShowCraters()
	{
		m_bShowCraters = !m_bShowCraters;
		updateSegmentation();
	}
	
	/**
	 * Método público para o carregamento de uma imagem local.
	 * @param pFile Objeto File com o arquivo da imagem local a ser carregado.
	 * @return Retorna verdadeiro caso a imagem tenha sido carregada e falso caso um erro
	 * tenha ocorrido.
	 */
	public boolean loadImage(File pFile)
	{
		try
		{
			CEnvironment.getInstance().loadLocalImage(pFile.getAbsolutePath());
		}
		catch (IOException e)
		{
			evaluateActionStatus();
			return false;
		}
		
		/*
		 * Atualiza as imagens e displays atuais com a nova imagem carregada
		 */
		BufferedImage pSourceImage;
		pSourceImage = CEnvironment.getInstance().getSourceImage();
		
		m_pSourceDisplay.setImage(pSourceImage);
		evaluateActionStatus();
		return true;
	}

	/**
	 * Método público para a solicitação de encerramento da aplicação.
	 */
	public void terminateApplication()
	{
		// +--- Fecha a janela principal
		m_pFrame.dispose();
		
		// +--- Sinaliza ao agente o encerramento da aplicação
		m_pAgent.getBeliefbase().getBelief("shutdown_requested").setFact(true);
	}

	/**
	 * Método público para a solicitação do início da segmentação da imagem carregada.
	 * @return Retorna verdadeiro caso a segmentação pôde ser iniciada e falso caso
	 * uma imagem ainda não tenha sido carregada.
	 */
	public boolean startSegmentation()
	{
		/*
		 * Prepara a imagem de apresentação do resultado do processo de segmentação
		 */
		updateSegmentation();

		/*
		 * Identifica ao agente as configurações de segmentação.
		 */
		m_pAgent.getBeliefbase().getBelief("minimum_crater_radius").setFact(CConfiguration.getMinimumRadius());
		m_pAgent.getBeliefbase().getBelief("maximum_crater_radius").setFact(CConfiguration.getMaximumRadius());
		m_pAgent.getBeliefbase().getBelief("distance_between_marks").setFact(CConfiguration.getMarksDistance());
		m_pAgent.getBeliefbase().getBelief("inclusion_factor").setFact(CConfiguration.getInclusionFactor());
		m_pAgent.getBeliefbase().getBelief("differentiation_factor").setFact(CConfiguration.getDifferentiationFactor());

		// +++ Guarda o horário do início da segmentação
		m_pAgent.getBeliefbase().getBelief("segmentation_start_time").setFact(System.currentTimeMillis());
		
		/*
		 * Identifica ao agente que a segmentação iniciou.
		 */
		m_pAgent.getBeliefbase().getBelief("start_requested").setFact(true);

		m_pProgressBar.setMinimum(0);
		m_pProgressBar.setMaximum(CEnvironment.getInstance().getExistingRegions().length);
		m_pProgressBar.setValue(0);
		evaluateActionStatus();
		return true;
	}
	
	public boolean stopSegmentation()
	{
		m_pAgent.getBeliefbase().getBelief("stop_requested").setFact(true);
		
		evaluateActionStatus();
		return true;
	}
	
	/**
	 * Método público para a solicitação da atualização da imagem de representação da segmentação
	 * da imagem original.
	 * @return Retorna verdadeiro caso a atualização pôde ser realizada e falso caso contrário.
	 */
	public boolean updateSegmentation()
	{
		BufferedImage pTargetImage;
		
		pTargetImage = CEnvironment.getInstance().genSegmentedRepresentation(m_bShowBaseImage, m_bShowRegions, m_bShowMarks, m_bShowAgents, m_bShowCraters);
		
		m_pTargetDisplay.setImage(pTargetImage);
		m_pTargetDisplay.repaint();
		return true;
	}
	
	public void evaluateActionStatus()
	{
		boolean bSegmentationRunning = (Boolean) m_pAgent.getBeliefbase().getBelief("start_requested").getFact();
		boolean bImageLoaded = (CEnvironment.getInstance().getSourceImage() != null);
		
		m_pExitCommand.setEnabled(!bSegmentationRunning);
		m_pLoadLocalCommand.setEnabled(!bSegmentationRunning);
		
		m_pUpdateSegmentationCommand.setEnabled(bImageLoaded);
		
		m_pStartSegmentationCommand.setEnabled(bImageLoaded && !bSegmentationRunning);
		m_pStopSegmentationCommand.setEnabled(bSegmentationRunning);
		
		m_pConfigSegmentationCommand.setEnabled(!bSegmentationRunning);
		
		m_pBtnCancel.setVisible(bSegmentationRunning);
		m_pProgressBar.setVisible(bSegmentationRunning);
	}
	
	public void stepStatusBar()
	{
		m_pProgressBar.setValue(m_pProgressBar.getValue() + 1);
	}

	public void actionPerformed(ActionEvent e)
	{
		stopSegmentation();
	}
	
	public void presentResults(String sReport)
	{
		System.out.println(sReport);
		JOptionPane.showMessageDialog(m_pFrame, sReport, "Resultado da Segmentação", JOptionPane.INFORMATION_MESSAGE);
	}
}
