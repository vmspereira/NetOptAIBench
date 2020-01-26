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
package pt.uminho.netopt.aibench.operations.weights;


import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import pt.uminho.algoritmi.netopt.ospf.simulation.OSPFWeights;
import pt.uminho.netopt.aibench.datatypes.ProjectBox;

@Operation(name = "InvCapOp", description = "InvCap heuristic")
public class InvCapOp {
	private ProjectBox projectB;
	private int maxW;
	private int minW;
	
	@Port(direction = Direction.INPUT, name = "Project Box", order = 1)
	public void getProject(ProjectBox projB) {
		this.projectB = projB;
	}

	
	@Port(direction = Direction.INPUT, name = "Minimum Weight", order = 2, defaultValue = "1")
	public void getMinWeight(int minW) {
		this.minW = minW;
	}
	
	@Port(direction = Direction.INPUT, name = "Maximum Weight", order = 3, defaultValue = "20")
	public void getMWeight(int maxW) {
		this.maxW = maxW;
		setInvCapWeights();
	}

	public void setInvCapWeights() {
		OSPFWeights weights = new OSPFWeights(projectB.getNetworkTopologyBox()
				.getNetworkTopology().getDimension());
		weights.setInvCapWeights(minW,maxW, projectB.getNetworkTopologyBox()
				.getNetworkTopology());

		projectB.addOSPFWeights(weights);
	}
}
