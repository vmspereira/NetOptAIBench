/**
* Copyright 2012-2017,
* Centro Algoritmi
* University of Minho
*
* This is free software: you can redistribute it and/or modify
* it under the terms of the GNU Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This code is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU Public License for more details.
*
* You should have received a copy of the GNU Public License
* along with this code.  If not, see <http://www.gnu.org/licenses/>.
* 
* @author V�tor Pereira
*/
package pt.uminho.netopt.aibench.datatypes;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;

import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.ListElements;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;
import pt.uminho.algoritmi.netopt.ospf.simulation.sr.SRSimulator;

@SuppressWarnings("serial")
@Datatype(structure = Structure.LIST, viewable = false, namingMethod = "getName", setNameMethod = "setName")
public class SRSimulatorBox extends Observable implements Serializable{

	private ArrayList<SRSimulator> simulators;
	private String name;
	
	public SRSimulatorBox(){
		this.simulators=new ArrayList<SRSimulator>();
		this.name="SR Simulator Box";
	}

	public void add(SRSimulator p) {
		this.simulators.add(p);
		this.setChanged();
		this.notifyObservers();
	}

	@ListElements
	public ArrayList<SRSimulator> getValues() {
		return simulators;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
