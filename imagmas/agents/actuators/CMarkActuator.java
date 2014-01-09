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
 
package imagmas.agents.actuators;

import imagmas.data.*;
import imagmas.environment.*;

public class CMarkActuator extends CAbstractActuator implements IActuator
{
	public CMarkActuator(String sName)
	{
		super(sName);
	}
	
	public Object act(Object[] apParameters)
	{
		if(apParameters == null || apParameters.length < 4)
		{
			System.out.println("Erro no atuador de marcas: Número de parâmetros inválido!");
			return false;
		}
		
		CMark pMark;
		String sCommand;
		CLocation pSourceLocation, pTargetLocation;
		
		/*
		 * A matriz de parâmetros deve conter os seguintes parâmetros obrigatórios:
		 * [0] --> String com o comando, que pode ser "create", "delete" ou "move"
		 * [1] --> Objeto CMark com a marca, caso o comando seja "delete" ou "move", ou nulo caso o comando
		 *         seja "create"
		 * [2] --> Localidade atual da marca, caso o comando seja "delete" ou "move", ou nulo caso o comando
		 *         seja "create"
		 * [3] --> Localidade de destino da marca, caso o comando seja "create" ou "move", ou nulo caso o comando
		 *         seja "delete"
		 */
		
		// +--- String de comando
		if(!(apParameters[0] instanceof String))
		{
			System.out.println("Erro no atuador de marcas: parâmetro 'comando' inválido!");
			return false;
		}
		else
			sCommand = (String) apParameters[0];

		/*
		 * Execução. Verifica os parâmetros iniciais e prossegue apropriadamente. Dependendo do comando
		 * os demais parâmetros são apropriadamente verificados.
		 */
		if(sCommand.equals("create"))
		{
			// +--- Localidade de destino para a Marca
			if(!(apParameters[3] instanceof CLocation))
			{
				System.out.println("Erro no atuador de marcas: parâmetro 'localidade de destino para a marca' inválido!");
				return false;
			}
			else
				pTargetLocation = (CLocation) apParameters[3];
			
			pMark = new CMark(pTargetLocation);
			if(CEnvironment.getInstance().setMarkLocation(pMark, null, pTargetLocation) == true)
				return pMark;
			else
			{
				System.out.println("Erro no atuador de marcas: não foi possível criar a marca. Verifique as mensagens anteriores.");
				return false;
			}
		}
		else if(sCommand.equals("delete"))
		{
			// +--- Marca
			if(!(apParameters[1] instanceof CMark))
			{
				System.out.println("Erro no atuador de marcas: parâmetro 'marca' inválido!");
				return false;
			}
			else
				pMark = (CMark) apParameters[1];
			
			// +--- Localidade atual da Marca
			if(!(apParameters[2] instanceof CLocation))
			{
				System.out.println("Erro no atuador de marcas: parâmetro 'localidade atual da marca' inválido!");
				return false;
			}
			else
				pSourceLocation = (CLocation) apParameters[2];

			if(CEnvironment.getInstance().setMarkLocation(pMark, pSourceLocation, null) == true)
				return true;
			else
			{
				System.out.println("Erro no atuador de marcas: não foi possível eliminar a marca. Verifique as mensagens anteriores.");
				return false;
			}
		}
		else if(sCommand.equals("move"))
		{
			// +--- Marca
			if(!(apParameters[1] instanceof CMark))
			{
				System.out.println("Erro no atuador de marcas: parâmetro 'marca' inválido!");
				return false;
			}
			else
				pMark = (CMark) apParameters[1];

			// +--- Localidade atual da Marca
			if(!(apParameters[2] instanceof CLocation))
			{
				System.out.println("Erro no atuador de marcas: parâmetro 'localidade atual da marca' inválido!");
				return false;
			}
			else
				pSourceLocation = (CLocation) apParameters[2];
			
			// +--- Localidade de destino para a Marca
			if(!(apParameters[3] instanceof CLocation))
			{
				System.out.println("Erro no atuador de marcas: parâmetro 'localidade de destino para a marca' inválido!");
				return false;
			}
			else
				pTargetLocation = (CLocation) apParameters[3];
			
			if(CEnvironment.getInstance().setMarkLocation(pMark, pSourceLocation, pTargetLocation) == true)
				return true;
			else
			{
				System.out.println("Erro no atuador de marcas: não foi possível mover a marca. Verifique as mensagens anteriores.");
				return false;
			}
		}
		else
		{
			System.out.println("Erro no atuador de marcas: comando inválido!");
			return false;
		}
	}
}
