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
 * Classe de ação para alternar a visão das regiões no resultado da segmentação.
 *
 * @author Luiz Carlos Vieira
 */
@SuppressWarnings("serial")
public class CToggleShowRegionsCommand extends AbstractAction
{
	/**
	 * Membro privado para armazenamento do objeto da janela principal.
	 */
	private CMainWindow m_pWindow;
	
	/**
	 * Construtor da classe. Inicializa o comando com a referência da janela principal.
	 * @param pWindow Objeto IMMainWindow com a referência da janela principal.
	 */
	public CToggleShowRegionsCommand(CMainWindow pWindow)
	{
		super("Exibir/Esconder Regiões");
		putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/imagmas/gui/images/Regions.gif")));
		putValue(Action.SHORT_DESCRIPTION, "Alterna a exibição das regiões no resultado da segmentação");
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_2, InputEvent.ALT_MASK));
		
		m_pWindow = pWindow;
	}

	/**
	 * Método de execução do comando, chamado automaticamente pela interface gráfica.
	 * @param e Objeto ActionEvent com o evento ocorrido. 
	 */
	public void actionPerformed(ActionEvent e)
	{
		m_pWindow.toggleShowRegions();
	}
}