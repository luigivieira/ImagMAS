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
 * Classe de ação para configurar o processo de segmentação.
 *
 * @author Luiz Carlos Vieira
 */
@SuppressWarnings("serial")
public class CConfigureSegmentationCommand extends AbstractAction
{
	/**
	 * Construtor da classe. Inicializa o comando com a referência da janela principal.
	 * @param pWindow Objeto IMMainWindow com a referência da janela principal.
	 */
	public CConfigureSegmentationCommand(CMainWindow pWindow)
	{
		super("Configurar...");
		putValue(Action.SHORT_DESCRIPTION, "Encerra o sistema");
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
	}

	/**
	 * Método de execução do comando, chamado automaticamente pela interface gráfica.
	 * Exibe a janela de configuração do processo de segmentação.
	 * @param e Objeto ActionEvent com o evento ocorrido. 
	 */
	public void actionPerformed(ActionEvent e)
	{
		CConfigurationDialog pDlg = new CConfigurationDialog();
		pDlg.setVisible(true);
	}
}
