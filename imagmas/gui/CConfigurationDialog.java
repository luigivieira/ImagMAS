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
import imagmas.configuration.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


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
@SuppressWarnings("serial")
public class CConfigurationDialog extends JDialog implements ActionListener
{
	private JPanel pButtonsPanel;
	private JSpinner pDifferentiationFactor;
	private JLabel pLabelEquivalence;
	private JSpinner pMarksDistance;
	private JLabel pLabelDistance;
	private JSpinner pInclusionFactor;
	private JLabel pLabelMaxRadius;
	private JSpinner pMinimumRadius;
	private JLabel pLabelInclusion;
	private JLabel pLabelRadius;
	private JPanel pElementsPanel;
	private JSpinner pMaximumRadius;
	private JButton pCancelButton;
	private JButton pOkButton;

	public CConfigurationDialog()
	{
		initGUI();
		Center();
	}
	
	private void initGUI() {
		try {
			{
				this.setTitle("Configurar segmentação...");
				this.setModal(true);
				this.setName("pCfgDialog");
				this.setResizable(false);
				this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				{
					pButtonsPanel = new JPanel();
					FlowLayout pButtonsPanelLayout = new FlowLayout();
					pButtonsPanelLayout.setAlignOnBaseline(true);
					pButtonsPanelLayout.setAlignment(FlowLayout.RIGHT);
					getContentPane().add(pButtonsPanel, BorderLayout.SOUTH);
					pButtonsPanel.setLayout(pButtonsPanelLayout);
					pButtonsPanel.setPreferredSize(new java.awt.Dimension(437, 41));
					{
						pOkButton = new JButton();
						pButtonsPanel.add(pOkButton);
						pOkButton.setText("Ok");
						pOkButton.setPreferredSize(new java.awt.Dimension(92, 24));
						pOkButton.addActionListener(this);
					}
					{
						pCancelButton = new JButton();
						pButtonsPanel.add(pCancelButton);
						pCancelButton.setText("Cancelar");
						pCancelButton.setSize(77, 24);
						pCancelButton.setPreferredSize(new java.awt.Dimension(92, 24));
						pCancelButton.addActionListener(this);
					}
				}
				{
					pElementsPanel = new JPanel();
					getContentPane().add(pElementsPanel, BorderLayout.CENTER);
					FlowLayout pElementsPanelLayout = new FlowLayout();
					pElementsPanelLayout.setAlignment(FlowLayout.RIGHT);
					pElementsPanel.setLayout(pElementsPanelLayout);
					pElementsPanel.setPreferredSize(new java.awt.Dimension(265, 113));
					{
						pLabelRadius = new JLabel();
						pElementsPanel.add(pLabelRadius);
						pLabelRadius.setText("Raio Mínimo:");
						pLabelRadius.setPreferredSize(new java.awt.Dimension(80, 17));
					}
					{
						pMinimumRadius = new JSpinner();
						pElementsPanel.add(pMinimumRadius);
						pMinimumRadius.setModel(new SpinnerNumberModel(CConfiguration.getMinimumRadius(), 1, 64, 1));
						pMinimumRadius.setPreferredSize(new java.awt.Dimension(114, 24));
						pMinimumRadius.getEditor().setToolTipText("Informe aqui o raio mínimo para uma cratera ser considerada válida pelos agentes.");
						pMinimumRadius.setToolTipText("Informe aqui o raio mínimo para uma cratera ser considerada válida pelos agentes.");
					}
					{
						pLabelMaxRadius = new JLabel();
						pElementsPanel.add(pLabelMaxRadius);
						pLabelMaxRadius.setText("Raio Máximo:");
					}
					{
						pMaximumRadius = new JSpinner();
						pElementsPanel.add(pMaximumRadius);
						pMaximumRadius.setModel(new SpinnerNumberModel(CConfiguration.getMaximumRadius(), 1, 64, 1));
						pMaximumRadius.setPreferredSize(new java.awt.Dimension(114, 24));
					}
					{
						pLabelDistance = new JLabel();
						pElementsPanel.add(pLabelDistance);
						pLabelDistance.setText("Distância entre Marcas:");
					}
					{
						pMarksDistance = new JSpinner();
						pElementsPanel.add(pMarksDistance);
						pMarksDistance.setModel(new SpinnerNumberModel(CConfiguration.getMarksDistance(), 1, 64, 1));
						pMarksDistance.setPreferredSize(new java.awt.Dimension(114, 24));
					}
					{
						pLabelInclusion = new JLabel();
						pElementsPanel.add(pLabelInclusion);
						pLabelInclusion.setText("Fator de Inclusão:");
						pLabelInclusion.setPreferredSize(new java.awt.Dimension(112, 17));
					}
					{
						pInclusionFactor = new JSpinner();
						pElementsPanel.add(pInclusionFactor);
						pInclusionFactor.setModel(new SpinnerNumberModel(CConfiguration.getInclusionFactor(), 0.0, 1.0, 0.1));
						pInclusionFactor.setPreferredSize(new java.awt.Dimension(114, 24));
						pInclusionFactor.getEditor().setPreferredSize(new java.awt.Dimension(44, 20));
						pInclusionFactor.getEditor().setToolTipText("Informe aqui o fator de inclusão para que crateras sobrepostas sejam absorvidas (entre 0 e 1).");
						pInclusionFactor.setToolTipText("Informe aqui o fator de inclusão para que crateras sobrepostas sejam absorvidas (entre 0 e 1).");
					}
					{
						pLabelEquivalence = new JLabel();
						pElementsPanel.add(pLabelEquivalence);
						pLabelEquivalence.setText("Fator de Diferenciação:");
						pLabelEquivalence.setPreferredSize(new java.awt.Dimension(143, 17));
					}
					{
						pDifferentiationFactor = new JSpinner();
						pElementsPanel.add(pDifferentiationFactor);
						pDifferentiationFactor.setModel(new SpinnerNumberModel(CConfiguration.getDifferentiationFactor(), 0.0, 1.0, 0.1));
						pDifferentiationFactor.getEditor().setPreferredSize(new java.awt.Dimension(70, 20));
						pDifferentiationFactor.setPreferredSize(new java.awt.Dimension(114, 24));
						pDifferentiationFactor.getEditor().setToolTipText("Informe aqui o fator de diferenciação entre as crateras (entre 0 e 1).");
						pDifferentiationFactor.setToolTipText("Informe aqui o fator de diferenciação entre as crateras (entre 0 e 1).");
					}
				}
			}
			{
				this.setSize(283, 224);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void actionPerformed(ActionEvent pEvent)
	{
		if(pEvent.getSource().equals(pOkButton))
		{
			CConfiguration.setMinimumRadius((Integer) pMinimumRadius.getValue());
			CConfiguration.setMaximumRadius((Integer) pMaximumRadius.getValue());
			CConfiguration.setMarksDistance((Integer) pMarksDistance.getValue());
			CConfiguration.setInclusionFactor((Double) pInclusionFactor.getValue());
			CConfiguration.setDifferentiationFactor((Double) pDifferentiationFactor.getValue());
		}
		dispose();
	}
	
	private void Center()
	{
		Dimension pScreen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle pFrame = getBounds();
		setLocation((pScreen.width - pFrame.width)/2, (pScreen.height - pFrame.height)/2);
	}
}
