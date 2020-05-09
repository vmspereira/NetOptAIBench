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
import pt.uminho.algoritmi.netopt.ospf.simulation.OSPFWeights;
import pt.uminho.algoritmi.netopt.ospf.simulation.sr.SRSimulator;
import pt.uminho.netopt.aibench.datatypes.ProjectBox;

@Operation(name = "ComputeLoadsOp", description = "Creates a new SR Simulator")
public class SRSimulatorOp {
	
	private ProjectBox projB;
	private OSPFWeights weights;
	
	
	@Port(direction = Direction.INPUT, name = "Project", order = 1)
	public void setProject(ProjectBox projB) {
		this.projB = projB;
	}

	@Port(direction = Direction.INPUT, name = "OSPFWeights", order = 2)
	public void getWeights(OSPFWeights weights) {
		this.weights = weights;
		SRSimulator sim = new SRSimulator(projB.getNetworkTopologyBox().getNetworkTopology().copy(),weights);
		projB.addSRSimulator(sim);
	}

	
}
