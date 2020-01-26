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
* @author Vítor Pereira
*/
package pt.uminho.netopt.aibench.datatypes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;

import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.ListElements;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;
import pt.uminho.algoritmi.netopt.ospf.simulation.NetworkLoads;

@SuppressWarnings("serial")
@Datatype(structure = Structure.LIST, viewable = false, namingMethod = "getName", setNameMethod = "setName")
public class NetworkLoadBox extends Observable implements Serializable{
	private ArrayList<NetworkLoads> loads;
	private String name;

	public NetworkLoadBox() {
		this.loads = new ArrayList<NetworkLoads>();
		this.name="Network Loads Box";
	}

	public void addNetworkLoads(NetworkLoads loads) {
		this.loads.add(loads);
		this.setChanged();
		this.notifyObservers();
	}

	
	@ListElements
	public ArrayList<NetworkLoads> getNetworkLoads() {
		return loads;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLoads(ArrayList<NetworkLoads> networkLoads) {
		this.loads=networkLoads;
	}

}
