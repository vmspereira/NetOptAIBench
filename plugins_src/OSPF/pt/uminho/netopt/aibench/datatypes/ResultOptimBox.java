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

@SuppressWarnings("serial")
@Datatype(structure = Structure.LIST, viewable = false, namingMethod = "getName", setNameMethod = "setName")
public class ResultOptimBox extends Observable implements Serializable{
	private ArrayList<ResultOptimType> results;
	private String name;

	public ResultOptimBox() {
		this.results = new ArrayList<ResultOptimType>();
		this.name="Optmization Results";
	}

	public void addResultOptim(ResultOptimType results) {
		this.results.add(results);
		this.setChanged();
		this.notifyObservers();
	}

	public void setResults(ArrayList<ResultOptimType> results) {
		this.results = results;
	}

	@ListElements
	public ArrayList<ResultOptimType> getResults() {
		return results;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

}
