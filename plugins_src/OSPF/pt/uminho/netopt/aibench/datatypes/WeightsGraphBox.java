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

@SuppressWarnings("serial")
@Datatype(structure = Structure.LIST,namingMethod = "getName", setNameMethod = "setName")
public class WeightsGraphBox extends Observable implements Serializable {

	private ArrayList<WeightsGraph> weigthGraphs;
	private String name;

	public void addWeightsGraph(WeightsGraph wc) {
		this.weigthGraphs.add(wc);
		this.setChanged();
		this.notifyObservers();
	}
	
	
	public WeightsGraphBox() {
		this.weigthGraphs = new ArrayList<WeightsGraph>();
		this.name="Path Graphs Box";
	}
	
	@ListElements
	public ArrayList<WeightsGraph> getWeigthGraphs() {
		return weigthGraphs;
	}

	public void setWeigthGraphs(ArrayList<WeightsGraph> weigthGraphs) {
		this.weigthGraphs = weigthGraphs;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
