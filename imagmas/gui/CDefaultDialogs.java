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

import java.awt.*;
import javax.swing.*;

public abstract class CDefaultDialogs
{
	/**
	 * Gera um beep seguido da exibi��o de uma mensagem de alerta composta
	 * por um t�tulo e uma descri��o.
	 * @param sTitle Texto do t�tulo da janela de di�logo da mensagem.
	 * @param sMessage Texto com a mensagem a ser exibida.
	 */
	public static void showAlertMessage(String sTitle, String sMessage)
	{
		Toolkit.getDefaultToolkit().beep();
		JOptionPane.showMessageDialog(null, sMessage, sTitle, 0);
	}
}
