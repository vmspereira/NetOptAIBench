/*******************************************************************************
 * /**
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
import pt.uminho.algoritmi.netopt.ospf.simulation.DelayRequests;
import pt.uminho.algoritmi.netopt.ospf.simulation.Demands;
import pt.uminho.algoritmi.netopt.ospf.simulation.OSPFWeights;
import pt.uminho.algoritmi.netopt.ospf.simulation.ResultSimul;
import pt.uminho.algoritmi.netopt.ospf.simulation.Simul;
import pt.uminho.algoritmi.netopt.ospf.simulation.Simul.LoadBalancer;
import pt.uminho.algoritmi.netopt.ospf.simulation.exception.DimensionErrorException;
import pt.uminho.netopt.aibench.datatypes.ProjectBox;
import pt.uminho.netopt.aibench.datatypes.ResultSimulType;

@Operation(name = "ComputeLDOp", description = "Compute Loads and Delays")
public class ComputeLDOp {
	private ProjectBox projB;
	private OSPFWeights weights;
	private Demands demands;
	private DelayRequests delayReqs;
	private LoadBalancer loadbalancer;

	@Port(direction = Direction.INPUT, name = "Project", order = 1)
	public void setProject(ProjectBox projB) {
		this.projB = projB;
	}

	@Port(direction = Direction.INPUT, name = "OSPFWeights", order = 2)
	public void getWeights(OSPFWeights weights) {
		this.weights = weights;
	}

	@Port(direction = Direction.INPUT, name = "Demands", order = 3)
	public void getDemands(Demands demands) {
		this.demands = demands;
	}

	@Port(direction = Direction.INPUT, name = "Delay Requests", order = 4)
	public void getDelayRequests(DelayRequests delayReqs)
			throws DimensionErrorException {
		this.delayReqs = delayReqs;
	}

	
	@Port(direction = Direction.INPUT, name = "LoadBalancer", order = 5)
	public void getLoadBalancer(LoadBalancer loadbalancer) throws DimensionErrorException {
		this.loadbalancer = loadbalancer;
		compute();
	}

	
	
	
	public void compute() throws DimensionErrorException {
		ResultSimul results = new ResultSimul();
		results.addDemands(demands);
		results.setDelayReqs(delayReqs);
		results.addWeights(weights);
		
		Simul simulator =new Simul(projB.getNetworkTopologyBox().getNetworkTopology());
		simulator.setLoadBalancer(loadbalancer);
		simulator.computeLoads(weights, demands);		
		
		
		simulator.computeLoads(weights, demands);
		results.addNetworkLoads(simulator.getLoads());

		simulator.computeDelays(weights, delayReqs);
		results.setEndToEndDelays(simulator.getAverageEndToEndDelays());

		ResultSimulType resultsT = new ResultSimulType(results);
		projB.addResultSimul(resultsT);
	}

}
