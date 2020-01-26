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
import pt.uminho.algoritmi.netopt.ospf.simulation.PValues;
import pt.uminho.algoritmi.netopt.ospf.simulation.ResultSimul;
import pt.uminho.algoritmi.netopt.ospf.simulation.Simul;
import pt.uminho.algoritmi.netopt.ospf.simulation.Simul.LoadBalancer;
import pt.uminho.algoritmi.netopt.ospf.simulation.exception.DimensionErrorException;
import pt.uminho.algoritmi.netopt.ospf.simulation.simulators.ISimulator;
import pt.uminho.algoritmi.netopt.ospf.simulation.simulators.PDEFTSimul;
import pt.uminho.netopt.aibench.datatypes.ProjectBox;
import pt.uminho.netopt.aibench.datatypes.ResultSimulType;

@Operation(name = "ComputeLoadsOp", description = "Compute Loads and Congestion")
public class ComputeLoadsOp {
	private ProjectBox projB;
	private OSPFWeights weights;
	private Demands demands;
	private LoadBalancer loadbalancer;
	private PValues pvalues;

	@Port(direction = Direction.INPUT, name = "Project", order = 1)
	public void setProject(ProjectBox projB) {
		this.projB = projB;
	}

	@Port(direction = Direction.INPUT, name = "OSPFWeights", order = 2)
	public void setWeights(OSPFWeights weights) {
		this.weights = weights;
	}

	@Port(direction = Direction.INPUT, name = "Demands", order = 3)
	public void setDemands(Demands demands){
		this.demands = demands;
	}
	

	@Port(direction = Direction.INPUT, name = "LoadBalancer", order = 4)
	public void setLoadBalancer(LoadBalancer loadbalancer){
		this.loadbalancer = loadbalancer;
	}

	
	@Port(direction = Direction.INPUT, name = "Node-p Values", defaultValue="null", allowNull =true,order = 5)
	public void setPValues(PValues pvalues) throws DimensionErrorException{
		this.pvalues = pvalues;
		computeLoads();
	}
	
	
	public void computeLoads() throws DimensionErrorException {
		
		ResultSimul results = new ResultSimul();
		results.addDemands(demands);
		results.addWeights(weights);
		
		ISimulator s;
		if(pvalues==null || loadbalancer.equals(LoadBalancer.ECMP) || loadbalancer.equals(LoadBalancer.NOLB))
		{
			Simul simul =new Simul(projB.getNetworkTopologyBox().getNetworkTopology().copy());
			simul.setLoadBalancer(loadbalancer);
			simul.computeLoads(weights, demands);
			s = (ISimulator)simul;
		}else{
			PDEFTSimul simul = new PDEFTSimul(projB.getNetworkTopologyBox().getNetworkTopology().copy());
			simul.setLoadBalancer(loadbalancer);
			simul.computeLoads(weights, demands, pvalues);
			s = (ISimulator)simul;
		}
		
		NetworkLoads loads=s.getLoads();
		results.addNetworkLoads(loads);
		ResultSimulType resultsT = new ResultSimulType(results);
		projB.addResultSimul(resultsT);
	}
}
