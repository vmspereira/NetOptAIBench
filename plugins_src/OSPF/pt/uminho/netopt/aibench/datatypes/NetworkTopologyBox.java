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
import pt.uminho.algoritmi.netopt.ospf.simulation.net.NetGraph;


@SuppressWarnings("serial")
@Datatype(structure = Structure.COMPLEX, namingMethod = "getName", setNameMethod = "setName")
public class NetworkTopologyBox implements Serializable {
	private NetworkTopology topology;
	private String name;

	public NetworkTopologyBox(String filenameNodes, String filenameEdges) {
		try {
			topology = new NetworkTopology(filenameNodes, filenameEdges);
			this.name="Network Topology";

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public NetworkTopologyBox(NetworkTopology topology) {
			this.topology = topology;
			this.name="Network Topology";
	}
	
    public NetworkTopologyBox() {
    	topology = new NetworkTopology();
    	this.name="Network Topology";
	}


	public NetworkTopologyBox(NetGraph graph) {
		topology = new NetworkTopology(graph);
		this.name="Network Topology";
	}


	/**
     * FIXME 
     * 
     * @return NetworkTopology
     */
	public NetworkTopology getNetworkTopology() {
		return this.topology;
	}
	
	public NetworkTopologyBox copy(){
		return new NetworkTopologyBox(topology.copy());
	}
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
