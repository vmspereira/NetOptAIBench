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
import pt.uminho.algoritmi.netopt.ospf.simulation.OSPFWeights;
import pt.uminho.algoritmi.netopt.ospf.simulation.PValues;
import pt.uminho.algoritmi.netopt.ospf.simulation.ResultSimul;
import pt.uminho.algoritmi.netopt.ospf.simulation.Simul.LoadBalancer;
import pt.uminho.algoritmi.netopt.ospf.simulation.exception.DimensionErrorException;
import pt.uminho.algoritmi.netopt.ospf.simulation.simulators.SRSimul;
import pt.uminho.netopt.aibench.datatypes.ProjectBox;
import pt.uminho.netopt.aibench.datatypes.ResultSimulType;

@Operation(name = "ComputeLoadsOp", description = "Compute Loads and Congestion for Single Adjacency SR")
public class ComputeLoadsSRPValueOp {
	private ProjectBox projB;
	private OSPFWeights weights;
	private PValues p;
	private Demands demands;
	private boolean useDEFT;

	@Port(direction = Direction.INPUT, name = "Project", order = 1)
	public void setProject(ProjectBox projB) {
		this.projB = projB;
	}

	@Port(direction = Direction.INPUT, name = "OSPFWeights", order = 2)
	public void getWeights(OSPFWeights weights) {
		this.weights = weights;
	}
	
	
	@Port(direction = Direction.INPUT, name = "P Values", order = 3)
	public void getPValues(PValues p) {
		this.p = p;
	}

	
	@Port(direction = Direction.INPUT, name = "Use DEFT", order = 4)
	public void getUseDEFT(boolean usedeft) throws DimensionErrorException {
		this.useDEFT=usedeft;
	}
	
	
	@Port(direction = Direction.INPUT, name = "Demands", order = 5)
	public void getDemands(Demands demands) throws DimensionErrorException {
		this.demands = demands;
		computeLoads();
	}
	
	

	

	public void computeLoads() throws DimensionErrorException {
		
		ResultSimul results = new ResultSimul();
		results.addDemands(demands);
		results.addWeights(weights);
		LoadBalancer lb= LoadBalancer.DEFT;
		if(!this.useDEFT)
			lb=LoadBalancer.PEFT;
		SRSimul simulator =new SRSimul(projB.getNetworkTopologyBox().getNetworkTopology().copy(),lb);
		simulator.setConfigureSRPath(true);
		simulator.computeLoads(weights, p, demands);		
		NetworkLoads loads=simulator.getLoads();
		results.addNetworkLoads(loads);
		ResultSimulType resultsT = new ResultSimulType(results);
		projB.addResultSimul(resultsT);
		projB.addSRConfiguration(simulator.getSRconfiguration());
	}
}
