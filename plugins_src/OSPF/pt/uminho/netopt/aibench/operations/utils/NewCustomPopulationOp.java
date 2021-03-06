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
* @author V�tor Pereira
*/
package pt.uminho.netopt.aibench.operations.utils;

import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import pt.uminho.algoritmi.netopt.ospf.simulation.Population;
import pt.uminho.netopt.aibench.datatypes.ProjectBox;

@Operation(description = "New Custom Population")
public class NewCustomPopulationOp {

	private ProjectBox projB;
	
	@Port(direction = Direction.INPUT, name = "Project Box",order = 1)
	public void setProject(ProjectBox projB) {
		this.projB = projB;
		buildPopulation();
	}

	private void buildPopulation() {
		Population p =new Population();
		projB.addCustomPopulation(p);
	}
}
