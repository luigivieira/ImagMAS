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

import imagmas.algorithms.*;
import imagmas.data.*;
import imagmas.environment.*;
import jadex.runtime.*;

@SuppressWarnings("serial")
public class CAdjustRegionToSolutionPlan extends Plan
{
	@Override
	public void body()
	{
		int i, iX, iY, iRadius, iStartX, iStartY, iEndX, iEndY;
		CLocation pCenter;
		CPixel pPixel;
		byte aiDisc[][];
		
		int iWidth = (Integer) getBeliefbase().getBelief("image_width").getFact();
		int iHeight = (Integer) getBeliefbase().getBelief("image_height").getFact();
		CRegion pRegion = (CRegion) getBeliefbase().getBelief("region").getFact();
		CCrater apCraters[] = (CCrater []) getBeliefbase().getBeliefSet("valid_craters").getFacts();

		pRegion.removeAllPixels();
		
		for(i = 0; i < apCraters.length; i++)
		{
			iRadius = apCraters[i].getRadius();
			pCenter = apCraters[i].getCenter();

			iStartX = pCenter.getX() - iRadius;
			iStartY = pCenter.getY() - iRadius;
			iEndX = pCenter.getX() + iRadius;
			iEndY = pCenter.getY() + iRadius;
			
			aiDisc = CImageProcessingAlgorithms.getDiscStructuringElement(iRadius);
			
			for(iX = iStartX; iX <= iEndX; iX++)
			{
				for(iY = iStartY; iY <= iEndY; iY++)
				{
					if(iX < 0 || iX >= iWidth || iY < 0 || iY >= iHeight)
						continue;
					
					if(aiDisc[iX - iStartX][iY - iStartY] == 0)
						continue;

					pPixel = CEnvironment.getInstance().getPixelAt(new CLocation(iX, iY));
					if(!pRegion.contains(pPixel))
						pRegion.addPixel(pPixel, false);
				}
			}
		}
		
		CEnvironment.getInstance().updateRegion(pRegion);
	}
}
