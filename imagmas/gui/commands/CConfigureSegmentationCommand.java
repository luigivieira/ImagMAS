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
 
package imagmas.gui.commands;

import java.awt.event.*;
import javax.swing.*;
import imagmas.gui.*;

/**
 * Classe de a��o para configurar o processo de segmenta��o.
 *
 * @author Luiz Carlos Vieira
 */
@SuppressWarnings("serial")
public class CConfigureSegmentationCommand extends AbstractAction
{
	/**
	 * Construtor da classe. Inicializa o comando com a refer�ncia da janela principal.
	 * @param pWindow Objeto IMMainWindow com a refer�ncia da janela principal.
	 */
	public CConfigureSegmentationCommand(CMainWindow pWindow)
	{
		super("Configurar...");
		putValue(Action.SHORT_DESCRIPTION, "Encerra o sistema");
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
	}

	/**
	 * M�todo de execu��o do comando, chamado automaticamente pela interface gr�fica.
	 * Exibe a janela de configura��o do processo de segmenta��o.
	 * @param e Objeto ActionEvent com o evento ocorrido. 
	 */
	public void actionPerformed(ActionEvent e)
	{
		CConfigurationDialog pDlg = new CConfigurationDialog();
		pDlg.setVisible(true);
	}
}
