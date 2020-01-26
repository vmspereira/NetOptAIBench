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
import pt.uminho.algoritmi.netopt.ospf.simulation.PValues;
import pt.uminho.netopt.aibench.datatypes.ProjectBox;

@Operation(name = "RandomPValuesOp", description = "Random P Values")
public class RandomPValuesOp {
	private ProjectBox projectB;
	private double maxW;
	private double minW;

	@Port(direction = Direction.INPUT, name = "Project Box", order = 1)
	public void getProject(ProjectBox projB) {
		this.projectB = projB;
	}

	
	@Port(direction = Direction.INPUT, name = "Minimum Weight", order = 2, defaultValue = "0.01")
	public void getMinWeight(double minW) {
		this.minW = minW;
	}
	
	
	@Port(direction = Direction.INPUT, name = "Maximum Weight", order = 2, defaultValue = "10.0")
	public void getMWeight(double maxW) {
		this.maxW = maxW;
		setRandomWeights();
	}

	public void setRandomWeights() {
		PValues p = new PValues(projectB.getNetworkTopologyBox()
				.getNetworkTopology().getDimension());
		p.setRandomWeights(minW,maxW);

		projectB.addPValues(p);
	}
}
