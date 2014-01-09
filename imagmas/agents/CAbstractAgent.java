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
 
package imagmas.agents;

import imagmas.agents.actuators.*;
import imagmas.agents.sensors.*;

import java.util.*;
import jadex.adapter.fipa.*;

/**
 * Classe básica (abstrata) para representação de um agente. 
 * 
 * @author Luiz Carlos Vieira
 */
@SuppressWarnings("serial")
public abstract class CAbstractAgent
{
	/**
	 * Objeto original do JADEX para identificação do agente.
	 */
	private AgentIdentifier m_pAgentIdentifier;
	
	/**
	 * Mapa com os sensores disponíveis ao agente.
	 */
	private HashMap<String, CAbstractSensor> m_mpSensors;

	/**
	 * Mapa com os atuadores disponíveis ao agente.
	 */
	private HashMap<String, CAbstractActuator> m_mpActuators;

	public CAbstractAgent(AgentIdentifier pAgentIdentifier)
	{
		if(pAgentIdentifier == null)
			m_pAgentIdentifier = null;
		else
			m_pAgentIdentifier = (AgentIdentifier) pAgentIdentifier.clone();
		m_mpSensors = new HashMap<String, CAbstractSensor>();
		m_mpActuators = new HashMap<String, CAbstractActuator>();
	}
	
	public void setName(String sName)
	{
		if(sName == null)
			m_pAgentIdentifier = null;
		else
			m_pAgentIdentifier = new AgentIdentifier(sName, true);
	}
	
	public String getName()
	{
		return m_pAgentIdentifier.getLocalName();
	}
	
	public AgentIdentifier getAgentIdentifier()
	{
		return m_pAgentIdentifier;
	}
	
	public void addSensor(CAbstractSensor pSensor)
	{
		m_mpSensors.put(pSensor.getName(), pSensor);
		pSensor.setAgent(this);
	}
	
	public CAbstractSensor getSensor(String sName)
	{
		return m_mpSensors.get(sName);
	}

	public void addActuator(CAbstractActuator pActuator)
	{
		m_mpActuators.put(pActuator.getName(), pActuator);
		pActuator.setAgent(this);
	}

	public CAbstractActuator getActuator(String sName)
	{
		return m_mpActuators.get(sName);
	}
	
	/**
	 * Método de comparação utilizado para verificar se uma outra instância de objeto
	 * CAgentInfo representa os mesmos dados de agente do que o objeto atual. É empregado automaticamente
	 * por listas e mapas da linguagem java para comparação em operações de adição e remoção,
	 * e também pode ser utilizado diretamente para rapidamente comparar duas informações de agentes.
	 * 
	 * @param pData Objeto da classe Object. Deve conter uma abstração de objeto da classe CAgentInfo,
	 * caso contrário a função retornará sempre falso.
	 * @return Retorna verdadeiro se o objeto de informação de agente informado contém o mesmo agente
	 * que o objeto atual. Caso contrário, retorna falso.
	 */
	@Override
	public boolean equals(Object pData)
	{
		return (pData instanceof CAbstractAgent) && m_pAgentIdentifier.getLocalName().equals(((CAbstractAgent) pData).getName());
	}
	
	/**
	 * Método público para obtenção de uma string de representação dos dados do agente.
	 * Utilizado para permitir a fácil impressão de mensagens de depuração
	 * com chamadas de System.out.println, por exemplo. A string retornada segue
	 * o formato "<Noe do Agente>".
	 * @return String de representação das informações do agente.
	 */
	@Override
	public String toString()
	{
		if(m_pAgentIdentifier == null)
			return "Indefinido";
		else
			return m_pAgentIdentifier.getLocalName();
	}
}
