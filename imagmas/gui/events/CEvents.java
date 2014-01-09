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
 
package imagmas.gui.events;

/**
 * Classe abstrata para a definição de eventos trocados entre a interface gráfica
 * (GUI) e o agente de ambiente.
 * 
 * @author Luiz Carlos Vieira
 */
public abstract class CEvents
{
	static final private int INCOMING_ID = 1000;
	
	static final private int OUTGOING_ID = 2000;
	
	/* *****************************************************************
	 * Eventos da Interface Gráfica para o agente de ambiente
	 ***************************************************************** */
	
	/**
	 * Evento para solicitar ao agente de ambiente o encerramento da aplicação.
	 */
	static final public int INCOMING_EVENT_TERMINATE_APPLICATION = INCOMING_ID + 0;

	/**
	 * Evento para solicitar ao agente de ambiente o início da segmentação da imagem carregada. 
	 */
	static final public int INCOMING_EVENT_START_SEGMENTATION = INCOMING_ID + 1;
	
	/* *****************************************************************
	 * Eventos do agente de ambiente para a Interface Gráfica
	 ***************************************************************** */
	
	/**
	 * Evento para solicitar à interface gráfica a exibição da janela principal.
	 */
	static final public int OUTGOING_EVENT_SHOW_GUI = OUTGOING_ID + 0;
	
	/**
	 * Evento para solicitar à interface gráfica a destruição da janela principal. 
	 */
	static final public int OUTGOING_EVENT_DISPOSE_GUI = OUTGOING_ID + 1;
}
