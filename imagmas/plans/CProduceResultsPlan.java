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
 
package imagmas.plans;

import imagmas.data.*;
import imagmas.gui.*;
import jadex.runtime.*;

@SuppressWarnings("serial")
public class CProduceResultsPlan extends Plan
{
	@Override
	public void body()
	{
		double dDuration = (System.currentTimeMillis() - ((Long) getBeliefbase().getBelief("segmentation_start_time").getFact())) / 1000.0;
		String sNewLine = String.valueOf((char) 13) + String.valueOf((char) 10);
		String sReport;
		CRegion apRegions[] = (CRegion []) getBeliefbase().getBeliefSet("existing_regions").getFacts();

		int iImageWidth = (Integer) getBeliefbase().getBelief("image_width").getFact();
		int iImageHeight = (Integer) getBeliefbase().getBelief("image_height").getFact();
		long iImageArea = iImageWidth * iImageHeight;
		
		sReport = "Número de regiões processadas: " + apRegions.length + sNewLine;
		
		int iHours = (int) (dDuration / 3600);
		dDuration = dDuration - (iHours * 3600);
		
		int iMinutes = (int) (dDuration / 60);
		dDuration = dDuration - (iMinutes * 60);
		
		int iSeconds = (int) dDuration;
		int iMiliseconds = (int) ((dDuration - ((int) dDuration)) * 1000);
		
		sReport += "Duração da segmentação: " + String.format("%02d:%02d:%02d - %03dms", iHours, iMinutes, iSeconds, iMiliseconds) + sNewLine;
		
		long iHitArea = 0;
		for(int i = 0; i < apRegions.length; i++)
			iHitArea += apRegions[i].getPixelCount();

		double dCoverage = (((double) iHitArea) / ((double) iImageArea)) * 100.0;
		
		sReport += "---------------------------------------------------------------" + sNewLine;
		sReport += "Área da imagem: " + iImageArea + sNewLine;
		sReport += "Area dos impactos: " + iHitArea + sNewLine;
		sReport += "Cobertura: " + String.format("%3.2f", dCoverage) + "%";
		
		getBeliefbase().getBelief("segmentation_concluded").setFact(false);
		
		CMainWindow pWindow = (CMainWindow) getBeliefbase().getBelief("main_window").getFact();
		pWindow.presentResults(sReport);
	}
}
