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
package pt.uminho.netopt.aibench.operations.statistics;

import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import pt.uminho.algoritmi.netopt.ospf.simulation.NetworkTopology;
import pt.uminho.algoritmi.netopt.ospf.simulation.OSPFWeights;
import pt.uminho.algoritmi.netopt.ospf.simulation.exception.DimensionErrorException;
import pt.uminho.algoritmi.netopt.ospf.simulation.net.NetNode.NodeType;
import pt.uminho.netopt.aibench.datatypes.ProjectBox;
import pt.uminho.netopt.aibench.datatypes.WeightsGraph;

@Operation(name = "WeightGraphOp", description = "OSPF paths")
public class WeightsGraphOp {
	
	private ProjectBox pb;
	private OSPFWeights weights;
	
	
	
	@Port(direction = Direction.INPUT, name = "Topology", order = 1)
	public void setNetworkTopology(ProjectBox pb) {
		this.pb = pb;
	}
	
	@Port(direction = Direction.INPUT, name = "Weights", order = 2)
	public void setWeights1(OSPFWeights weights) {
		this.weights = weights;
		try {
			run();
		} catch (DimensionErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	//@Port(direction=Direction.OUTPUT,order=3)
	public void run() throws DimensionErrorException{
		NetworkTopology topo=pb.getNetworkTopologyBox().getNetworkTopology().copy();
		topo.getNetGraph().setAllNodesType(NodeType.SDN_SR);
		WeightsGraph wg=new WeightsGraph(topo,weights);
		pb.addWeightGraph(wg);
	}
	
	
	
		
	
	
}
