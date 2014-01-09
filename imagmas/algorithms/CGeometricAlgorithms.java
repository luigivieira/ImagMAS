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
 
package imagmas.algorithms;

import java.util.*;

import imagmas.algorithms.data.CVertex;
import imagmas.data.*;
import imagmas.external.voronoi.*;

public abstract class CGeometricAlgorithms
{
	public static ArrayList<CVertex> calcVoronoiVertices(CLocation[] apPoints, ArrayList<CLocation> apValidPoints)
	{
		// +--- Usando a biblioteca externa, calcula o diagrama de voronoi para as localidades dadas
		Triangulation pTriangulation;
		Triangle pInitTriangle = new Triangle(
                						new Pnt(-10000, -10000),
                						new Pnt(10000, -10000),
                						new Pnt(0, 10000) );
        pTriangulation = new Triangulation(pInitTriangle);
		
        for(int i = 0; i < apPoints.length; i++)
        	pTriangulation.delaunayPlace(new Pnt(apPoints[i].getX(), apPoints[i].getY()));

        // +--- A partir desse diagrama, identifica os vértices dentro dos limites da região
        CVertex pVertex;
        CLocation pLocation;
        Pnt pVertexPoint;
        ArrayList<CVertex> apVertices = new ArrayList<CVertex>();
        ArrayList<CLocation> apVertexPoints;
        
        for(Triangle pTriangle: pTriangulation)
        {
        	apVertexPoints = new ArrayList<CLocation>();
        	for(int iPnt = 0; iPnt < pTriangle.size(); iPnt++)
        	{
        		pVertexPoint = pTriangle.get(iPnt);
        		apVertexPoints.add(new CLocation((int) pVertexPoint.coord(0), (int) pVertexPoint.coord(1)));
        	}
        	
        	pVertexPoint = pTriangle.getCircumcenter();
        	pLocation = new CLocation((int) pVertexPoint.coord(0), (int) pVertexPoint.coord(1));
        	
        	pVertex = new CVertex(pLocation, (int) pVertexPoint.subtract(pTriangle.get(0)).magnitude(), apVertexPoints);
        	
        	// +--- Somente considera os vértices não duplicados e que estejam dentre os pontos válidos
        	if(!apVertices.contains(pVertex) && apValidPoints.contains(pLocation))
       			apVertices.add(pVertex);
        }
        
        return apVertices;
	}
	
	public static ArrayList<CVertex> filterByObservation(ArrayList<CVertex> apVertices)
	{
		Stack<CVertex> apAvailables = new Stack<CVertex>();
		apAvailables.addAll(apVertices);
		
		ArrayList<CVertex> apVisibles;
		boolean bIMustDie;
		
		CVertex pVertex = apAvailables.size() > 0 ? apAvailables.pop() : null;
		while(pVertex != null)
		{
			bIMustDie = false;
			apVisibles = observeNeighbourhood(pVertex, apVertices);
			
			for(CVertex pOtherVertex: apVisibles)
			{
				if(pOtherVertex.getRadius() < pVertex.getRadius())
				{
					apAvailables.remove(pOtherVertex);
					apVertices.remove(pOtherVertex);
				}
				else if(pOtherVertex.getRadius() > pVertex.getRadius())
					bIMustDie = true;
			}
			
			if(bIMustDie)
			{
				apAvailables.remove(pVertex);
				apVertices.remove(pVertex);
			}
			
			pVertex = apAvailables.size() > 0 ? apAvailables.pop() : null;
		}
		
		return apVertices;
	}
	
	public static ArrayList<CVertex> observeNeighbourhood(CVertex pVertex, ArrayList<CVertex> apVertices)
	{
		ArrayList<CVertex> apVisibles = new ArrayList<CVertex>();
		int iDistance;
		
		for(CVertex pOtherVertex: apVertices)
		{
			iDistance = (int) pVertex.getLocation().calcDistanceFrom(pOtherVertex.getLocation());
			if(iDistance < 15)
				apVisibles.add(pOtherVertex);
		}
		
		return apVisibles;
	}

	public static Object[] calcMinimumCircle(CLocation apPoints[])
	{
		int i, j, iDistance;
		int iFirstMaxDistance = Integer.MIN_VALUE;
		int iSecondMaxDistance = Integer.MIN_VALUE;
		CLocation apPairs[] = new CLocation[4];
		
		for(i = 0; i < apPoints.length; i++)
		{
			for(j = 0; j < apPoints.length; j++)
			{
				if(i == j)
					continue;
			
				iDistance = (int) apPoints[i].calcDistanceFrom(apPoints[j]);
				
				if(iDistance > iFirstMaxDistance)
				{
					apPairs[0] = apPoints[i];
					apPairs[1] = apPoints[j];
					iFirstMaxDistance = iDistance;
				}
				else if(iDistance > iSecondMaxDistance)
				{
					apPairs[2] = apPoints[i];
					apPairs[3] = apPoints[j];
					iSecondMaxDistance = iDistance;
				}
			}
		}
		
		CLocation pCenter = CLocation.calcCentroidLocation(apPairs);
		int iRadius = Integer.MIN_VALUE;
		
		for(i = 0; i < apPairs.length; i++)
		{
			iDistance = (int) pCenter.calcDistanceFrom(apPairs[i]);
			if(iDistance > iRadius)
				iRadius = iDistance;
		}
		
		
		Object aRet[] = new Object[2];
		aRet[0] = pCenter;
		aRet[1] = iRadius;
		return aRet;
	}
	
	public static double calcCircleArea(int iRadius)
	{
		return (iRadius * iRadius) * Math.PI;
	}

	public static double calcIntersectionArea(CLocation pCenterA, int iRadiusA, CLocation pCenterB, int iRadiusB)
	{
		int iRadiusAPow = iRadiusA * iRadiusA;
		int iRadiusBPow = iRadiusB * iRadiusB;
		double dDistance = pCenterA.calcDistanceFrom(pCenterB);
		double dDistancePow = dDistance * dDistance;
		double dAngleA, dAngleB, dAreaA, dAreaB;
		
		/*
		 * Primeiramente, calcula o ângulo (em radianos) formado pelo centro e os dois pontos de interseção
		 * para cada um dos círculos (ver dissertação sobre explicação da fórmula).
		 */
		dAngleA = Math.acos((iRadiusAPow + dDistancePow - iRadiusBPow) / (2 * dDistance * iRadiusA)) * 2;
		
		dAngleB = Math.acos((iRadiusBPow + dDistancePow - iRadiusAPow) / (2 * dDistance * iRadiusB)) * 2;
		
		/*
		 * Então, calcula a área do setor circular e do triângulo entre esses pontos, também para cada
		 * círculo. A área de interseção é a soma da diferença entre a área do setor circular e do triângulo
		 * dos dois círculos (novamente, ver dissertação para detalhes).
		 */
		dAreaA = ((iRadiusAPow * dAngleA) / 2) - ((iRadiusAPow * Math.sin(dAngleA)) / 2);
		
		dAreaB = ((iRadiusBPow * dAngleB) / 2) - ((iRadiusBPow * Math.sin(dAngleB)) / 2);
		
		return dAreaA + dAreaB;
	}
	
	public static double calcDifferenceFactor(int iRadius1, int iRadius2)
	{
		return (double) iRadius1 / (double) iRadius2;
	}
}