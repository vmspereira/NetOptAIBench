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
import pt.uminho.algoritmi.netopt.ospf.simulation.OSPFWeights;

@SuppressWarnings("serial")
@Datatype(structure = Structure.LIST, viewable = false, namingMethod = "getName", setNameMethod = "setName")
public class OSPFWeightsBox extends Observable implements Serializable{
	private ArrayList<OSPFWeights> weights;
	private String name;

	public OSPFWeightsBox() {
		this.weights = new ArrayList<OSPFWeights>();
		this.name="OSPF Weights Box";
	}

	public void addOSPFWeights(OSPFWeights weights) {
		this.weights.add(weights);
		this.setChanged();
		this.notifyObservers();
	}

	public void setWeights(ArrayList<OSPFWeights> weights) {
		this.weights = weights;
	}

	@ListElements
	public ArrayList<OSPFWeights> getWeights() {
		return weights;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
