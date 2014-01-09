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
 
package imagmas.data;

/**
 * Enumeração que representa uma direção de movimentação no ambiente por meio de uma
 * chave significativa relacionada ao ângulo da direção.
 * 
 * @author Luiz Carlos Vieira
 */
public enum EDirection
{
	/**
	 * Nome e ângulo indicativos de direção indefinida (-1 graus). 
	 */
	UNDEFINED (-1),

	/**
	 * Nome e ângulo indicativos de direção leste (0 graus). 
	 */
	EAST (0),
	
	/**
	 * Nome e ângulo indicativos de direção nordeste (45 graus). 
	 */
	NORTHEAST (45),
	
	/**
	 * Nome e ângulo indicativos de direção norte (90 graus).
	 */
	NORTH (90),
	
	/**
	 * Nome e ângulo indicativos de direção noroeste (135 graus).
	 */
	NORTHWEST (135),
	
	/**
	 * Nome e ângulo indicativos de direção oeste (180 graus).
	 */
	WEST (180),
	
	/**
	 * Nome e ângulo indicativos de direção sudoeste (225 graus).
	 */
	SOUTHWEST (225),
	
	/**
	 * Nome e ângulo indicativos de direção sul (270 graus).
	 */
	SOUTH (270),
	
	/**
	 * Nome e ângulo indicativos de direção sudeste (315 graus).
	 */
	SOUTHEAST (315);
	
	/**
	 * Atributo utilizado para armazenar o ângulo correspondente ao nome da direção representada pela enumeração.
	 */
	private int m_iAngle;
	
	/**
	 * Construtor privado (seguindo as regras para a construção de enumerações na linguagem Java)
 	 * Ele é chamado automaticamente pelo JAVA quando uma enumeração é criada a partir do nome,
 	 * para inicializar o atributo com o ângulo correspondente.
	 * 
	 * @param iAngle Valor inteiro com o ângulo correspondente à direção da enumeração.
	 */
	private EDirection(int iAngle)
	{
		m_iAngle = iAngle;
	}
	
	/**
	 * Método getter utilizado para obter o ângulo correspondente à direção atribuída à enumeração.
	 *  
	 * @return Valor inteiro com o ângulo correspondente à direção da enumeração.
	 */
	public int getAngle()
	{
		return m_iAngle;
	}

	public int calcAngularDifferenceTo(EDirection eDir)
	{
		return Math.abs(eDir.getAngle() - m_iAngle);
	}
	
	/**
	 * Método para a obtenção da direção oposta à direção atribuída à enumeração.
	 * @return EDirection com a direção oposta.
	 */
	public EDirection getOpposedDirection()
	{
		switch(m_iAngle)
		{
			case -1: // UNDEFINED
				return EDirection.UNDEFINED;
				
			case 0: // EAST
				return EDirection.WEST;
				
			case 45: // NORTHEAST
				return EDirection.SOUTHWEST;

			case 90: // NORTH
				return EDirection.SOUTH;
				
			case 135: // NORTHWEST
				return EDirection.SOUTHEAST;
				
			case 180: // WEST
				return EDirection.EAST;
				
			case 225: // SOUTHWEST 
				return EDirection.NORTHEAST;
				
			case 270: // SOUTH
				return EDirection.NORTH;
				
			default: // 315 - SOUTHEAST
				return EDirection.NORTHWEST;
		}
	}
	
	/**
	 * Método estático de auxílio para a obtenção de uma enumeração EDirection a partir do valor numérico
	 * do ângulo.
	 * @param iAngle Valor inteiro com o ângulo. Deve ser um entre os valores 0, 45, 90, 135, 180, 225, 270 e 315.
	 * @return
	 */
	public static EDirection getEnumFromAngle(int iAngle)
	{
		switch(iAngle)
		{
			case -1:
				return EDirection.UNDEFINED;
		
			case 0:
				return EDirection.EAST;
				
			case 45:
				return EDirection.NORTHEAST;

			case 90:
				return EDirection.NORTH;

			case 135:
				return EDirection.NORTHWEST;

			case 180:
				return EDirection.WEST;

			case 225:
				return EDirection.SOUTHWEST;

			case 270:
				return EDirection.SOUTH;
				
			case 315:
				return EDirection.SOUTHEAST;
				
			default:
				return null;
		}
	}
	
	/**
	 * Método público para obtenção de uma string de representação da direção.
	 * Utilizado para permitir a fácil impressão de mensagens de depuração
	 * com chamadas de System.out.println, por exemplo. A string retornada segue
	 * o formato "????? (?º)", com o nome da direção e o valor de seu ângulo.
	 * @return String de representação da direção.
	 */
	@Override
	public String toString()
	{
		String sDirName;
		switch(m_iAngle)
		{
			case -1:
				sDirName = "UNDEFINED";
				break;

			case 0:
				sDirName = "EAST";
				break;
				
			case 45:
				sDirName = "NORTHEAST";
				break;

			case 90:
				sDirName = "NORTH";
				break;
				
			case 135:
				sDirName = "NORTHWEST";
				break;
				
			case 180:
				sDirName = "WEST";
				break;
				
			case 225: 
				sDirName = "SOUTHWEST";
				break;
				
			case 270:
				sDirName = "SOUTH";
				break;
				
			default: // 315
				sDirName = "SOUTHEAST";
				break;
		}
		
		return sDirName + " (" + m_iAngle + "º)";
	}
}