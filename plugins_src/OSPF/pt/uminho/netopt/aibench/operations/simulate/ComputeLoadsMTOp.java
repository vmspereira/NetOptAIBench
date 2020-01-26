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
package pt.uminho.netopt.aibench.operations.simulate;

import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import pt.uminho.algoritmi.netopt.ospf.simulation.Demands;
import pt.uminho.algoritmi.netopt.ospf.simulation.NetworkLoads;
import pt.uminho.algoritmi.netopt.ospf.simulation.NetworkTopology;
import pt.uminho.algoritmi.netopt.ospf.simulation.OSPFWeights;
import pt.uminho.algoritmi.netopt.ospf.simulation.ResultSimul;
import pt.uminho.algoritmi.netopt.ospf.simulation.exception.DimensionErrorException;
import pt.uminho.netopt.aibench.datatypes.ProjectBox;
import pt.uminho.netopt.aibench.datatypes.ResultSimulType;

@Operation(name = "ComputeLoadsOp", description = "Compute MT Loads and Congestion")
public class ComputeLoadsMTOp {
	private ProjectBox projB;
	private OSPFWeights[] weights=new OSPFWeights[4];
	private Demands demands;
	private int ntopologies;

	@Port(direction = Direction.INPUT, name = "Project", order = 1)
	public void setProject(ProjectBox projB) {
		this.projB = projB;
	}
	
	@Port(direction = Direction.INPUT, name = "N.º of Topologies", order = 2,defaultValue = "2")
	public void getWeights(int ntopologies) {
		this.ntopologies = ntopologies;
	}


	@Port(direction = Direction.INPUT, name = "OSPFWeights TP0", order = 3)
	public void getWeights0(OSPFWeights weights) {
		this.weights[0] = weights;
	}

	
	@Port(direction = Direction.INPUT, name = "OSPFWeights TP1", order = 4)
	public void getWeights1(OSPFWeights weights) {
		this.weights[1] = weights;
	}
	
	@Port(direction = Direction.INPUT, name = "OSPFWeights TP2", order = 5)
	public void getWeights2(OSPFWeights weights) {
		this.weights[2] = weights;
	}
	
	@Port(direction = Direction.INPUT, name = "OSPFWeights TP3", order = 6)
	public void getWeights3(OSPFWeights weights) {
		this.weights[3] = weights;
	}
	
	@Port(direction = Direction.INPUT, name = "Total Demands", order = 7)
	public void getDemands(Demands demands) throws DimensionErrorException {
		this.demands = demands;
		computeLoads();
	}

	public void computeLoads() throws DimensionErrorException {
		
		NetworkTopology topology = projB.getNetworkTopologyBox()
				.getNetworkTopology();
		int n = ntopologies;
		
		Demands d = new Demands(demands.getDimension());
		d.setFilename(demands.getFilename() + " 1/" + n + " part");

		for (int i = 0; i < demands.getDimension(); i++) {
			for (int j = 0; j < demands.getDimension(); j++) {
				d.setDemands(i, j, demands.getDemands(i, j) / n);
			}
		}
		
		
		NetworkLoads loads = new NetworkLoads(topology);
		ResultSimul results = new ResultSimul();
		for(int i=0;i<n;i++){
			projB.getSimul().computeLoads(weights[i],d);
			NetworkLoads ld = projB.getSimul().getLoads();
			ld.setName("Topology "+i+" with Partial demands");
			loads.addLoads(ld.getLoads());
		}
	
		double f=projB.getSimul().congestionMeasure(loads, demands);
		
		loads.setCongestion(f);
		loads.setName("Total loads");
		results.addNetworkLoads(loads);
		
		ResultSimulType resultsT = new ResultSimulType(results);
		projB.addResultSimul(resultsT);
	}
}
