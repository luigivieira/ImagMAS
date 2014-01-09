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
import java.io.*;

import javax.swing.*;
import imagmas.gui.*;

/**
 * Classe de ação para efetuar o carregamento de uma imagem local no sistema.
 *
 * @author Luiz Carlos Vieira
 */
@SuppressWarnings("serial")
public class CLoadLocalCommand extends AbstractAction
{
	/**
	 * Membro privado para armazenamento do objeto da janela principal.
	 */
	private CMainWindow m_pWindow;
	
	/**
	 * Construtor da classe. Inicializa o comando com a referência da janela principal.
	 * @param pWindow Objeto IMMainWindow com a referência da janela principal.
	 */
	public CLoadLocalCommand(CMainWindow pWindow)
	{
		super("Carregar Local...");
		putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/imagmas/gui/images/Open.gif")));
		putValue(Action.SHORT_DESCRIPTION, "Carrega uma imagem local no sistema");
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK));
		
		m_pWindow = pWindow;
	}

	/**
	 * Método de execução do comando, chamado automaticamente pela interface gráfica.
	 * Exibe uma janela de diálogo do tipo "Abrir Arquivo" para permitir ao usuário
	 * selecionar a imagem a ser carregada.
	 * @param e Objeto ActionEvent com o evento ocorrido. 
	 */
	public void actionPerformed(ActionEvent e)
	{
		JFileChooser pDlg = new JFileChooser();
		pDlg.setSelectedFile(new File("C:\\ImagMAS\\teste.jpg"));
		int iRet = pDlg.showOpenDialog(null);
		
		if(iRet == JFileChooser.APPROVE_OPTION)
		{
			if(!m_pWindow.loadImage(pDlg.getSelectedFile()))
				CDefaultDialogs.showAlertMessage("Erro", "Não foi possível abrir a imagem local.");
			else
				m_pWindow.updateSegmentation();
		}
	}
}