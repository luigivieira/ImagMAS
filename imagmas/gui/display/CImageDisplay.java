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
 
package imagmas.gui.display;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class CImageDisplay extends JPanel
{
	private BufferedImage m_pImage;
	
	public CImageDisplay()
	{
		m_pImage = null;
	}
	
	public void setImage(BufferedImage pImage)
	{
		m_pImage = pImage;
		if(m_pImage != null)
		{
			setPreferredSize(new Dimension(m_pImage.getWidth(), m_pImage.getHeight()));
			setBounds(0, 0, m_pImage.getWidth(), m_pImage.getHeight());
			setVisible(true);
		}
		else
		{
			setPreferredSize(new Dimension(10, 10));
			setBounds(0, 0, 10, 10);
			setVisible(false);
		}
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		if(m_pImage != null)
		{
			Graphics2D pG2 = (Graphics2D) g;
			pG2.drawImage(m_pImage, null, 0, 0);
		}
	}
}
