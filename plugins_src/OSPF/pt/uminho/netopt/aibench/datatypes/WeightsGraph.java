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
import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;
import pt.uminho.algoritmi.netopt.ospf.simulation.NetworkTopology;
import pt.uminho.algoritmi.netopt.ospf.simulation.OSPFWeights;
import pt.uminho.algoritmi.netopt.ospf.simulation.exception.DimensionErrorException;

@SuppressWarnings("serial")
@Datatype(structure=Structure.COMPLEX,viewable=false,namingMethod="getName",setNameMethod="setName")
public class WeightsGraph implements Serializable{

	private OSPFWeights weights;
	private NetworkTopology topology;
	private String name;
	
	public WeightsGraph(NetworkTopology topology,OSPFWeights weights) throws DimensionErrorException
	{
		this.topology=topology;
		this.weights=weights;
		topology.applyWeights(weights);
	}
	
	public NetworkTopology getTopology() {
		return topology;
	}
	public void setTopology(NetworkTopology topology) {
		this.topology = topology;
	}
	public OSPFWeights getWeights() {
		return weights;
	}
	public void setWeights(OSPFWeights weights) {
		this.weights = weights;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
		
}
