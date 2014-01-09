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

import imagmas.gui.*;

import java.awt.event.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class CStopSegmentationCommand extends AbstractAction
{
	/**
	 * Membro privado para armazenamento do objeto da janela principal.
	 */
	private CMainWindow m_pWindow;
	
	/**
	 * Construtor da classe. Inicializa o comando com a referência da janela principal.
	 * @param pWindow Objeto IMMainWindow com a referência da janela principal.
	 */
	public CStopSegmentationCommand(CMainWindow pWindow)
	{
		super("Parar");
		putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/imagmas/gui/images/Stop.gif")));
		putValue(Action.SHORT_DESCRIPTION, "Para a segmentação em andamento");
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F5, InputEvent.CTRL_MASK));
		
		m_pWindow = pWindow;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		m_pWindow.stopSegmentation();
	}
}
